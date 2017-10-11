/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.policy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.bstek.bdf.plugins.databasetool.command.ColumnCreateCommand;
import com.bstek.bdf.plugins.databasetool.command.ColumnReorderCommand;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.part.ColumnEditPart;

public class ColumnOrderedLayoutEditPolicy extends OrderedLayoutEditPolicy {

	@Override
	protected Command createAddCommand(EditPart child, EditPart after) {
		return null;
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		if (child == after || getHost().getChildren().size() == 1) {
			return null;
		}
		int index = getHost().getChildren().indexOf(child);
		if (index == 0) {
			if (after == null)
				return null;
		} else {
			if (after == getHost().getChildren().get(index - 1))
				return null;
		}
		ColumnReorderCommand cmd = new ColumnReorderCommand((Table) getHost().getModel(), (Column) child.getModel());
		if (after != null) {
			cmd.setAfterColumn((Column) after.getModel());
		}
		return cmd;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected EditPart getInsertionReference(Request request) {
		int y = ((ChangeBoundsRequest) request).getLocation().y;
		List<ColumnEditPart> columnEditParts = getHost().getChildren();
		ColumnEditPart afterColumn = null;
		for (Iterator<ColumnEditPart> iter = columnEditParts.iterator(); iter.hasNext();) {
			ColumnEditPart columnEditPart = iter.next();
			Rectangle r = columnEditPart.getFigure().getBounds();
			if (y < r.y) {
				return afterColumn;
			}
			afterColumn = columnEditPart;
		}
		return afterColumn;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (request.getNewObjectType() == Column.class && getHost().getModel() instanceof Table) {
			int index = getHost().getChildren().size();
			return new ColumnCreateCommand((Column) request.getNewObject(), (Table) getHost().getModel(), index);
		}
		return null;
	}

}
