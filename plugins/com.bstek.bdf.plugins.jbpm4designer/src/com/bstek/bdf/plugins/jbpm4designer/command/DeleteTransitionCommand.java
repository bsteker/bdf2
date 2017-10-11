/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
/**
 * @author Jacky
 */
public class DeleteTransitionCommand extends Command {
	private Transition transition;
	public DeleteTransitionCommand(Transition transition){
		this.transition=transition;
	}
	@Override
	public void execute() {
		this.transition.disconnection();
	}
	@Override
	public void undo() {
		this.transition.reconnection();
	}
}
