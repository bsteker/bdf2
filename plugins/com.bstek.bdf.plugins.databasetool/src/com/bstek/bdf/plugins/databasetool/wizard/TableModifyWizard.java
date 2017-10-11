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

import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.wizard.pages.TableModifyWizardPage;

public class TableModifyWizard extends Wizard implements INewWizard {

	private TableModifyWizardPage page;

	public TableModifyWizard(Table table) {
		setWindowTitle("表信息");
		setNeedsProgressMonitor(true);
		page = new TableModifyWizardPage("TableModifyWizardPage", table);
	}

	public void addPages() {
		addPage(page);
	}

	public boolean performFinish() {
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	public TableModifyWizardPage getPage() {
		return page;
	}

	public void setPage(TableModifyWizardPage page) {
		this.page = page;
	}

}
