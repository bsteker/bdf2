/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.action;

import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.DbToolGefEditor;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.utils.ClassUtils;
import com.bstek.bdf.plugins.databasetool.wizard.pages.dialog.DbDriverLocationDialog;

public abstract class DatabaseAction extends BaseSelectionAction {
	private Schema schema;
	private DbToolGefEditor editor;
	private DbDialect dbDialect;

	public DatabaseAction(IWorkbenchPart part) {
		super(part);
	}

	protected boolean calculateEnabled() {
		if (getWorkbenchPart() instanceof GraphicalEditor) {
			return true;
		}
		return false;
	}

	public void run() {
		int state = validateDb();
		if (getSchema() == null) {
			return;
		}
		if (state == Dialog.OK) {
			execute();
		}
	}

	public abstract void execute();

	private int validateDb() {
		editor = (DbToolGefEditor) getWorkbenchPart().getAdapter(GraphicalEditor.class);
		schema = editor.getModel();
		dbDialect = DbDialectManager.getDbDialect(schema.getCurrentDbType());
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String driverLocation = store.getString(schema.getCurrentDbType());
		if (driverLocation.trim().length() == 0) {
			return openDbDriverLocationDialog(schema.getCurrentDbType(), dbDialect.getJdbcDriverName(), driverLocation);
		} else {
			return attemptTestJdbcDriver(schema.getCurrentDbType(), dbDialect.getJdbcDriverName(), driverLocation);
		}
	}

	private int openDbDriverLocationDialog(String dbType, String driverName, String driverLocation) {
		DbDriverMetaData dbDriverMetaData = new DbDriverMetaData();
		dbDriverMetaData.setDbNmae(dbType);
		dbDriverMetaData.setDriverClassName(driverName);
		dbDriverMetaData.setDriverLocation(driverLocation);
		DbDriverLocationDialog dialog = new DbDriverLocationDialog("请先配置数据库驱动", getWorkbenchPart().getSite().getShell(), dbDriverMetaData);
		if (dialog.open() == Dialog.OK) {
			dbDriverMetaData.setDriverLocation(dialog.getDriverLocation());
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			store.setValue(dbType, dialog.getDriverLocation());
			return attemptTestJdbcDriver(dbType, driverName, dbDriverMetaData.getDriverLocation());
		} else {
			return Dialog.CANCEL;
		}
	}

	private int attemptTestJdbcDriver(String dbType, String driverName, String driverLocation) {
		try {
			ClassUtils.loadJdbcDriverClass(driverName, driverLocation);
		} catch (Exception e) {
			errorMessage("Jdbc驱动加载失败，请重新设置驱动位置!\n" + e.getMessage());
			return openDbDriverLocationDialog(dbType, driverName, driverLocation);
		}
		return Dialog.OK;
	}

	public Schema getSchema() {
		return schema;
	}

	public DbDialect getDbDialect() {
		return dbDialect;
	}

	public DbToolGefEditor getEditor() {
		return editor;
	}

}