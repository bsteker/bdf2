/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.bstek.bdf.plugins.databasetool.command.ConnectionDeleteCommand;
import com.bstek.bdf.plugins.databasetool.model.Connection;

public class ConnectionDeleteEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		ConnectionDeleteCommand command = new ConnectionDeleteCommand((Connection) getHost().getModel());
		return command;
	}

}
