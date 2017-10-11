package com.bstek.bdf2.jbpm4.service;

import java.util.Collection;

import com.bstek.bdf2.jbpm4.model.ComponentControl;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
public interface IComponentControlService {
	public static final String BEAN_ID="bdf2.jbpm4.componentControlService";
	Collection<ComponentControl> getComponentControls(String processDefinitionId,String taskName);
}
