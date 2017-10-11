/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
/**
 * @author Jacky
 */
public class TransitionLabelDirectEditCommand extends Command {
	private TransitionLabel transitionName;
	private String newName;
	private String oldName;
	public TransitionLabelDirectEditCommand(TransitionLabel transitionName,String newName){
		this.transitionName=transitionName;
		this.newName=newName;
	}
	@Override
	public void execute() {
		oldName=transitionName.getText();
		transitionName.setText(newName);
	}
	@Override
	public void undo() {
		transitionName.setText(oldName);
	}
}
