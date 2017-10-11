/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class TableLayoutCommand extends Command {
	private Rectangle newBounds;
	private Rectangle oldBounds;
	private ChangeBoundsRequest request;

	private final Table table;

	public TableLayoutCommand(Table table, ChangeBoundsRequest req, Rectangle newBounds) {
		if (table == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.table = table;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		setLabel("Table Move/Resize");
	}

	public boolean canExecute() {
		Object type = request.getType();
		return (RequestConstants.REQ_MOVE.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type) || RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN
				.equals(type));
	}

	public void execute() {
		oldBounds = new Rectangle(table.getConstraints());
		if (newBounds.getSize().height() < Table.h) {
			newBounds.setHeight(Table.h);
		}
		if (newBounds.getSize().width() < Table.w) {
			newBounds.setWidth(Table.w);
		}
		redo();
	}

	public void redo() {
		table.setConstraints(newBounds);
		resetSelfConnection();
	}

	public void undo() {
		table.setConstraints(oldBounds);
		resetSelfConnection();
	}

	private void resetSelfConnection() {
		Connection c = table.getSelfConnection();
		if (c != null) {
			c.setSelfConnectionBendpoint();
		}
	}
}
