/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.wizard.pages.DbConectionWizardPage;
import com.bstek.bdf.plugins.databasetool.wizard.pages.ExportToDatabaseWizardPage;

public class ExportToDatabaseWizard extends Wizard implements INewWizard {
	private Schema schema;

	private DbConectionWizardPage dbConectionWizardPage;
	private ExportToDatabaseWizardPage exportToDatabaseWizardPage;

	public ExportToDatabaseWizard(Schema schema) {
		setWindowTitle("导出同步到数据库");
		setNeedsProgressMonitor(true);
		this.schema = schema;
		dbConectionWizardPage = new DbConectionWizardPage("DbConectionWizardPage");
		exportToDatabaseWizardPage = new ExportToDatabaseWizardPage("ExportToDatabaseWizardPage");
	}

	public void addPages() {
		addPage(dbConectionWizardPage);
		addPage(exportToDatabaseWizardPage);
	}

	public boolean performFinish() {
		return true;
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public DbConectionWizardPage getDbConectionWizardPage() {
		return dbConectionWizardPage;
	}

	public ExportToDatabaseWizardPage getExportToDatabaseWizardPage() {
		return exportToDatabaseWizardPage;
	}

	public void setDbConectionWizardPage(DbConectionWizardPage dbConectionWizardPage) {
		this.dbConectionWizardPage = dbConectionWizardPage;
	}

	public void setExportToDatabaseWizardPage(ExportToDatabaseWizardPage exportToDatabaseWizardPage) {
		this.exportToDatabaseWizardPage = exportToDatabaseWizardPage;
	}

}
