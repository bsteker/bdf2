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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.policy.NodeXYLayoutEditPolicy;
/**
 * @author Jacky
 */
public class ProcessDefinitionEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener{
	public ProcessDefinitionEditPart(ProcessDefinition processDefinition){
		this.setModel(processDefinition);
		processDefinition.addPropertyChangeListener(this);
	}
	@Override
	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}

	@Override
	protected List<AbstractNodeElement> getModelChildren() {
		List<AbstractNodeElement> list=new ArrayList<AbstractNodeElement>();
		list.addAll(((ProcessDefinition)this.getModel()).getNodes());
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if(type.equals(IContentOutlinePage.class)){
			//return new ProcessDefinitionOutlinePage(this.getViewer());
		}
		return super.getAdapter(type);
	}
	@Override
	protected void createEditPolicies() {
		ProcessDefinition processDefinition=(ProcessDefinition)this.getModel();
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,new NodeXYLayoutEditPolicy(processDefinition));
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String eventName=evt.getPropertyName();
		if(eventName.equals(AbstractNodeElement.NODE_ADD) || eventName.equals(AbstractNodeElement.NODE_REMOVE)){
			this.refreshChildren();
		}
	}
}
