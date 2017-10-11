/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class ColumnReorderCommand extends Command {
	private Table table;
	private Column column;
	private int index;
	private int oldIndex;

	public ColumnReorderCommand(Table table, Column column) {
		super("Reorder Column");
		this.table = table;
		this.column = column;
	}

	public void setAfterColumn(Column afterColumn) {
		index = table.getColumns().indexOf(afterColumn) + 1;
	}

	public void execute() {
		oldIndex = table.getColumns().indexOf(column);
		table.removeColumn(column);
		table.addColumn(index <= oldIndex ? index : index - 1, column);
	}

	public void undo() {
		table.removeColumn(column);
		table.addColumn(oldIndex <= index ? oldIndex : oldIndex - 1, column);
	}
}
