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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
import com.bstek.bdf.plugins.jbpm4designer.figure.AbstractNodeFigure;
import com.bstek.bdf.plugins.jbpm4designer.manager.NodeCellEditorLocator;
import com.bstek.bdf.plugins.jbpm4designer.manager.NodeDirectEditManager;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
import com.bstek.bdf.plugins.jbpm4designer.policy.DeleteNodeEditPolicy;
import com.bstek.bdf.plugins.jbpm4designer.policy.NodeDirectEditPolicy;
import com.bstek.bdf.plugins.jbpm4designer.policy.TransitionEditPolicy;
import com.bstek.bdf.plugins.jbpm4designer.preference.NodeImageConfig;
/**
 * @author Jacky
 */
public abstract class NodeElementEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener,NodeEditPart{
	public NodeElementEditPart(AbstractNodeElement node){
		node.addPropertyChangeListener(this);
		this.setModel(node);
	}
	
	@Override
	public AbstractNodeElement getModel() {
		return (AbstractNodeElement)super.getModel();
	}
	
	@Override
	protected void refreshVisuals() {
		AbstractNodeElement node = getModel();
		Rectangle bounds = new Rectangle(node.getX(), node.getY(), node.getWidth(), node.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}
	
	@Override
	protected List<Transition> getModelSourceConnections() {
		List<Transition> list=this.getModel().getOutTransitions();
		transformTransition(list);
		return list;
	}
	@Override
	protected List<Transition> getModelTargetConnections() {
		List<Transition> list=this.getModel().getInTransitions();
		transformTransition(list);
		return list;
	}

	protected Image getNodeImage(){
		NodeImageConfig config=Activator.getPreference().getNodeImageConfigByName(this.getModel().nodeName());
		if(config==null){
			throw new RuntimeException("当前环境未配置节点"+this.getModel().nodeName()+"的预定义信息");
		}
		return config.getImage();
	}
	private void transformTransition(List<Transition> list){
		ProcessDefinition pd=(ProcessDefinition)this.getParent().getModel();
		for(Transition trans:list){
			if(trans.getSource()==null){
				for(AbstractNodeElement node:pd.getNodes()){
					if(node.getLabel().equals(trans.getSourceNodeName())){
						trans.setSource(node);
						break;
					}
				}
			}
			if(trans.getTarget()==null){
				for(AbstractNodeElement node:pd.getNodes()){
					if(node.getLabel().equals(trans.getTargetNodeName())){
						trans.setTarget(node);
						break;
					}
				}
			}
		}
	}
	
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new TransitionEditPolicy(getModel()));
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE,new DeleteNodeEditPolicy());
		this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NodeDirectEditPolicy());
	}
	
	@Override
	public void performRequest(Request req) {
		if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
			NodeDirectEditManager manager=new NodeDirectEditManager(this,TextCellEditor.class,new NodeCellEditorLocator(this.getModel()));
			manager.show();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String eventName=event.getPropertyName();
		if(eventName.equals(AbstractNodeElement.NODE_MOVE) || eventName.equals(AbstractNodeElement.NODE_RESIZE)){
			this.refreshVisuals();
		}
		if(eventName.equals(AbstractNodeElement.SOURCE_TRANSITION_DROP) || eventName.equals(AbstractNodeElement.TRANSITION_REMOVE)){
			this.refreshSourceConnections();
		}
		if(eventName.equals(AbstractNodeElement.TARGET_TRANSITION_DROP) || eventName.equals(AbstractNodeElement.TRANSITION_REMOVE)){
			this.refreshTargetConnections();
		}
		if(eventName.equals(AbstractNodeElement.LABEL_CHANGED)){
			((AbstractNodeFigure)this.getFigure()).setLabel((String)event.getNewValue());
		}
	}
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new ChopboxAnchor(this.getFigure());
	}
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new ChopboxAnchor(this.getFigure());
	}
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(this.getFigure());
	}
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(this.getFigure());
	}
}
