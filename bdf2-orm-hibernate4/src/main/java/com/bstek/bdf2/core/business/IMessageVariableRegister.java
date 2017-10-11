package com.bstek.bdf2.core.business;

import java.util.Collection;

import com.bstek.bdf2.core.model.MessageVariable;

/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
public interface IMessageVariableRegister {
	String getTypeId();
	String getTypeName();
	Collection<MessageVariable> getMessageVariables();
}
