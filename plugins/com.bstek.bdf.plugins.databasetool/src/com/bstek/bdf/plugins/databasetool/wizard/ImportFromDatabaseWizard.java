/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.wizard.pages.DbConectionWizardPage;
import com.bstek.bdf.plugins.databasetool.wizard.pages.ImportFromDatabaseWizardPage;

public class ImportFromDatabaseWizard extends Wizard implements INewWizard {
	private Schema schema;
	private DbConectionWizardPage dbConectionWizardPage;
	private ImportFromDatabaseWizardPage importFromDatabaseWizardPage;

	public ImportFromDatabaseWizard(Schema schema) {
		setWindowTitle("从数据库导入模型");
		setNeedsProgressMonitor(true);
		this.schema = schema;
		dbConectionWizardPage = new DbConectionWizardPage("DbConectionWizardPage");
		dbConectionWizardPage.setPageComplete(false);
		importFromDatabaseWizardPage = new ImportFromDatabaseWizardPage("ImportFromDatabaseWizardPage");
	}

	public void addPages() {
		addPage(dbConectionWizardPage);
		addPage(importFromDatabaseWizardPage);
	}

	public boolean performFinish() {
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		if (currentPage instanceof ImportFromDatabaseWizardPage) {
			return true;
		}
		return false;
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

	public ImportFromDatabaseWizardPage getImportFromDatabaseWizardPage() {
		return importFromDatabaseWizardPage;
	}

	public void setDbConectionWizardPage(DbConectionWizardPage dbConectionWizardPage) {
		this.dbConectionWizardPage = dbConectionWizardPage;
	}

	public void setImportFromDatabaseWizardPage(ImportFromDatabaseWizardPage importFromDatabaseWizardPage) {
		this.importFromDatabaseWizardPage = importFromDatabaseWizardPage;
	}

}
