package com.bstek.bdf2.jbpm5.task.event.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.bstek.bdf2.jbpm5.BpmJpaDao;
import com.bstek.bdf2.jbpm5.model.StatusOperation;
import com.bstek.bdf2.jbpm5.model.Task;
import com.bstek.bdf2.jbpm5.model.TaskLog;
import com.bstek.bdf2.jbpm5.task.event.ITaskEvent;

/**
 * @author Jacky.gao
 * @since 2013-4-17
 */
public class TaskLogEvent extends BpmJpaDao implements ITaskEvent {

	public boolean support(StatusOperation operation) {
		return true;
	}

	@SuppressWarnings("unchecked")
	public void execute(Task task) {
		EntityManager em=this.getEntityManager();
		String ql="from "+TaskLog.class.getName()+" t where t.taskId=:taskId";
		List<TaskLog> taskLogs=em.createQuery(ql).setParameter("taskId",task.getId()).getResultList();
		TaskLog taskLog=null;
		if(taskLogs.size()==0){
			taskLog=new TaskLog();
		}else{
			taskLog=taskLogs.get(0);
		}
		buildTaskLog(taskLog,task);
		em.persist(taskLog);
	}
	private void buildTaskLog(TaskLog taskLog,Task task){
		taskLog.setActor(task.getActor());
		taskLog.setBusinessId(task.getBusinessId());
		taskLog.setCmnt(task.getCmnt());
		taskLog.setCreate(task.getCreate());
		taskLog.setEnd(task.getEnd());
		taskLog.setFaultMessage(task.getFaultMessage());
		taskLog.setName(task.getName());
		taskLog.setOwner(task.getOwner());
		taskLog.setParentId(task.getParentId());
		taskLog.setPreviousStatus(task.getPreviousStatus());
		taskLog.setPriority(task.getPriority());
		taskLog.setProcessId(task.getProcessId());
		taskLog.setProcessInstanceId(task.getProcessInstanceId());
		taskLog.setSessionId(task.getSessionId());
		taskLog.setSkippable(task.isSkippable());
		taskLog.setStart(task.getStart());
		taskLog.setStatus(task.getStatus());
		taskLog.setTaskId(task.getId());
		taskLog.setType(task.getType());
		taskLog.setWorkItemId(task.getWorkItemId());
	}
}
