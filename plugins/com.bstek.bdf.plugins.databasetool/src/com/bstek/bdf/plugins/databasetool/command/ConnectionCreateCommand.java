/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class ConnectionCreateCommand extends Command {
	private Connection connection;
	private final Table source;
	private Table target;
	private Column column;

	public ConnectionCreateCommand(Table source, Connection connection) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel("Connection Create");
		this.source = source;
		this.connection = connection;
	}

	public boolean canExecute() {
		if (target == null) {
			return false;
		}
		for (Iterator<Connection> iter = target.getInConnections().iterator(); iter.hasNext();) {
			Connection conn = (Connection) iter.next();
			if (conn.getSource().equals(source)) {
				return false;
			}
		}
		return true;
	}

	public void execute() {
		connection.setSource(source);
		connection.setTarget(target);
		redo();
	}

	public void redo() {
		Column pkColumn = source.getFirstPkColumn();
		addPkColumn(pkColumn);
		connection.setPkColumn(pkColumn);
		connection.setFkColumn(column);
		connection.reconnect();
		if (source.equals(target)) {
			connection.setSelfConnectionBendpoint();
		}
	}

	public void setTarget(Table target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}

	public void undo() {
		target.removeColumn(column);
		connection.disconnect();
	}

	private void addPkColumn(Column pkColumn) {
		column = new Column();
		column.setId(Column.generateId());
		column.setFk(true);
		column.setPk(false);
		column.setAutoIncrement(false);
		column.setLabel(pkColumn.getLabel());
		column.setName(pkColumn.getTable().getName() + "_" + pkColumn.getName());
		if (source.equals(target)) {
			column.setName("PARENT_" + pkColumn.getName().toUpperCase());
		} else if (pkColumn.getTable().getName().endsWith("_")) {
			column.setName(pkColumn.getTable().getName() + pkColumn.getName());
		} else {
			column.setName(pkColumn.getTable().getName() + "_" + pkColumn.getName());
		}
		column.setType(pkColumn.getType());
		column.setLength(pkColumn.getLength());
		column.setDecimalLength(pkColumn.getDecimalLength());
		column.setDefaultValue(pkColumn.getDefaultValue());
		column.setNotNull(true);
		column.setUnique(false);
		column.setComment(pkColumn.getComment());
		column.setTable(target);
		target.addColumn(column);

	}
}
