package com.bstek.bdf2.jbpm5.workitem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.lang.StringUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.process.instance.impl.ProcessInstanceImpl;
import org.jbpm.process.workitem.AbstractWorkItemHandler;

import com.bstek.bdf2.jbpm5.model.Task;
import com.bstek.bdf2.jbpm5.model.TaskGroupCandidate;
import com.bstek.bdf2.jbpm5.model.TaskStatus;
import com.bstek.bdf2.jbpm5.model.TaskType;
import com.bstek.bdf2.jbpm5.service.ProcessConstants;
import com.bstek.bdf2.jbpm5.task.TaskManager;

/**
 * @author Jacky.gao
 * @since 2013-4-9
 */
public class HumanTaskWorkItemHandler extends AbstractWorkItemHandler {
	private TaskManager taskManager;

	public HumanTaskWorkItemHandler(StatefulKnowledgeSession ksession,
			TaskManager taskManager) {
		super(ksession);
		this.taskManager = taskManager;
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Task task = new Task();
		task.setCreate(new Date());
		task.setWorkItemId(workItem.getId());
		task.setStatus(TaskStatus.Created);
		StatefulKnowledgeSession session=this.getSession();
		task.setSessionId(session.getId());
		ProcessInstance pi=session.getProcessInstance(workItem.getProcessInstanceId());
		task.setProcessId(pi.getProcessId());
		VariableScopeInstance varContextInstance=(VariableScopeInstance)((ProcessInstanceImpl)pi).getContextInstance(VariableScope.VARIABLE_SCOPE);
		String businessId=(String)varContextInstance.getVariable(ProcessConstants.BUSINESS_ID);
		task.setBusinessId(businessId);
		task.setProcessInstanceId(workItem.getProcessInstanceId());
		String taskName = (String) workItem.getParameter("TaskName");
		if(StringUtils.isEmpty(taskName)){
			taskName="Unnamed task";
		}
		task.setName(taskName);
		String comment = (String) workItem.getParameter("Comment");
		task.setCmnt(comment);
		String priorityString = (String) workItem.getParameter("Priority");
		int priority = 0;
		if (priorityString != null) {
			try {
				priority = new Integer(priorityString);
			} catch (NumberFormatException e) {}
		}
		task.setPriority(priority);
		task.setSkippable(!"false".equals(workItem.getParameter("Skippable")));
		String groupId = (String) workItem.getParameter("GroupId");
		String actorId = (String) workItem.getParameter("ActorId");
		EntityManager em=taskManager.getEntityManager();
		EntityTransaction trans=em.getTransaction();
		try{
			trans.begin();
			if (StringUtils.isNotEmpty(actorId)) {
				String[] actorIds = actorId.trim().split(",");
				assignmentByActorIds(task,em,actorIds);
			} else if (StringUtils.isNotEmpty(groupId)) {
				String[] groupIds = groupId.trim().split(",");
				assignmentByGroupIds(task,em,groupIds);
			} else {
				//如果没有指定任务处理人或groupId，那么将采用bdf2提供的动态任务分配类
			}
			trans.commit();
		}catch(Exception ex){
			trans.rollback();
			throw new RuntimeException(ex);
		}

		// Object contentObject = workItem.getParameter("Content");
	}

	private void assignmentByGroupIds(Task task,EntityManager em,String[] groupIds) {
		task.setType(TaskType.Group);
		task.setStatus(TaskStatus.Ready);
		taskManager.getEntityManager().persist(task);
		for (String id : groupIds) {
			id = id.trim();
			if (StringUtils.isNotEmpty(id)) {
				TaskGroupCandidate groupCandidate = new TaskGroupCandidate();
				groupCandidate.setGroupId(id);
				groupCandidate.setTask(task);
				em.persist(groupCandidate);
			}
		}
	}

	private void assignmentByActorIds(Task task,EntityManager em,String[] actorIds) {
		List<String> usernames = new ArrayList<String>();
		for (String username : actorIds) {
			username = username.trim();
			if (StringUtils.isNotEmpty(username)) {
				usernames.add(username);
			}
		}
		if (usernames.size() > 1) {
			// 表明是一个会签类型的多任务
			task.setType(TaskType.Countersign);
			em.persist(task);
			for(String owner:usernames){
				Task subTask=this.buildSubTask(task);
				subTask.setOwner(owner);
				em.persist(subTask);
			}
		} else {
			// 直接分配给某个人的任务
			task.setType(TaskType.Personal);
			task.setOwner(usernames.get(0));
			em.persist(task);
		}
	}
	
	private Task buildSubTask(Task task){
		Task sub=new Task();
		sub.setName(task.getName());
		sub.setBusinessId(task.getBusinessId());
		sub.setCmnt(task.getCmnt());
		sub.setCreate(task.getCreate());
		sub.setPriority(task.getPriority());
		sub.setProcessId(task.getProcessId());
		sub.setProcessInstanceId(task.getProcessInstanceId());
		sub.setSessionId(task.getSessionId());
		sub.setSkippable(task.isSkippable());
		sub.setStatus(TaskStatus.Created);
		sub.setType(TaskType.Subtask);
		sub.setWorkItemId(task.getWorkItemId());
		sub.setParentId(task.getId());
		return sub;
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}
}
