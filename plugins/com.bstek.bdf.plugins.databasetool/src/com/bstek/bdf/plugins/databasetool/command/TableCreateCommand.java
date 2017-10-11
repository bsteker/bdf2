/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class TableCreateCommand extends Command {
	private Table table;
	private Schema parent;
	private Point point;

	public TableCreateCommand(Table table, Schema parent, Point point) {
		this.table = table;
		this.parent = parent;
		this.point = point;
		setLabel("Table Create");
	}

	public boolean canExecute() {
		return table != null && parent != null && point != null;
	}

	public void execute() {
		Rectangle r = new Rectangle(point, table.getConstraints().getSize());
		int i = parent.getTables().size() + 1;
		table.setName("table" + i);
		table.setLabel("table" + i);
		table.setComment("");
		table.setSchema(parent);
		table.setConstraints(r);
		redo();
	}

	public void redo() {
		parent.addTable(table);
	}

	public void undo() {
		parent.removeTable(table);
	}

}