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
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
/**
 * @author Jacky
 */
public class DeleteNodeCommand extends Command {
	private ProcessDefinition processDefinition;
	private AbstractNodeElement node;
	public DeleteNodeCommand(ProcessDefinition processDefinition,AbstractNodeElement node){
		this.processDefinition=processDefinition;	
		this.node=node;
	}
	@Override
	public void execute() {
		this.processDefinition.removeNode(node);
	}
	@Override
	public void undo() {
		this.processDefinition.addNode(node);
	}
}
