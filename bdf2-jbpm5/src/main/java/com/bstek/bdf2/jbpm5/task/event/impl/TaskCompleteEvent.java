package com.bstek.bdf2.jbpm5.task.event.impl;

import com.bstek.bdf2.jbpm5.BpmJpaDao;
import com.bstek.bdf2.jbpm5.model.StatusOperation;
import com.bstek.bdf2.jbpm5.model.Task;
import com.bstek.bdf2.jbpm5.task.event.ITaskEvent;

/**
 * @author Jacky.gao
 * @since 2013-4-12
 */
public class TaskCompleteEvent extends BpmJpaDao implements ITaskEvent {
	public boolean support(StatusOperation operation){
		if(operation.equals(StatusOperation.Complete) || operation.equals(StatusOperation.Fail) || operation.equals(StatusOperation.Exit)){
			return true;
		}
		return false;
	}
	public void execute(Task task) {
		this.getEntityManager().remove(task);
	}
}
