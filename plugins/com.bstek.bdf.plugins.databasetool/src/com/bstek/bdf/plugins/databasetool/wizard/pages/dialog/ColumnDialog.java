/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages.dialog;

import java.util.List;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.model.Column;

public class ColumnDialog extends Dialog {
	public static final String DIALOG_MESSAGE = "设置列信息";
	public static final int TYPE_NEW = 0;
	public static final int TYPE_MODIFY = 1;
	private Column column;
	private int type;
	private DataBindingContext dataBindingContext;
	private Button buttonPK;
	private Button buttonFK;
	private Button buttonNotNull;
	private Button buttonUnique;
	private Button buttonAutoIncrement;
	private Label errorLabel;

	private Text columnNameText;
	private Text columnLabelText;
	private Text lengthText;
	private Text decimalText;
	private Text commentText;
	private Text defaultVaueText;
	private Combo typeCombo;
	private ColumnType columnType;

	private Button buttonOk;
	private Button buttonCancel;

	public ColumnDialog(Shell parentShell, Column column, int type) {
		super(parentShell);
		dataBindingContext = new DataBindingContext();
		this.column = column;
		this.type = type;
	}

	private void createBasicControl(Composite container) {
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 6;
		layout.verticalSpacing = 10;
		layout.marginTop = 10;
		layout.marginWidth = 10;
		createErrorLabel(container);
		createColumnInfoControl(container);
		final ISWTObservableValue observableValue = SWTObservables.observeText(errorLabel);
		errorLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		dataBindingContext.bindValue(observableValue, new AggregateValidationStatus(dataBindingContext.getBindings(),
				AggregateValidationStatus.MAX_SEVERITY), null, null);
		observableValue.addChangeListener(new IChangeListener() {
			public void handleChange(ChangeEvent event) {
				if (observableValue.getValue().equals("OK")) {
					errorLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					errorLabel.setText(DIALOG_MESSAGE);
					buttonOk.setEnabled(true);
				} else {
					errorLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					buttonOk.setEnabled(false);
				}
			}
		});

	}

