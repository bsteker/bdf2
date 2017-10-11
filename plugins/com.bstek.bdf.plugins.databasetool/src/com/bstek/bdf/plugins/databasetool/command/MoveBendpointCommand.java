/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;


import org.eclipse.draw2d.Bendpoint;

import com.bstek.bdf.plugins.databasetool.model.ConnectionBendpoint;

public class MoveBendpointCommand extends BaseBendpointCommand {

	private Bendpoint oldBendpoint;

	public void execute() {
		ConnectionBendpoint bp = new ConnectionBendpoint();
		bp.setRelativeDimensions(getFirstRelativeDimension(),
				getSecondRelativeDimension());
		setOldBendpoint((Bendpoint) getConn().getBendpoints().get(getIndex()));
		getConn().setBendpoint(getIndex(), bp);
		super.execute();
	}

	protected Bendpoint getOldBendpoint() {
		return oldBendpoint;
	}

	public void setOldBendpoint(Bendpoint bp) {
		oldBendpoint = bp;
	}

	public void undo() {
		super.undo();
		getConn().setBendpoint(getIndex(), getOldBendpoint());
	}

}
