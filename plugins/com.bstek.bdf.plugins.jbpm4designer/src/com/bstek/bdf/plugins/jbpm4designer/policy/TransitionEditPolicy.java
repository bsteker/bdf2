/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.bstek.bdf.plugins.jbpm4designer.command.CreateTransitionCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.TransitionReconnectionCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
/**
 * @author Jacky
 */
public class TransitionEditPolicy extends GraphicalNodeEditPolicy {
	private AbstractNodeElement node;
	public TransitionEditPolicy(AbstractNodeElement node){
		this.node=node;
	}
	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		CreateTransitionCommand command=(CreateTransitionCommand)request.getStartCommand();
		command.setTarget(node);
		return command;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		request.setStartCommand(new CreateTransitionCommand(node));
		return request.getStartCommand();
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Transition transition=(Transition)request.getConnectionEditPart().getModel();
		AbstractNodeElement target=(AbstractNodeElement)request.getTarget().getModel();
		TransitionReconnectionCommand command=new TransitionReconnectionCommand(transition);
		command.setTarget(target);
		return command;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Transition transition=(Transition)request.getConnectionEditPart().getModel();
		AbstractNodeElement target=(AbstractNodeElement)request.getTarget().getModel();
		TransitionReconnectionCommand command=new TransitionReconnectionCommand(transition);
		command.setSource(target);
		return command;
	}
}
