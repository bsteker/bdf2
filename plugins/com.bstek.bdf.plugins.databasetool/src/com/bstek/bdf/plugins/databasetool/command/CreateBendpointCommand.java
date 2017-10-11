/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import com.bstek.bdf.plugins.databasetool.model.ConnectionBendpoint;

public class CreateBendpointCommand extends BaseBendpointCommand {

	public void execute() {
		ConnectionBendpoint connectionBendpoint = new ConnectionBendpoint();
		connectionBendpoint.setRelativeDimensions(getFirstRelativeDimension(), getSecondRelativeDimension());
		getConn().insertBendpoint(getIndex(), connectionBendpoint);
		super.execute();
	}

	public void undo() {
		super.undo();
		getConn().removeBendpoint(getIndex());
	}

}
