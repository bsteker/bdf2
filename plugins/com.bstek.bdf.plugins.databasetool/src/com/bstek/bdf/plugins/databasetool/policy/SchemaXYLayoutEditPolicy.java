/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.bstek.bdf.plugins.databasetool.command.TableCreateCommand;
import com.bstek.bdf.plugins.databasetool.command.TableLayoutCommand;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.part.TableEditPart;

public class SchemaXYLayoutEditPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child instanceof TableEditPart && constraint instanceof Rectangle) {
			return new TableLayoutCommand((Table) child.getModel(), request, (Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child, constraint);
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (request.getNewObjectType() == Table.class && getHost().getModel() instanceof Schema) {
			return new TableCreateCommand((Table) request.getNewObject(), (Schema) getHost().getModel(), request.getLocation());
		}
		return null;
	}
}
