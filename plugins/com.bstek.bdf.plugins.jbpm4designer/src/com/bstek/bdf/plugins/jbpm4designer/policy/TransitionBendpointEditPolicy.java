/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.policy;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import com.bstek.bdf.plugins.jbpm4designer.command.CreateBendpointCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.DeleteBendpointCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.MoveBendpointCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
/**
 * @author Jacky
 */
public class TransitionBendpointEditPolicy extends BendpointEditPolicy {
	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		CreateBendpointCommand command=new CreateBendpointCommand();
		Point point = request.getLocation();
		int index=request.getIndex();
		Transition transition=(Transition)getHost().getModel();
		command.setIndex(index);
		command.setPoint(point);
		command.setTransition(transition);
		return command;
	}

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		DeleteBendpointCommand command=new DeleteBendpointCommand();
		int index=request.getIndex();
		Transition transition=(Transition)getHost().getModel();
		command.setIndex(index);
		command.setTransition(transition);
		return command;
	}

	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendpointCommand command=new MoveBendpointCommand();
		Point point = request.getLocation();
		int index=request.getIndex();
		Transition transition=(Transition)getHost().getModel();
		command.setIndex(index);
		command.setPoint(point);
		command.setTransition(transition);
		return command;
	}
}
