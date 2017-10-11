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
public class MoveBendpointCommand extends AbstractBendpointCommand {
	@Override
	public void execute() {
		this.transition.removeBendpoint(index);
		this.transition.addBendpoint(index, point);
	}

	@Override
	public void undo() {
		if(index>-1){
			this.transition.removeBendpoint(index);			
		}
	}
}
