package com.bstek.bdf2.jbpm4.context;

import java.util.Map;

import org.jbpm.api.task.Task;

import com.bstek.bdf2.core.business.IMessageVariableRegister;

/**
 * @author Jacky.gao
 * @since 2013-3-29
 */
public interface IJbpm4MessageVariableRegister extends IMessageVariableRegister {
	public static final String TYPE="jbpm4";
	Map<String,String> fetchMessages(Task task,String assignee);
}
