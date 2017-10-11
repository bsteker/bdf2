/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.EndNode;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.model.StartNode;
/**
 * @author Jacky
 */
public class CreateNodeCommand extends Command {
	private AbstractNodeElement node;
	private ProcessDefinition processDefinition;
	private Rectangle box;
	public CreateNodeCommand(ProcessDefinition processDefinition,AbstractNodeElement node,Rectangle box){
		this.node=node;
		this.box=box;
		this.processDefinition=processDefinition;
	}
	@Override
	public boolean canExecute() {
		if(node instanceof StartNode){
			boolean can=true;
			for(AbstractNodeElement n:processDefinition.getNodes()){
				if(n instanceof StartNode){
					can=false;
					break;
				}
			}
			return can;
		}else if(node instanceof EndNode){
			boolean can=true;
			for(AbstractNodeElement n:processDefinition.getNodes()){
				if(n instanceof EndNode){
					can=false;
					break;
				}
			}
			return can;
			
		}else{
			return true;
		}
	}

	@Override
	public void execute() {
		this.node.setX(box.x);
		this.node.setY(box.y);
		this.processDefinition.addNode(node);
		if(node instanceof StartNode || node instanceof EndNode){
			return;
		}
	}
	@Override
	public void undo() {
		this.processDefinition.removeNode(node);
	}
}
