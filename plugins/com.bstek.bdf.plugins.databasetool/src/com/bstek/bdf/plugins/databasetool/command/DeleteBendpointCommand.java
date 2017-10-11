/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.draw2d.Bendpoint;

public class DeleteBendpointCommand extends BaseBendpointCommand {

	private Bendpoint bendpoint;

	public void execute() {
		bendpoint = (Bendpoint) getConn().getBendpoints().get(getIndex());
		getConn().removeBendpoint(getIndex());
		super.execute();
	}

	public void undo() {
		super.undo();
		getConn().insertBendpoint(getIndex(), bendpoint);
	}

}
