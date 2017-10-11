/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.bstek.bdf.plugins.databasetool.command.ColumnDeleteCommand;
import com.bstek.bdf.plugins.databasetool.command.TableDeleteCommand;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class BaseComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		Command c = null;
		if (getHost().getModel() instanceof Table) {
			c = new TableDeleteCommand((Schema) getHost().getParent().getModel(), (Table) getHost().getModel());

		}
		if (getHost().getModel() instanceof Column) {
			c = new ColumnDeleteCommand((Table) getHost().getParent().getModel(), (Column) getHost().getModel());
		}
		return c;
	}

}
