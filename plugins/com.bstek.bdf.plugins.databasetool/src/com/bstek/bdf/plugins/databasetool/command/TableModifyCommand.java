/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class TableModifyCommand extends Command {
	public static final String TEMP_DATA = "TEMP_DATA";
	private Table model;
	private Table data;
	private List<Column> oldColumns;
	private Map<Connection, Column> oldConnctionPkColumnMap = new HashMap<Connection, Column>();
	private Map<Connection, Column> oldConnctionFkColumnMap = new HashMap<Connection, Column>();
	private String oldName;
	private String oldLabel;
	private String oldComment;

	public TableModifyCommand(Table model, Table data) {
		super();
		this.model = model;
		this.data = data;
		setLabel("Table Modify");
	}

	public boolean canExecute() {
		return model != null && data != null;
	}

	public void execute() {
		oldColumns = model.getColumns();
		oldName = model.getName();
		oldLabel = model.getLabel();
		oldComment = model.getComment();
		redo();
	}

	public void redo() {
		model.setName(data.getName());
		model.setLabel(data.getLabel());
		model.setComment(data.getComment());
		for (Column c : data.getColumns()) {
			c.setTable(model);
		}
		model.setColumns(data.getColumns());
		updateConnectionFkColumn(model, data);
		updateConnectionPkColumn(model, data);
		model.fireModelModifyEvent();
	}

	public void undo() {
		model.setName(oldName);
		model.setLabel(oldLabel);
		model.setComment(oldComment);
		model.setColumns(oldColumns);
		for (Entry<Connection, Column> entry : oldConnctionFkColumnMap.entrySet()) {
			entry.getKey().setFkColumn(entry.getValue());
		}
		for (Entry<Connection, Column> entry : oldConnctionPkColumnMap.entrySet()) {
			entry.getKey().setPkColumn(entry.getValue());
		}
		model.fireModelModifyEvent();
	}

	private void updateConnectionFkColumn(Table model, Table data) {
		List<Connection> ins = model.getInConnections();
		for (Connection c : ins) {
			Column oldColumn = c.getFkColumn();
			Column fkColumn = findFkColumn(c, data);
			c.setFkColumn(fkColumn);
			oldConnctionFkColumnMap.put(c, oldColumn);
		}
	}

	private void updateConnectionPkColumn(Table model, Table data) {
		List<Connection> outs = model.getOutConnections();
		for (Connection c : outs) {
			Column oldColumn = c.getPkColumn();
			Column pkColumn = findPkColumn(c, data);
			c.setPkColumn(pkColumn);
			oldConnctionPkColumnMap.put(c, oldColumn);
		}
	}

	private Column findFkColumn(Connection connection, Table data) {
		String id = connection.getFkColumn().getId();
		List<Column> columns = data.getColumns();
		for (Column c : columns) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}

	private Column findPkColumn(Connection connection, Table data) {
		String id = connection.getPkColumn().getId();
		List<Column> columns = data.getColumns();
		for (Column c : columns) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}

}