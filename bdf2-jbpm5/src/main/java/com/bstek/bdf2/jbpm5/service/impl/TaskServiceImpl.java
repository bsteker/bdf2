package com.bstek.bdf2.jbpm5.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.lang.StringUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.jbpm5.BpmJpaDao;
import com.bstek.bdf2.jbpm5.model.StatusOperation;
import com.bstek.bdf2.jbpm5.model.Task;
import com.bstek.bdf2.jbpm5.model.TaskDelegate;
import com.bstek.bdf2.jbpm5.model.TaskGroupCandidate;
import com.bstek.bdf2.jbpm5.model.TaskStatus;
import com.bstek.bdf2.jbpm5.model.TaskType;
import com.bstek.bdf2.jbpm5.service.IDataService;
import com.bstek.bdf2.jbpm5.service.IKnowledgeService;
import com.bstek.bdf2.jbpm5.service.ITaskService;
import com.bstek.bdf2.jbpm5.task.TaskSummary;
import com.bstek.bdf2.jbpm5.task.event.TaskEventSupport;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-4-9
 */
public class TaskServiceImpl extends BpmJpaDao implements ITaskService,InitializingBean {
	private IKnowledgeService bpmService;
	private IDataService dataService;
	private TaskEventSupport taskEventSupport;
	public void complete(long taskId) {
		this.completeTask(taskId,null,null);		
	}
	public void complete(long taskId,Map<String,Object> results) {
		this.completeTask(taskId,null,results);		
	}
	public void complete(long taskId, String username) {
		this.completeTask(taskId, username, null);
	}
	public void complete(long taskId, String username,Map<String,Object> results) {
		this.completeTask(taskId, username, results);
	}
	
	public Object getProcessVariable(long taskId,String variableName){
		Task task=this.getTask(taskId);
		StatefulKnowledgeSession session=this.bpmService.getSession(task.getSessionId());
		try{
			RuleFlowProcessInstance pi=(RuleFlowProcessInstance)session.getProcessInstance(task.getProcessInstanceId());
			return pi.getVariable(variableName);
		}finally{
			session.dispose();
		}
	}
	
	private void completeTask(long taskId, String username,Map<String,Object> results) {
		EntityManager em=this.getEntityManager();
		Task task=em.find(Task.class, taskId);
		StatefulKnowledgeSession session=this.bpmService.getSession(task.getSessionId());
		if(StringUtils.isNotEmpty(username)){
			task.setActor(username);
		}else{
			task.setActor(task.getOwner());
		}
		session.getWorkItemManager().completeWorkItem(task.getWorkItemId(),results);
		changeTaskStatus(task,StatusOperation.Complete);
	}
	public void delegate(long taskId, String username, String targetUsername) {
		EntityManager em=this.getEntityManager();
		EntityTransaction trans=em.getTransaction();
		try{
			trans.begin();
			String jpql="select d from "+TaskDelegate.class.getName()+" d where d.task.id=:taskId and d.delegator=:targetUsername";
			int size=em.createQuery(jpql).setParameter("taskId", taskId).setParameter("targetUsername", targetUsername).getResultList().size();
			if(size>0){
				throw new IllegalArgumentException("User "+targetUsername+" has already delegate task "+taskId+"");
			}
			Task task=new Task(taskId);
			TaskDelegate taskDelegate=new TaskDelegate();
			taskDelegate.setTask(task);
			em.persist(taskDelegate);
			taskEventSupport.fireEvents(StatusOperation.Delegate, task);
			trans.commit();
		}catch(Exception ex){
			trans.rollback();
			throw new RuntimeException(ex);
		}
	}

	public void claim(long taskId, String username) {
		this.changeTask(taskId, username, StatusOperation.Claim,null);
	}

	public void release(long taskId, String username) {
		this.changeTask(taskId, username, StatusOperation.Release,null);
	}

	public void start(long taskId, String username) {
		this.changeTask(taskId, username, StatusOperation.Start,null);
	}

	public void suspend(long taskId, String username) {
		this.changeTask(taskId, username, StatusOperation.Suspend,null);
	}

