/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.command.ColumnModifyCommand;
import com.bstek.bdf.plugins.databasetool.command.TableModifyCommand;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.wizard.TableModifyWizard;
import com.bstek.bdf.plugins.databasetool.wizard.pages.dialog.ColumnDialog;

public class ModelModifyAction extends SelectionAction {
	public ModelModifyAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	protected void init() {
		setId(ActionIdConstants.MODIFY_ACTION_ID);
		setText("Modify");
		setEnabled(false);
		setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_EDITOR));

	}

	@Override
	public void run() {
		super.run();
		BaseModel baseModel = getSelectedModel();
		if (baseModel instanceof Table) {
			Table model = (Table) baseModel;
			Table tablePage = getPropertyCopyNewTable(model);
			TableModifyWizard wizard = new TableModifyWizard(tablePage);
			WizardDialog dialog = new WizardDialog(getWorkbenchPart().getSite().getShell(), wizard);
			dialog.setPageSize(650, 500);
			dialog.setHelpAvailable(false);
			if (dialog.open() == Dialog.OK) {
				tablePage = wizard.getPage().getTable();
				Request request = new Request(RequestConstants.TABLE_MODIFY);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(TableModifyCommand.TEMP_DATA, tablePage);
				request.setExtendedData(map);
				Command command = ((EditPart) getSelectedObjects().get(0)).getCommand(request);
				execute(command);
			}
		} else if (baseModel instanceof Column) {
			Column model = (Column) baseModel;
			Column data = model.getPropertyCopyNewColumn();
			ColumnDialog dialog = new ColumnDialog(getWorkbenchPart().getSite().getShell(), data, ColumnDialog.TYPE_MODIFY);
			if (dialog.open() == Dialog.OK) {
				Request request = new Request(RequestConstants.COLUMN_MODIFY);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(ColumnModifyCommand.TEMP_DATA, data);
				request.setExtendedData(map);
				Command command = ((EditPart) getSelectedObjects().get(0)).getCommand(request);
				execute(command);
			}
		}

	}

	@Override
	protected boolean calculateEnabled() {
		BaseModel model = getSelectedModel();
		if (model != null && (model instanceof Table || model instanceof Column)) {
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

	private Table getPropertyCopyNewTable(Table model) {
		Table tablePage = model.getTableCopy();
		Column pageColumn = null;
		List<Column> columns = new ArrayList<Column>();
		try {
			for (Column modelColumn : model.getColumns()) {
				pageColumn = modelColumn.getPropertyCopyNewColumn();
				pageColumn.setCanDelete(calculateCanDelete(model, modelColumn));
				pageColumn.setTable(tablePage);
				columns.add(pageColumn);
			}
			tablePage.setColumns(columns);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tablePage;
	}

	private boolean calculateCanDelete(Table model, Column modelColumn) {
		List<Connection> cons = model.getOutConnections();
		for (Connection c : cons) {
			if (c.getPkColumn() != null && c.getPkColumn().equals(modelColumn)) {
				return false;
			}
		}
		return true;
	}

}
