/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class ColumnDeleteCommand extends Command {
	private Column column;
	private Table parent;
	private int index;

	public ColumnDeleteCommand(Table parent, Column column) {
		this.column = column;
		this.parent = parent;
		setLabel("Column Delete");
	}

	public boolean canExecute() {
		if (column.isFk()) {
			return false;
		}
		List<Connection> cons = parent.getOutConnections();
		for (Connection c : cons) {
			if (c.getPkColumn() != null && c.getPkColumn().equals(column)) {
				return false;
			}
		}
		return true;
	}

	public void execute() {
		redo();
	}

	public void redo() {
		index = parent.getColumns().indexOf(column);
		parent.removeColumn(column);
	}

	public void undo() {
		parent.addColumn(index, column);
	}

}