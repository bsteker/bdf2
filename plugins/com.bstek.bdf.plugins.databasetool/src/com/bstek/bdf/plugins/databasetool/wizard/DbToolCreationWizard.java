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

import com.bstek.bdf.plugins.databasetool.wizard.pages.DbToolCreationWizardPage;

public class DbToolCreationWizard extends Wizard implements INewWizard {
	private DbToolCreationWizardPage page1;

	public void addPages() {
		addPage(page1);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page1 = new DbToolCreationWizardPage(workbench, selection);
	}

	public boolean performFinish() {
		return page1.finish();
	}

}
