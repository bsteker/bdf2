/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.databasetool.model.Connection;

public class ConnectionModifyCommand extends Command {
	private Connection connection;
	private String constraintName;
	private String oldConstraintName;
	private String type;
	private String oldType;

	public ConnectionModifyCommand(Connection connection, String constraintName, String type) {
		this.connection = connection;
		this.constraintName = constraintName;
		this.type = type;
		setLabel("Connection Modify");
	}

	public boolean canExecute() {
		if (connection.getConstraintName().equals(constraintName) && connection.getType().equals(type)) {
			return false;
		}
		return true;
	}

	public void execute() {
		this.oldConstraintName = connection.getConstraintName();
		this.oldType = connection.getType();
		redo();
	}

	public void redo() {
		connection.setConstraintName(constraintName);
		connection.setType(type);
		connection.fireModelModifyEvent();
	}

	public void undo() {
		connection.setConstraintName(oldConstraintName);
		connection.setType(oldType);
		connection.fireModelModifyEvent();
	}
}