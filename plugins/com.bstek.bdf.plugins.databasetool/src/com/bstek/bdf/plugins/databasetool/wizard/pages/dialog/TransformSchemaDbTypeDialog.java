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

import com.bstek.bdf.plugins.databasetool.dialect.DbType;
import com.bstek.bdf.plugins.databasetool.model.Schema;

public class TransformSchemaDbTypeDialog extends Dialog {
	private String dialogTitle = "转换数据库模型";
	private Schema schema;
	private String destDbType;
	private Button buttonOk;
	private Button buttonCancel;
	private Combo dbCombo;
	private Text sourceDbText;

	public TransformSchemaDbTypeDialog(Shell parentShell, Schema schema) {
		super(parentShell);
		this.schema = schema;
	}

	private void createControl(Composite container) {
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 10;
		layout.marginTop = 10;
		layout.marginWidth = 10;

		GridData gd = null;
		Label sourceDbNameLabel = new Label(container, SWT.LEFT);
		sourceDbNameLabel.setText("当前数据库:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		sourceDbNameLabel.setLayoutData(gd);

		sourceDbText = new Text(container, SWT.BORDER | SWT.SINGLE);
		sourceDbText.setEnabled(false);
		sourceDbText.setText(schema.getCurrentDbType());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		sourceDbText.setLayoutData(gd);

		Label dbNmaeLabel = new Label(container, SWT.LEFT);
		dbNmaeLabel.setText("目标数据库:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		dbNmaeLabel.setLayoutData(gd);

		dbCombo = new Combo(container, SWT.READ_ONLY);
		DbType[] values = DbType.values();
		for (DbType type : values) {
			if (!type.name().toLowerCase().equals(schema.getCurrentDbType().toLowerCase())) {
				dbCombo.add(type.name());
			}
		}
		dbCombo.select(0);
		destDbType = dbCombo.getText();
		dbCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				destDbType = dbCombo.getText();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		dbCombo.setLayoutData(gd);

	}

	protected Point getInitialSize() {
		return new Point(400, 180);
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

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public String getDestDbType() {
		return destDbType;
	}

	public void setDestDbType(String destDbType) {
		this.destDbType = destDbType;
	}

}