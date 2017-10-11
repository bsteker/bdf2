/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.action;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.command.ConnectionModifyCommand;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.wizard.pages.dialog.DbTableRelationDialog;

public class TableRelationModifyAction extends SelectionAction {
	public TableRelationModifyAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	protected void init() {
		setId(ActionIdConstants.MODIFY_TABLE_RELATION_ACTION_ID);
		setText("Modify Relation");
		setEnabled(false);
		setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_EDITOR));

	}

	@Override
	public void run() {
		super.run();
		BaseModel baseModel = getSelectedModel();
		if (baseModel instanceof Connection) {
			Connection model = (Connection) baseModel;
			DbTableRelationDialog dialog = new DbTableRelationDialog(getWorkbenchPart().getSite().getShell(), model);
			if (dialog.open() == Dialog.OK) {
				String constraintName = dialog.getConstraintName();
				String type = dialog.getType();
				execute(new ConnectionModifyCommand(model, constraintName, type));
			}
		}

	}

	@Override
	protected boolean calculateEnabled() {
		BaseModel model = getSelectedModel();
		if (model != null && (model instanceof Connection)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	private BaseModel getSelectedModel() {
		List obj = getSelectedObjects();
		if (!obj.isEmpty() && obj.get(0) instanceof EditPart) {
			EditPart editPart = (EditPart) obj.get(0);
			return (BaseModel) editPart.getModel();
		}
		return null;
	}
}
