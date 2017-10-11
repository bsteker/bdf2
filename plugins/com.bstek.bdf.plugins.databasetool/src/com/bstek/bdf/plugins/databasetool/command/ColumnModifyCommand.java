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

public class ColumnModifyCommand extends Command {
	public static final String TEMP_DATA = "TEMP_DATA";
	private Column column;
	private Column oldColumn;
	private Column data;

	public ColumnModifyCommand(Column column, Column data) {
		this.column = column;
		this.data = data;
		setLabel("Column Modify");
	}

	public boolean canExecute() {
		if (data==null) {
			return false;
		}
		return true;
	}

	public void execute() {
		oldColumn = column.getPropertyCopyNewColumn();
		redo();
	}

	public void redo() {
		copyColumnData(column, data);
		column.fireModelModifyEvent();
	}

	public void undo() {
		copyColumnData(column, oldColumn);
		column.fireModelModifyEvent();
	}

	private void copyColumnData(Column c1, Column c2) {
		c1.setId(c2.getId());
		c1.setFk(c2.isFk());
		c1.setPk(c2.isPk());
		c1.setAutoIncrement(c2.isAutoIncrement());
		c1.setLabel(c2.getLabel());
		c1.setName(c2.getName());
		c1.setType(c2.getType());
		c1.setLength(c2.getLength());
		c1.setDecimalLength(c2.getDecimalLength());
		c1.setDefaultValue(c2.getDefaultValue());
		c1.setNotNull(c2.isNotNull());
		c1.setUnique(c2.isUnique());
		c1.setComment(c2.getComment());
		c1.setTable(c2.getTable());
	}

}