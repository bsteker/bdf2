/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class TableDeleteCommand extends Command {
	private Schema schema;
	private Table model;
	private List<Connection> inConnections = new ArrayList<Connection>();
	private List<Connection> outConnections = new ArrayList<Connection>();
	public TableDeleteCommand(Schema schema, Table model) {
		super();
		this.schema = schema;
		this.model = model;
		setLabel("Table Delete");
		inConnections.addAll(model.getInConnections());
		outConnections.addAll(model.getOutConnections());
	}

	@Override
	public void execute() {
		redo();
	}

	@Override
	public void undo() {
		for(Connection c:inConnections){
			c.reconnect();
		}
		for(Connection c:outConnections){
			c.reconnect();
			Column fkColumn = c.getFkColumn();
			fkColumn.setFk(true);
			fkColumn.fireModelModifyEvent();
		}
		schema.addTable(model);
	}

	@Override
	public void redo() {
		for(Connection c:inConnections){
			c.disconnect();
		}
		for(Connection c:outConnections){
			c.disconnect();
			Column fkColumn = c.getFkColumn();
			fkColumn.setFk(false);
			fkColumn.fireModelModifyEvent();
			
		}
		schema.removeTable(model);
	}
}
