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

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.wizard.pages.ColumnModifyWizardPage;

public class ColumnModifyWizard extends Wizard implements INewWizard {

	private ColumnModifyWizardPage page;

	public ColumnModifyWizard(Column column ) {
		setWindowTitle("修改列信息");
		setNeedsProgressMonitor(false);
		page = new ColumnModifyWizardPage("ColumnModifyWizard",column);
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

}
