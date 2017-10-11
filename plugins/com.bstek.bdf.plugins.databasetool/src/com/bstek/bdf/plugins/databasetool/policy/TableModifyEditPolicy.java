/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.policy;

import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import com.bstek.bdf.plugins.databasetool.action.RequestConstants;
import com.bstek.bdf.plugins.databasetool.command.TableModifyCommand;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class TableModifyEditPolicy extends AbstractEditPolicy {

	@Override
	public Command getCommand(Request request) {
		if (request.getType().equals(RequestConstants.TABLE_MODIFY)) {
			return createTableModifyCommand(request);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Command createTableModifyCommand(Request request) {
		Map<String, Object> map = request.getExtendedData();
		Table model = (Table) getHost().getModel();
		Table data = (Table) map.get(TableModifyCommand.TEMP_DATA);
		return new TableModifyCommand(model, data);
	}

}
