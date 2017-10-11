/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

/**
 * @author Jacky
 */
public class DeleteBendpointCommand extends AbstractBendpointCommand {
	@Override
	public void execute() {
		this.oldIndex=index;
		this.transition.removeBendpoint(index);
	}
	@Override
	public void undo() {
		if(oldIndex>-1 && this.oldPoint!=null){
			transition.addBendpoint(oldIndex,oldPoint);			
		}
	}
}