	private void createErrorLabel(Composite container) {
		errorLabel = new Label(container, SWT.NONE);
		errorLabel.setText(DIALOG_MESSAGE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 6;
		errorLabel.setLayoutData(gridData);
	}

	private void createColumnInfoControl(Composite container) {
		GridData gd;
		Composite buttonComposite = (Composite) new Composite(container, SWT.NONE);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 6;
		buttonComposite.setLayoutData(gd);

		RowLayout layout = new RowLayout();
		layout.marginLeft = 10;
		layout.marginRight = 10;
		layout.marginTop = 10;
		layout.marginBottom = 10;
		layout.spacing = 10;
		buttonComposite.setLayout(layout);

		buttonPK = new Button(buttonComposite, SWT.CHECK);
		buttonPK.setText("主键");
		buttonPK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (buttonPK.getSelection()) {
					column.setNotNull(true);
					buttonNotNull.setEnabled(false);
					boolean support = DbDialectManager.getCurrentDbDialect().supportAutoIncrement(typeCombo.getText());
					buttonAutoIncrement.setEnabled(support ? true : false);
				} else {
					buttonNotNull.setEnabled(true);
					buttonAutoIncrement.setEnabled(false);
					buttonAutoIncrement.setSelection(false);
					column.setAutoIncrement(false);
				}
			}

		});

		buttonNotNull = new Button(buttonComposite, SWT.CHECK);
		buttonNotNull.setText("不能为空");

		buttonUnique = new Button(buttonComposite, SWT.CHECK);
		buttonUnique.setText("唯一");

		if (column.isPk()) {
			buttonNotNull.setEnabled(false);
		}
		buttonFK = new Button(buttonComposite, SWT.CHECK);
		buttonFK.setText("外键");
		buttonFK.setEnabled(false);

		buttonAutoIncrement = new Button(buttonComposite, SWT.CHECK);
		buttonAutoIncrement.setText("自增");
		boolean support = DbDialectManager.getCurrentDbDialect().supportAutoIncrement(column.getType());
		buttonAutoIncrement.setEnabled(support && column.isPk() ? true : false);
		buttonAutoIncrement.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (buttonAutoIncrement.getSelection()) {
					boolean support = DbDialectManager.getCurrentDbDialect().supportAutoIncrement(typeCombo.getText());
					if (!support) {
						buttonAutoIncrement.setSelection(false);
					}
				}
			}

		});
		bindWidgetValue(column, Column.AUTOINCREMENT, buttonAutoIncrement);

		Label columnNameLabel = new Label(container, SWT.NULL);
		columnNameLabel.setText("&列名:");
		columnNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		columnNameText.setFocus();
		columnNameText.addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event e) {
				columnNameText.selectAll();
				if (type == TYPE_MODIFY) {
					errorLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					errorLabel.setText(DIALOG_MESSAGE);
				}
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		columnNameText.setLayoutData(gd);

		Label columnLabel = new Label(container, SWT.NULL);
		columnLabel.setText("&名称:");
		columnLabelText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		columnLabelText.setLayoutData(gd);

		Label typeLabel = new Label(container, SWT.NULL);
		typeLabel.setText("&类型:");
		typeCombo = new Combo(container, SWT.READ_ONLY);
		List<ColumnType> columnTypes = DbDialectManager.getCurrentDbDialect().getColumnTypes();
		for (ColumnType columnType : columnTypes) {
			typeCombo.add(columnType.getType());
		}
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 150;
		typeCombo.setLayoutData(gd);

		Label lengthLabel = new Label(container, SWT.NULL);
		lengthLabel.setText("&长度:");
		lengthText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 50;
		lengthText.setLayoutData(gd);
		lengthText.addVerifyListener(new VerifyNumberListener());

		Label decimalLabel = new Label(container, SWT.NULL);
		decimalLabel.setText("&小数位:");
		decimalText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 50;
		decimalText.setLayoutData(gd);
		decimalText.addVerifyListener(new VerifyNumberListener());

		Label commentLabel = new Label(container, SWT.NULL);
		commentLabel.setText("&备注:");
		commentText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		GridData gridData = new GridData();
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 5;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		commentText.setLayoutData(gridData);

		Label defaultVaueLabel = new Label(container, SWT.NULL);
		defaultVaueLabel.setText("&默认值:");
		defaultVaueText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		defaultVaueText.setLayoutData(gd);

		bindControlValues();

		if (typeCombo.getText().length() == 0) {
			column.setType(DbDialectManager.getCurrentDbDialect().getDefaultColumnType());
		}
		columnType = DbDialectManager.getColumnType(DbDialectManager.getCurrentDbDialect(), typeCombo.getText());
		lengthText.setEnabled(columnType.isLength() ? true : false);
		decimalText.setEnabled(columnType.isDecimal() ? true : false);
		if (type == TYPE_NEW) {
			if (columnType.isLength() && columnType.getDefaultLength() > 0) {
				lengthText.setText(String.valueOf(columnType.getDefaultLength()));
			} else {
				lengthText.setText("");
			}
			if (columnType.isDecimal() && columnType.getDefaultDecimal() > 0) {
				decimalText.setText(String.valueOf(columnType.getDefaultDecimal()));
			} else {
				decimalText.setText("");
			}
		} else {
			if (!columnType.isLength()) {
				lengthText.setText("");
			}
			if (!columnType.isDecimal()) {
				decimalText.setText("");
			}
		}
		typeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columnType = DbDialectManager.getColumnType(DbDialectManager.getCurrentDbDialect(), typeCombo.getText());
				lengthText.setEnabled(columnType.isLength() ? true : false);
				decimalText.setEnabled(columnType.isDecimal() ? true : false);
				if (columnType.isLength() && columnType.getDefaultLength() > 0) {
					lengthText.setText(String.valueOf(columnType.getDefaultLength()));
				} else {
					lengthText.setText("");
				}
				if (columnType.isDecimal() && columnType.getDefaultDecimal() > 0) {
					decimalText.setText(String.valueOf(columnType.getDefaultDecimal()));
				} else {
					decimalText.setText("");
				}
				boolean support = DbDialectManager.getCurrentDbDialect().supportAutoIncrement(typeCombo.getText());
				if (support && column.isPk()) {
					buttonAutoIncrement.setEnabled(true);
				} else {
					buttonAutoIncrement.setSelection(false);
					column.setAutoIncrement(false);
					buttonAutoIncrement.setEnabled(false);
				}
			}

		});

	}

	private void bindControlValues() {
		bindWidgetValue(column, Column.PK, buttonPK);
		bindWidgetValue(column, Column.NOTNULL, buttonNotNull);
		bindWidgetValue(column, Column.UNIQUE, buttonUnique);
		bindWidgetValue(column, Column.FK, buttonFK);

		UpdateValueStrategy update = new UpdateValueStrategy();
		update.setAfterConvertValidator(new FieldColumnNameValidator());
		Binding bindValue = bindWidgetValue(column, Column.NAME, columnNameText, update, null);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		bindWidgetValue(column, Column.LABEL, columnLabelText);

		update = new UpdateValueStrategy();
		update.setAfterConvertValidator(new FieldColumnTypeValidator());
		bindValue = bindWidgetValue(column, Column.TYPE, typeCombo, update, null);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		bindWidgetValue(column, Column.LENGTH, lengthText);
		bindWidgetValue(column, Column.DECIMALLENGTH, decimalText);
		bindWidgetValue(column, Column.COMMENT, commentText);
		bindWidgetValue(column, Column.DEFAULTVALUE, defaultVaueText);
	}

	private Binding bindWidgetValue(Object obj, String property, Object objWidget, UpdateValueStrategy targetToModel,
			UpdateValueStrategy modelToTarget) {
		IObservableValue model = BeanProperties.value(obj.getClass(), property).observe(obj);
		IObservableValue widget = null;
		if (objWidget instanceof Text) {
			widget = WidgetProperties.text(SWT.Modify).observe(objWidget);
		} else if (objWidget instanceof Button || objWidget instanceof Combo) {
			widget = WidgetProperties.selection().observe(objWidget);
		}
		return dataBindingContext.bindValue(widget, model, targetToModel, modelToTarget);
	}

	private Binding bindWidgetValue(Object obj, String property, Object objWidget) {
		return bindWidgetValue(obj, property, objWidget, null, null);
	}

	protected int getShellStyle() {
		return super.getShellStyle()|SWT.RESIZE|SWT.MAX;
	}

	protected Point getInitialSize() {
		return new Point(500, 400);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		createBasicControl(container);
		return container;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("列信息");
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
		if (type == TYPE_NEW) {
			buttonOk.setEnabled(false);
		}
	}

	private class VerifyNumberListener implements VerifyListener {
		public void verifyText(VerifyEvent event) {
			/*if (event.text != null && event.text.length() > 0) {
				event.doit = Character.isDigit(event.text.charAt(0));
			}*/

		}
	}

	private class FieldColumnNameValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			if (value instanceof String) {
				String s = (String) value;
				if (s.length() == 0) {
					return ValidationStatus.error("列名不能为空！");
				} else if (s.length() > 32) {
					return ValidationStatus.error("列名长度不能超过32位！");
				} else {
					return Status.OK_STATUS;
				}
			} else {
				throw new RuntimeException("not supposed!");
			}
		}

	}

	private class FieldColumnTypeValidator implements IValidator {
		@Override
		public IStatus validate(Object value) {
			if (value instanceof String) {
				String s = (String) value;
				if (s.length() == 0) {
					return ValidationStatus.error("类型不能为空！");
				} else {
					return Status.OK_STATUS;
				}
			} else {
				throw new RuntimeException("not supposed!");
			}
		}

	}

}