	public void resume(long taskId, String username) {
		this.changeTask(taskId, username, StatusOperation.Resume,null);
	}

	public void fail(long taskId, String username,String faultMessage) {
		this.changeTask(taskId, username, StatusOperation.Fail,faultMessage);
	}

	public void exit(long taskId, String username) {
		this.changeTask(taskId, username, StatusOperation.Exit,null);
	}
	public void stop(long taskId, String username) {
		this.changeTask(taskId, username, StatusOperation.Stop,null);
		
	}
	public void changeTaskOwner(long taskId, String newOwner) {
		Task task=this.getTask(taskId);
		if(task.getStatus().equals(TaskStatus.Created)){
			EntityManager em=getEntityManager();
			EntityTransaction trans=em.getTransaction();
			try{
				trans.begin();
				task.setOwner(newOwner);
				em.persist(task);
				trans.commit();
			}catch(Exception ex){
				trans.rollback();
				throw new RuntimeException(ex);
			}
		}else{
			throw new IllegalStateException("Current task status is "+task.getStatus()+",can not change it owner!");
		}
	}
	private void changeTask(long taskId,String username,StatusOperation operation,String faultMessage) {
		EntityManager em=getEntityManager();
		EntityTransaction trans=em.getTransaction();
		try{
			trans.begin();
			Task task=em.find(Task.class, taskId);
			if(StringUtils.isNotEmpty(faultMessage)){
				task.setFaultMessage(faultMessage);
			}
			if(changeTaskStatus(task,operation)){
				em.persist(task);				
			}
			TaskStatus status=task.getStatus();
			if(status.equals(TaskStatus.Completed) || status.equals(TaskStatus.Error) || status.equals(TaskStatus.Exited) || status.equals(TaskStatus.Obsolete) || status.equals(TaskStatus.Failed)){
				StatefulKnowledgeSession session=this.bpmService.getSession(task.getSessionId());
				session.getWorkItemManager().completeWorkItem(task.getWorkItemId(),null);	
				session.dispose();
			}
			trans.commit();
		}catch(Exception ex){
			trans.rollback();
			throw new RuntimeException(ex);
		}
	}

	private boolean changeTaskStatus(Task task,StatusOperation operation){
		boolean taskNeedSave=true;
		TaskStatus status=task.getStatus();
		boolean error=false;
		switch(operation){
		case Claim:
			if(status.equals(TaskStatus.Ready)){
				task.setStatus(TaskStatus.Reserved);
			}else{
				error=true;
			}
			break;
		case Release:
			if(status.equals(TaskStatus.Reserved)){
				task.setStatus(TaskStatus.Ready);
			}else{
				error=true;
			}
			break;
		case Start:
			if(status.equals(TaskStatus.Reserved) || status.equals(TaskStatus.Created)){
				task.setStatus(TaskStatus.InProgress);
				task.setStart(new Date());
			}else{
				error=true;
			}
			break;
		case Complete:
			if(status.equals(TaskStatus.InProgress)){
				task.setStatus(TaskStatus.Completed);
				task.setEnd(new Date());
			}else{
				error=true;
			}
			taskNeedSave=false;
			break;
		case Suspend:
			if(status.equals(TaskStatus.Ready) || status.equals(TaskStatus.Reserved) || status.equals(TaskStatus.InProgress)){
				task.setStatus(TaskStatus.Suspended);
				task.setPreviousStatus(status);
			}else{
				error=true;
			}
			break;
		case Resume:
			if(status.equals(TaskStatus.Suspended)){
				task.setStatus(task.getPreviousStatus());
			}else{
				error=true;
			}
			break;
		case Fail:
			if(status.equals(TaskStatus.InProgress)){
				task.setStatus(TaskStatus.Failed);
				task.setEnd(new Date());
			}else{
				error=true;
			}
			taskNeedSave=false;
			break;
		case Exit:
			if(status.equals(TaskStatus.InProgress)){
				task.setStatus(TaskStatus.Failed);
				task.setEnd(new Date());
			}else{
				error=true;
			}
			taskNeedSave=false;
			break;
		case Stop:
			if(status.equals(TaskStatus.InProgress)){
				task.setStatus(task.getPreviousStatus());
			}else{
				error=true;
			}
			break;
		default:
			
			break;
		}
		if(error){
			throw new IllegalArgumentException("Current task status is "+status+" can not change to "+operation);
		}
		taskEventSupport.fireEvents(operation, task);
		return taskNeedSave;
	}
	
