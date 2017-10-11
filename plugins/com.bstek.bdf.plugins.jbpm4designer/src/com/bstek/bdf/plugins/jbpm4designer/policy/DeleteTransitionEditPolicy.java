/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.bstek.bdf.plugins.jbpm4designer.command.DeleteTransitionCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
/**
 * @author Jacky
 */
public class DeleteTransitionEditPolicy extends ConnectionEditPolicy {
	private Transition transition;
	public DeleteTransitionEditPolicy(Transition transition){
		this.transition=transition;
	}
	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		return new DeleteTransitionCommand(transition);
	}
}
