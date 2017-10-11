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
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.command.TableImportCommand;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.importer.ImportDbToSchemaBuilder;
import com.bstek.bdf.plugins.databasetool.wizard.ImportFromDatabaseWizard;
import com.bstek.bdf.plugins.databasetool.wizard.pages.ImportFromDatabaseWizardPage;

public class ImportFromDatabaseAction extends DatabaseAction {

	public ImportFromDatabaseAction(IWorkbenchPart part) {
		super(part);
	}

	protected void init() {
		super.init();
		setText("Import");
		setId(ActionIdConstants.IMPORT_DATABASE_ACTION_ID);
		setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_IMPORT_DATABASE));
	}

	@Override
	public void execute() {
		DbDialect dbDialect = getDbDialect();
		ImportFromDatabaseWizard wizard = new ImportFromDatabaseWizard(getSchema());
		WizardDialog dialog = new WizardDialog(getWorkbenchPart().getSite().getShell(), wizard);
		dialog.setHelpAvailable(false);
		if (dialog.open() == Dialog.OK) {
			ImportFromDatabaseWizardPage page = wizard.getImportFromDatabaseWizardPage();
			final List<String> checkedTables = page.getCheckedTables();
			final DbDriverMetaData dbDriverMetaData = page.getDbDriverMetaData();
			final ImportDbToSchemaBuilder importDbToSchemaBuilder = ImportDbToSchemaBuilder.getInstance(dbDialect);
			IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					Connection conn = null;
					try {
						monitor.beginTask("正在导入数据库模型...", checkedTables.size() * 2);
						conn = DbJdbcUtils.getConnection(dbDriverMetaData.getDbType(), dbDriverMetaData.getUrl(), dbDriverMetaData.getUsername(),
								dbDriverMetaData.getPassword());
						importDbToSchemaBuilder.builder(checkedTables, conn, monitor);
						monitor.done();
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
						
					} finally {
						DbJdbcUtils.closeConnection(conn);
					}

				}
			};

			try {
				new ProgressMonitorDialog(getWorkbenchPart().getSite().getShell()).run(true, true, runnableWithProgress);
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						execute(new TableImportCommand(getSchema(), importDbToSchemaBuilder.getBuilderTableList()));
					}
				});
			} catch (InvocationTargetException e1) {
				errorMessage(e1.getTargetException().getMessage());
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				errorMessage(e1.getMessage());
				e1.printStackTrace();
			}

		}

	}

}