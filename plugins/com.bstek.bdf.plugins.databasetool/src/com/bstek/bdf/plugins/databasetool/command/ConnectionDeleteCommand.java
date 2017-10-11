/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;

public class ConnectionDeleteCommand extends Command {

	private Connection connection;
	private boolean flag = false;
	private Column column;
	private int index;

	public ConnectionDeleteCommand(Connection connection) {
		super();
		setLabel("Connection Delete");
		this.connection = connection;
	}

	@Override
	public void execute() {
		flag = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "提示", "删除外键约束时是否删除对应的列字段？");
		column = connection.getFkColumn();
		index = connection.getTarget().getColumns().indexOf(column);
		redo();
	}

	@Override
	public void undo() {
		if (flag) {
			connection.getTarget().addColumn(index, column);
		} else {
			column.setFk(true);
			column.fireModelModifyEvent();
		}
		connection.reconnect();
	}

	@Override
	public void redo() {
		if (flag) {
			connection.getTarget().removeColumn(column);
		} else {
			column.setFk(false);
			column.fireModelModifyEvent();
		}
		connection.disconnect();
	}

}