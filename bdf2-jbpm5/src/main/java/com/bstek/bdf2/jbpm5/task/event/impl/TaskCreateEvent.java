package com.bstek.bdf2.jbpm5.task.event.impl;

import com.bstek.bdf2.jbpm5.model.StatusOperation;
import com.bstek.bdf2.jbpm5.model.Task;
import com.bstek.bdf2.jbpm5.task.event.ITaskEvent;

/**
 * @author Jacky.gao
 * @since 2013-4-12
 */
public class TaskCreateEvent implements ITaskEvent {
	public boolean support(StatusOperation operation){
		if(!operation.equals(StatusOperation.Create)){
			return true;
		}
		return false;
	}
	public void execute(Task task) {
		
	}
}
