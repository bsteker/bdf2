/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
/**
 * @author Jacky
 */
public class TransitionReconnectionCommand extends Command {
	private Transition transition;
	private AbstractNodeElement source;
	private AbstractNodeElement target;
	private AbstractNodeElement oldSource;
	private AbstractNodeElement oldTarget;
	
	public TransitionReconnectionCommand(Transition transition){
		this.transition=transition;
	}
	@Override
	public void execute() {
		oldSource=this.transition.getSource();
		oldTarget=this.transition.getTarget();
		this.transition.disconnection();
		if(source!=null){
			this.transition.setSource(source);
		}else if(target!=null){
			this.transition.setTarget(target);
		}
		this.transition.reconnection();
	}

	@Override
	public void undo() {
		this.transition.disconnection();
		this.transition.setSource(oldSource);
		this.transition.setTarget(oldTarget);
		this.transition.reconnection();
	}
	public void setSource(AbstractNodeElement source) {
		this.source = source;
	}
	public void setTarget(AbstractNodeElement target) {
		this.target = target;
	}
}