	public Task getTask(long taskId) {
		EntityManager em=getEntityManager();
		Task task=em.find(Task.class, taskId);
		return task;
	}
	
	public List<TaskSummary> getTasksOwned(String username) {
		List<TaskStatus> status=new ArrayList<TaskStatus>();
		status.add(TaskStatus.Created);
		return this.converTasks(this.loadTasks(username,status,null));
	}

	public List<TaskSummary> getTasksOwned(String username,List<TaskStatus> status) {
		return this.converTasks(this.loadTasks(username,status,null));
	}

	public void pagingTasksOwned(String username, Page<TaskSummary> page) {
		List<TaskStatus> status=new ArrayList<TaskStatus>();
		status.add(TaskStatus.Created);
		Page<Task> p=new Page<Task>(page.getPageSize(),page.getPageNo());
		this.loadTasks(username, status, p);
		page.setEntities(this.converTasks(p.getEntities()));
		page.setEntityCount(p.getEntityCount());
	}

	public void pagingTasksOwned(String username, Page<TaskSummary> page,List<TaskStatus> status) {
		Page<Task> p=new Page<Task>(page.getPageSize(),page.getPageNo());
		this.loadTasks(username, status, p);
		page.setEntities(this.converTasks(p.getEntities()));
		page.setEntityCount(p.getEntityCount());
	}
	
	private List<Task> loadTasks(String username, List<TaskStatus> status,Page<Task> page){
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("owner", username);
		parameter.put("type", TaskType.Personal);
		parameter.put("status", status);
		String ql=" from "+Task.class.getName()+" t where t.owner=:owner and t.type=:type and t.status in :status order by t.create desc";
		String jpql="select t "+ql;
		if(page!=null){
			String countJpql="select count(*) "+ql;
			pagingQuery(jpql, countJpql, page, parameter);
			return null;
		}else{
			return find(jpql, parameter);
		}
	}

	public List<TaskSummary> getTasksForPotentialOwner(String username) {
		List<TaskStatus> status=new ArrayList<TaskStatus>();
		status.add(TaskStatus.Ready);
		List<Task> tasks=loadTasksForPotentialOwner(username,status,null);
		return this.converTasks(tasks);
	}

	public void getTasksForPotentialOwner(String username,Page<TaskSummary> page) {
		Page<Task> p=new Page<Task>(page.getPageSize(),page.getPageNo());
		List<TaskStatus> status=new ArrayList<TaskStatus>();
		status.add(TaskStatus.Ready);
		loadTasksForPotentialOwner(username,status,p);
		page.setEntities(this.converTasks(p.getEntities()));
		page.setEntityCount(p.getEntityCount());
	}

	public List<TaskSummary> getTasksForPotentialOwner(String username,List<TaskStatus> status) {
		List<Task> tasks=loadTasksForPotentialOwner(username,status,null);
		return this.converTasks(tasks);
	}

