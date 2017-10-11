/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.databasetool.model.Connection;

public class DbTableRelationDialog extends Dialog {
	private String dialogTitle = "设置外键约束";
	private Connection connection;
	private Button buttonOk;
	private Button buttonCancel;

	private Text constraintNameText;
	private Text pkColumnNameText;
	private Text fkColumnNameText;
	private Combo typeCombo;

	private String type;
	private String constraintName;

	public DbTableRelationDialog(Shell parentShell, Connection connection) {
		super(parentShell);
		this.connection = connection;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		createControl(container);
		return container;
	}

	private void createControl(Composite container) {
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 10;
		layout.marginTop = 10;
		layout.marginWidth = 10;
		this.constraintNameText = createFieldControl(container, "约束名称：");
		this.pkColumnNameText = createFieldControl(container, "主键名称：");
		this.fkColumnNameText = createFieldControl(container, "外键名称：");
		this.typeCombo = createTypeComboControl(container, "对应关系：");
		initControlData();
		addListeners();
	}

	private void initControlData() {
		constraintNameText.setText(connection.getConstraintName());
		constraintNameText.selectAll();
		constraintNameText.setFocus();
		pkColumnNameText.setText(connection.getPkColumn().getName());
		pkColumnNameText.setEnabled(false);
		fkColumnNameText.setText(connection.getFkColumn().getName());
		fkColumnNameText.setEnabled(false);
		typeCombo.setText(connection.getType());

		setConstraintName(constraintNameText.getText());
		setType(typeCombo.getText());
	}

	private void addListeners() {
		constraintNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setConstraintName(constraintNameText.getText());
			}
		});
		typeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setType(typeCombo.getText());
			}
		});

	}

	private Combo createTypeComboControl(Composite parent, String name) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(name);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		label.setLayoutData(gd);
		this.typeCombo = new Combo(parent, SWT.READ_ONLY);
		typeCombo.add(Connection.ONETOMANY);
		typeCombo.add(Connection.ONETOONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		this.typeCombo.setLayoutData(gd);
		return typeCombo;
	}

	private Text createFieldControl(Composite parent, String labelName) {
		return createFieldControl(parent, labelName, false);
	}

	private Text createFieldControl(Composite parent, String labelName, boolean password) {
		return createFieldControl(parent, labelName, password, 2);
	}

	private Text createFieldControl(Composite parent, String labelName, boolean password, int textHorizontalSpan) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(labelName);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		label.setLayoutData(gd);
		int stype = SWT.BORDER | SWT.SINGLE;
		if (password) {
			stype = stype | SWT.PASSWORD;
		}
		Text textControl = new Text(parent, stype);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = textHorizontalSpan;
		textControl.setLayoutData(gd);
		return textControl;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(getDialogTitle());
	}

	protected Point getInitialSize() {
		return new Point(450, 250);
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

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public String getType() {
		return type;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

}