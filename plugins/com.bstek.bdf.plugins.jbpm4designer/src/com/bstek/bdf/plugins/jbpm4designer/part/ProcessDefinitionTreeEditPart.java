/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
/**
 * @author Jacky
 */
public class ProcessDefinitionTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener{
	public ProcessDefinitionTreeEditPart(ProcessDefinition processDefinition){
		processDefinition.addPropertyChangeListener(this);
		this.setModel(processDefinition);
	}
	@Override
	protected String getText() {
		return "流程";
	}

	@Override
	protected List<AbstractNodeElement> getModelChildren() {
		ProcessDefinition processDefinition=(ProcessDefinition)this.getModel();
		return processDefinition.getNodes();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName=evt.getPropertyName();
		if(propertyName.equals(AbstractNodeElement.NODE_ADD) || propertyName.equals(AbstractNodeElement.NODE_REMOVE)){
			this.refreshChildren();
		}
	}
}
