/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.bstek.bdf.plugins.jbpm4designer.command.CreateNodeCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.NodeMoveAndResizeCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.part.NodeElementEditPart;
/**
 * @author Jacky
 */
public class NodeXYLayoutEditPolicy extends XYLayoutEditPolicy {
	private ProcessDefinition processDefinition;
	public NodeXYLayoutEditPolicy(ProcessDefinition processDefinition){
		this.processDefinition=processDefinition;
	}
	
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if(child instanceof NodeElementEditPart){
			return new ResizableEditPolicy();
		}else{
			return new NonResizableEditPolicy();			
		}
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		Object newObj=request.getNewObject();
		if(newObj instanceof AbstractNodeElement){
			AbstractNodeElement node=(AbstractNodeElement)newObj;
			Rectangle box=(Rectangle)getConstraintFor(request);
			return new CreateNodeCommand(processDefinition,node,box);
		}
		return null;
	}

	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		Rectangle box=(Rectangle)constraint;
		AbstractNodeElement node=(AbstractNodeElement)child.getModel();
		return new NodeMoveAndResizeCommand(node, box);
	}
}
