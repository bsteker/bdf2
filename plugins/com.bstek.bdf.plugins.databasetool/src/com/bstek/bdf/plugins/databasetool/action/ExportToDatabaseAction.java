/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.action;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.exporter.ExportSchemaToDbBuilder;
import com.bstek.bdf.plugins.databasetool.wizard.ExportToDatabaseWizard;

public class ExportToDatabaseAction extends DatabaseAction {

	public ExportToDatabaseAction(IWorkbenchPart part) {
		super(part);
	}

	protected void init() {
		super.init();
		setText("Export Database");
		setId(ActionIdConstants.EXPORT_DATABASE_ACTION_ID);
		setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_EXPORT_DATABASE));
	}

	@Override
	public void execute() {
		final ExportToDatabaseWizard wizard = new ExportToDatabaseWizard(getSchema());
		WizardDialog dialog = new WizardDialog(getWorkbenchPart().getSite().getShell(), wizard);
		dialog.setHelpAvailable(false);
		if (dialog.open() == Dialog.OK) {
			final DbDriverMetaData dbDriverMetaData = wizard.getDbConectionWizardPage().getDbDriverMetaData();
			final boolean deleteTable = wizard.getExportToDatabaseWizardPage().isDeleleTable();
			IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						monitor.beginTask("正在同步到数据库....", IProgressMonitor.UNKNOWN);
						doRun(dbDriverMetaData, deleteTable, monitor);
						monitor.done();
					} catch (final Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			};
			try {
				new ProgressMonitorDialog(getWorkbenchPart().getSite().getShell()).run(true, true, runnableWithProgress);
				okMessage("同步到数据库成功!");
			} catch (InvocationTargetException e1) {
				errorMessage(e1.getTargetException().getMessage());
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				errorMessage(e1.getMessage());
				e1.printStackTrace();
			}
		}

	}

	private void doRun(final DbDriverMetaData dbDriverMetaData, boolean deleteTable, IProgressMonitor monitor) throws Exception {
		Connection conn = null;
		try {
			conn = DbJdbcUtils.getConnection(dbDriverMetaData.getDbType(), dbDriverMetaData.getUrl(), dbDriverMetaData.getUsername(),
					dbDriverMetaData.getPassword());
			ExportSchemaToDbBuilder builder = ExportSchemaToDbBuilder.getInstance(getDbDialect(), getSchema(), deleteTable);
			builder.execute(conn, monitor);
		} finally {
			DbJdbcUtils.closeConnection(conn);
		}
	}

}