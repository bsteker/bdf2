/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import com.bstek.bdf.plugins.databasetool.dialect.DbType;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.persistence.PersistenceHelper;

public class DbToolCreationWizardPage extends WizardNewFileCreationPage {
	private static final String DEFAULT_EXTENSION = ".dbtool";
	private final IWorkbench workbench;
	private String currentDbType;

	public DbToolCreationWizardPage(IWorkbench workbench, IStructuredSelection selection) {
		super("DbToolCreationPage", selection);
		this.workbench = workbench;
		setTitle("Create a new " + DEFAULT_EXTENSION + " file");
		setDescription("Create a new " + DEFAULT_EXTENSION + " file");
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite composite = (Composite) getControl();
		Group group = new Group(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setText("设置");
		group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		Label label = new Label(group, SWT.NULL);
		label.setText("数据库方言：");
		final Combo combo = new Combo(group, SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gd);
		DbType[] values = DbType.values();
		for (DbType type : values) {
			combo.add(type.name());
		}
		combo.select(0);
		currentDbType = combo.getText();
		setFileName("NewFile" + DEFAULT_EXTENSION);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentDbType = combo.getText();
			}
		});
		setPageComplete(validatePage());
	}

	public boolean finish() {
		IFile newFile = createNewFile();
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		if (newFile != null && page != null) {
			try {
				IDE.openEditor(page, newFile, true);
			} catch (PartInitException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	protected InputStream getInitialContents() {
		Schema schema = new Schema();
		schema.setCurrentDbType(currentDbType);
		return PersistenceHelper.convertObjectToXml(schema);
	}

	private boolean validateFilename() {
		if (getFileName() != null && getFileName().endsWith(DEFAULT_EXTENSION)) {
			return true;
		}
		setErrorMessage("The 'file' name must end with " + DEFAULT_EXTENSION);
		return false;
	}

	protected boolean validatePage() {
		return super.validatePage() && validateFilename();
	}
}