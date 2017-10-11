/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.wizard.pages.dialog.ColumnDialog;

public class ColumnCreateCommand extends Command {
	private Column column;
	private Table parent;
	private int index;
	private boolean flag = false;

	public ColumnCreateCommand(Column column, Table parent, int index) {
		this.column = column;
		this.parent = parent;
		this.index = index;
		setLabel("Column Create");
	}

	public boolean canExecute() {
		return column != null && parent != null;
	}

	public boolean canUndo() {
		if (flag) {
			return true;
		}
		return false;
	}

	public void execute() {
		ColumnDialog dialog = new ColumnDialog(Display.getCurrent().getActiveShell(), column, ColumnDialog.TYPE_NEW);
		if (dialog.open() == Dialog.OK) {
			flag = true;
			column.setTable(parent);
			parent.addColumn(index, column);
		}

	}

	public void redo() {
		parent.addColumn(index, column);
	}

	public void undo() {
		parent.removeColumn(column);
	}

}