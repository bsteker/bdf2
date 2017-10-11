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

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.exporter.ExportConstants;
import com.bstek.bdf.plugins.databasetool.wizard.pages.ExportToJavaBeanWizardPage;

public class ExportToJavaBeanWizard extends Wizard implements INewWizard {
	private String packageName;
	private String savePath;
	private boolean doradoPropertyDef;
	private boolean hiberanteAnnotation;

	private ExportToJavaBeanWizardPage page;

	public ExportToJavaBeanWizard() {
		setWindowTitle("导出javabean文件");
		setNeedsProgressMonitor(true);
		page = new ExportToJavaBeanWizardPage("ExportJavaBeanWizardPage");
	}

	public void addPages() {
		addPage(page);
	}

	public boolean performFinish() {
		setPackageName(page.getPackageText().getText());
		setDoradoPropertyDef(page.getButtonPropertyDef().getSelection());
		setHiberanteAnnotation(page.getButtonHibernateAnotation().getSelection());
		Activator.getDefault().getPreferenceStore().setValue(ExportConstants.DORADO_PROPERTY_DEF, doradoPropertyDef);
		Activator.getDefault().getPreferenceStore().setValue(ExportConstants.HIBERNATE_ANNOTATION, hiberanteAnnotation);
		return true;
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		return false;
	}

	public ExportToJavaBeanWizardPage getPage() {
		return page;
	}

	public void setPage(ExportToJavaBeanWizardPage page) {
		this.page = page;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public boolean isDoradoPropertyDef() {
		return doradoPropertyDef;
	}

	public boolean isHiberanteAnnotation() {
		return hiberanteAnnotation;
	}

	public void setDoradoPropertyDef(boolean doradoPropertyDef) {
		this.doradoPropertyDef = doradoPropertyDef;
	}

	public void setHiberanteAnnotation(boolean hiberanteAnnotation) {
		this.hiberanteAnnotation = hiberanteAnnotation;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
