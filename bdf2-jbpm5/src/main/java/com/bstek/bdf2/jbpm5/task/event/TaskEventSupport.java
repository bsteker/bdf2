package com.bstek.bdf2.jbpm5.task.event;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.jbpm5.model.StatusOperation;
import com.bstek.bdf2.jbpm5.model.Task;

/**
 * @author Jacky.gao
 * @since 2013-4-12
 */
public class TaskEventSupport implements ApplicationContextAware{
	private Collection<ITaskEvent> taskEvents;
	public void fireEvents(StatusOperation operation,Task task){
		for(ITaskEvent event:taskEvents){
			if(event.support(operation)){
				event.execute(task);
			}
		}
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		taskEvents=applicationContext.getBeansOfType(ITaskEvent.class).values();
	}
}
