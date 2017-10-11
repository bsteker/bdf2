package com.bstek.bdf2.jbpm5.task.event;

import com.bstek.bdf2.jbpm5.model.StatusOperation;
import com.bstek.bdf2.jbpm5.model.Task;

/**
 * @author Jacky.gao
 * @since 2013-4-12
 */
public interface ITaskEvent {
	boolean support(StatusOperation operation);
	void execute(Task task);
}
