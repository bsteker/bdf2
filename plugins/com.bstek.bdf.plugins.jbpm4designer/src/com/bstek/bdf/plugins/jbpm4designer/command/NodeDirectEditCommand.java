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
/**
 * @author Jacky
 */
public class NodeDirectEditCommand extends Command {
	private AbstractNodeElement node;
	private String label;
	private String oldLabel;
	
	public NodeDirectEditCommand(AbstractNodeElement node,String label){
		this.node=node;
		this.label=label;
	}
	@Override
	public void execute() {
		oldLabel=this.node.getLabel();
		this.node.setLabel(label);
	}
	@Override
	public void undo() {
		this.node.setLabel(oldLabel);
	}
}
