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

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class TableImportCommand extends Command {
	private Schema schema;
	private List<Table> tableList;

	public TableImportCommand(Schema schema, List<Table> tableList) {
		this.schema = schema;
		this.tableList = tableList;
		setLabel("Import Tables");
	}

	public boolean canExecute() {
		return schema != null && schema != null;
	}

	public void execute() {
		redo();
	}

	public void redo() {
		for (Table table : tableList) {
			table.setSchema(schema);
			schema.addTable(table);
		}
	}

	public void undo() {
		for (Table table : tableList) {
			schema.removeTable(table);
		}
	}

}