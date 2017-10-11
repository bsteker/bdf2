/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages.dialog;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;

public class DbDriverLocationDialog extends Dialog {
	private String dialogTitle = "设置数据库驱动";
	private DbDriverMetaData dbDriverMetaData;

	private Text dbNameText;
	private Text driverClassNameText;
	private Text driverLocationText;

	private Button buttonBrowerDriverFile;

	private Button buttonOk;
	private Button buttonCancel;

	private String driverLocation;

	public DbDriverLocationDialog(Shell parentShell, DbDriverMetaData dbDriverMetaData) {
		super(parentShell);
		this.dbDriverMetaData = dbDriverMetaData;
	}

	public DbDriverLocationDialog(String dialogTitle, Shell parentShell, DbDriverMetaData dbDriverMetaData) {
		super(parentShell);
		this.dbDriverMetaData = dbDriverMetaData;
		this.dialogTitle = dialogTitle;
	}

	private void createControl(Composite container) {
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 10;
		layout.marginTop = 10;
		layout.marginWidth = 10;

		GridData gd = null;
		Label dbNameLabel = new Label(container, SWT.LEFT);
		dbNameLabel.setText("数据库名称:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		dbNameLabel.setLayoutData(gd);

		dbNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		dbNameText.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		dbNameText.setLayoutData(gd);

		Label driverClassNameLabel = new Label(container, SWT.LEFT);
		driverClassNameLabel.setText("Jdbc驱动类:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		driverClassNameLabel.setLayoutData(gd);

		driverClassNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		driverClassNameText.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		driverClassNameText.setLayoutData(gd);

		Label driverLocationLabel = new Label(container, SWT.LEFT);
		driverLocationLabel.setText("Jdbc驱动位置:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		driverLocationLabel.setLayoutData(gd);

		driverLocationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		driverLocationText.setEditable(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		driverLocationText.setLayoutData(gd);

		buttonBrowerDriverFile = new Button(container, SWT.PUSH);
		buttonBrowerDriverFile.setText("浏览...");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.widthHint = 80;
		buttonBrowerDriverFile.setLayoutData(gd);
		buttonBrowerDriverFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.MULTI);
				dialog.setFilterExtensions(new String[] { "*.jar" });
				if (dialog.open() != null) {
					String[] fileNames = dialog.getFileNames();
					StringBuffer sb = new StringBuffer();
					if (fileNames.length > 0) {
						int i = 1;
						for (String fileName : fileNames) {
							File file = new File(dialog.getFilterPath(), fileName);
							sb.append(file.getAbsolutePath());
							if (i != fileNames.length) {
								sb.append(";");
							}
							i++;
						}
					}
					driverLocationText.setText(sb.toString());
					setDriverLocation(sb.toString());
				}
			}
		});

		initTextControlData();
	}

	private void initTextControlData() {
		if (dbDriverMetaData != null) {
			dbNameText.setText(dbDriverMetaData.getDbType());
			driverClassNameText.setText(dbDriverMetaData.getDriverClassName());
			driverLocationText.setText(dbDriverMetaData.getDriverLocation() == null ? "" : dbDriverMetaData.getDriverLocation());
		}
	}

	protected Point getInitialSize() {
		return new Point(500, 200);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		createControl(container);
		return container;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(getDialogTitle());
	}

	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		buttonOk = getButton(IDialogConstants.OK_ID);
		buttonOk.setText("确定");
		buttonCancel = getButton(IDialogConstants.CANCEL_ID);
		buttonCancel.setText("取消");
	}

	public String getDriverLocation() {
		if(driverLocation==null){
			return "";
		}
		return driverLocation;
	}

	public void setDriverLocation(String driverLocation) {
		this.driverLocation = driverLocation;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

}