	public void getTasksForPotentialOwner(String username,Page<TaskSummary> page, List<TaskStatus> status) {
		Page<Task> p=new Page<Task>(page.getPageSize(),page.getPageNo());
		loadTasksForPotentialOwner(username,status,p);
		page.setEntities(this.converTasks(p.getEntities()));
		page.setEntityCount(p.getEntityCount());
	}
	private List<Task> loadTasksForPotentialOwner(String username,List<TaskStatus> status,Page<Task> page) {
		String[] groupIds=dataService.getUserGroupIds(username);
		if(groupIds==null || groupIds.length<1){
			return null;
		}
		String ql="from "+TaskGroupCandidate.class.getName()+" t where t.groupId in :groupIds and t.task.type=:taskType and t.task.status in :taskStatus order by t.task.create desc";
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("groupIds", groupIds);
		parameter.put("taskType", TaskType.Group);
		parameter.put("taskStatus", status);
		String jpql="select t "+ql;
		List<Task> tasks=new ArrayList<Task>();
		if(page!=null){
			Page<TaskGroupCandidate> p=new Page<TaskGroupCandidate>(page.getPageSize(),page.getPageNo());
			String countJpql="select count(*) "+ql;
			pagingQuery(jpql, countJpql, p, parameter);
			for(TaskGroupCandidate candidate:p.getEntities()){
				tasks.add(candidate.getTask());
			}
			page.setEntities(tasks);
			page.setEntityCount(p.getEntityCount());
			return null;
		}
		List<TaskGroupCandidate> candidateGroupTasks=find(jpql, parameter);
		for(TaskGroupCandidate candidate:candidateGroupTasks){
			tasks.add(candidate.getTask());
		}
		return tasks;			
	}

	public List<TaskSummary> getTasksByStatusAndProcessId(long processInstanceId, List<TaskStatus> status) {
		String jpql="select t from "+Task.class.getName()+" t where t.processInstanceId=:processInstanceId and t.status in :status order by t.create desc";
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("processInstanceId",processInstanceId);
		parameter.put("status", status);
		List<Task> tasks=find(jpql, parameter);
		return this.converTasks(tasks);
	}

	
	public List<TaskSummary> getTasksByStatusByProcessIdAndTaskName(long processInstanceId, List<TaskStatus> status, String taskName) {
		String jpql="select t from "+Task.class.getName()+" t where t.processInstanceId=:processInstanceId and t.status and t.taskName=:taskName in :status order by t.create desc";
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("processInstanceId",processInstanceId);
		parameter.put("status", status);
		parameter.put("taskName", taskName);
		List<Task> tasks=find(jpql, parameter);
		return this.converTasks(tasks);
	}

	public List<TaskSummary> getSubTasks(long taskId) {
		String jpql="select t from "+Task.class.getName()+" t where t.parentId=:parentId order by t.create desc";
		Map<String,Object> parameter=new HashMap<String,Object>();
		parameter.put("parentId",taskId);
		List<Task> tasks=find(jpql, parameter);
		return this.converTasks(tasks);
	}
	
	private List<TaskSummary> converTasks(Collection<Task> tasks){
		List<TaskSummary> result=new ArrayList<TaskSummary>();
		for(Task task:tasks){
			TaskSummary ts=new TaskSummary();
			ts.setId(task.getId());
			ts.setBusinessId(task.getBusinessId());
			ts.setCreateDate(task.getCreate());
			ts.setDescription(task.getCmnt());
			ts.setName(task.getName());
			ts.setOwner(task.getOwner());
			ts.setPriority(task.getPriority());
			ts.setProcessId(task.getProcessId());
			ts.setProcessInstanceId(task.getProcessInstanceId());
			ts.setSessionId(task.getSessionId());
			ts.setStatus(task.getStatus());
			ts.setType(task.getType());
			result.add(ts);
		}
		return result;
	}
	public void setTaskEventSupport(TaskEventSupport taskEventSupport) {
		this.taskEventSupport = taskEventSupport;
	}
	public IKnowledgeService getBpmService() {
		return bpmService;
	}
	public void setBpmService(IKnowledgeService bpmService) {
		this.bpmService = bpmService;
	}
	public IDataService getDataService() {
		return dataService;
	}
	public void setDataService(IDataService dataService) {
		this.dataService = dataService;
	}
	public void afterPropertiesSet() throws Exception {
		Map<String,IDataService> userServiceMap=this.getApplicationContext().getBeansOfType(IDataService.class);
		if(userServiceMap.size()==0){
			throw new RuntimeException("You need implement ["+IDataService.class.getName()+"] interface for bdf2-jbpm5 module");
		}
		dataService=userServiceMap.values().iterator().next();
	}
}
