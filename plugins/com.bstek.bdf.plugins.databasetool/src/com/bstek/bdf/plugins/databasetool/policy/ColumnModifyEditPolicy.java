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
import com.bstek.bdf.plugins.databasetool.command.ColumnModifyCommand;
import com.bstek.bdf.plugins.databasetool.model.Column;

public class ColumnModifyEditPolicy extends AbstractEditPolicy {

	@Override
	public Command getCommand(Request request) {
		if (request.getType().equals(RequestConstants.COLUMN_MODIFY)) {
			return createColumnModifyCommand(request);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Command createColumnModifyCommand(Request request) {
		Map<String, Object> map = request.getExtendedData();
		Column model = (Column) getHost().getModel();
		Column data = (Column) map.get(ColumnModifyCommand.TEMP_DATA);
		return new ColumnModifyCommand(model, data);
	}

}
