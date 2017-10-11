/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class ColumnSection extends AbstractSection {
	private Button buttonPK;
	private Button buttonFK;
	private Button buttonNotNull;
	private Button buttonUnique;
	private Button buttonAutoIncrement;

	private CLabel typeLabel;
	private CLabel lengthLabel;
	private CLabel decimalLengthLabel;
	private CLabel defaultValueLabel;

	private CLabel nameLabel;
	private CLabel labelLabel;
	private CLabel commentLabel;

	private Text nameText;
	private Text labelText;
	private Text commentText;

	private CCombo typeCombo;
	private ColumnType columnType;
	private Text lengthText;
	private Text decimalLengthText;
	private Text defaultValueText;

	private Column column;

	private DbDialect dbDialect;

	public void sectionChanged(Control control) {
		String propertyId = null;
		Object newValue = null;
		if (getModel() instanceof Column) {
			if (control == nameText) {
				propertyId = Column.NAME;
				newValue = nameText.getText();
			} else if (control == labelText) {
				propertyId = Column.LABEL;
				newValue = labelText.getText();
			} else if (control == commentText) {
				propertyId = Column.COMMENT;
				newValue = commentText.getText();
			} else if (control == lengthText) {
				propertyId = Column.LENGTH;
				newValue = lengthText.getText();
			} else if (control == decimalLengthText) {
				propertyId = Column.DECIMALLENGTH;
				newValue = decimalLengthText.getText();
			} else if (control == defaultValueText) {
				propertyId = Column.DEFAULTVALUE;
				newValue = defaultValueText.getText();
			}
			fireChangPropertyEvent(propertyId, newValue);
		}
	}

	private void createButtons(Composite parent) {
		buttonPK = getWidgetFactory().createButton(parent, "主键", SWT.CHECK);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, 30);
		buttonPK.setLayoutData(data);

		buttonNotNull = getWidgetFactory().createButton(parent, "不能为空", SWT.CHECK);
		data = new FormData();
		data.top = new FormAttachment(buttonPK, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 80);
		buttonNotNull.setLayoutData(data);

		buttonUnique = getWidgetFactory().createButton(parent, "唯一", SWT.CHECK);
		data = new FormData();
		data.top = new FormAttachment(buttonPK, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 150);
		buttonUnique.setLayoutData(data);

		buttonFK = getWidgetFactory().createButton(parent, "外键", SWT.CHECK);
		data = new FormData();
		data.top = new FormAttachment(buttonPK, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 200);
		buttonFK.setLayoutData(data);
		buttonFK.setEnabled(false);

		buttonAutoIncrement = getWidgetFactory().createButton(parent, "自增", SWT.CHECK);
		data = new FormData();
		data.top = new FormAttachment(buttonPK, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 250);
		buttonAutoIncrement.setLayoutData(data);

	}

	private void createNameText(Composite parent) {
		this.nameText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(buttonPK, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.nameText.setLayoutData(data);
	}

	private void createNameLabel(Composite parent) {
		this.nameLabel = getWidgetFactory().createCLabel(parent, "列名:");
		FormData data = new FormData();
		data.top = new FormAttachment(nameText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nameText, -ITabbedPropertyConstants.HSPACE);
		this.nameLabel.setLayoutData(data);
	}

	private void createLabelText(Composite parent) {
		this.labelText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(nameText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.labelText.setLayoutData(data);
	}

	private void createLabelLabel(Composite parent) {
		this.labelLabel = getWidgetFactory().createCLabel(parent, "名称:");
		FormData data = new FormData();
		data.top = new FormAttachment(labelText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(labelText, -ITabbedPropertyConstants.HSPACE);
		this.labelLabel.setLayoutData(data);
	}

	private void createTypeCombo(Composite parent) {
		this.typeCombo = getWidgetFactory().createCCombo(parent, SWT.READ_ONLY);
		FormData data = new FormData();
		data.top = new FormAttachment(labelText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.typeCombo.setLayoutData(data);
	}

	private void createTypeLabel(Composite parent) {
		this.typeLabel = getWidgetFactory().createCLabel(parent, "类型:");
		FormData data = new FormData();
		data.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(typeCombo, -ITabbedPropertyConstants.HSPACE);
		this.typeLabel.setLayoutData(data);
	}

	private void createLengthText(Composite parent) {
		this.lengthText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(typeCombo, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.lengthText.setLayoutData(data);
	}

	private void createLengthLabel(Composite parent) {
		this.lengthLabel = getWidgetFactory().createCLabel(parent, "长度:");
		FormData data = new FormData();
		data.top = new FormAttachment(lengthText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(lengthText, -ITabbedPropertyConstants.HSPACE);
		this.lengthLabel.setLayoutData(data);
	}

	private void createDecimalLengthText(Composite parent) {
		this.decimalLengthText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(lengthText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.decimalLengthText.setLayoutData(data);
	}

	private void createDecimalLengthLabel(Composite parent) {
		this.decimalLengthLabel = getWidgetFactory().createCLabel(parent, "小数位:");
		FormData data = new FormData();
		data.top = new FormAttachment(decimalLengthText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(decimalLengthText, -ITabbedPropertyConstants.HSPACE);
		this.decimalLengthLabel.setLayoutData(data);
	}

	private void createDefaultValueText(Composite parent) {
		this.defaultValueText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(decimalLengthText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.defaultValueText.setLayoutData(data);

	}

	private void createDefaultValueLabel(Composite parent) {
		this.defaultValueLabel = getWidgetFactory().createCLabel(parent, "默认值:");
		FormData data = new FormData();
		data.top = new FormAttachment(defaultValueText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(defaultValueText, -ITabbedPropertyConstants.HSPACE);
		this.defaultValueLabel.setLayoutData(data);
	}

	private void createCommentText(Composite parent) {
		this.commentText = getWidgetFactory().createText(parent, "", SWT.MULTI | SWT.BORDER);
		FormData data = new FormData();
		data.top = new FormAttachment(defaultValueText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		data.height = 100;
		this.commentText.setLayoutData(data);

	}

	private void createCommentLabel(Composite parent) {
		this.commentLabel = getWidgetFactory().createCLabel(parent, "备注:");
		FormData data = new FormData();
		data.top = new FormAttachment(commentText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(commentText, -ITabbedPropertyConstants.HSPACE);
		this.commentLabel.setLayoutData(data);
	}

	public void createControls(Composite composite, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(composite, tabbedPropertySheetPage);
		Composite parent = getWidgetFactory().createFlatFormComposite(composite);

		createButtons(parent);

		createNameText(parent);
		createNameLabel(parent);

		createLabelText(parent);
		createLabelLabel(parent);

		createTypeCombo(parent);
		createTypeLabel(parent);

		createLengthText(parent);
		createLengthLabel(parent);

		createDecimalLengthText(parent);
		createDecimalLengthLabel(parent);

		createDefaultValueText(parent);
		createDefaultValueLabel(parent);

		createCommentText(parent);
		createCommentLabel(parent);

		hookListeners();
	}

	private class VerifyNumberListener implements VerifyListener {
		public void verifyText(VerifyEvent event) {
			/*if (event.text != null && event.text.length() > 0) {
				event.doit = Character.isDigit(event.text.charAt(0));
			}*/
			/*if (event.widget == lengthText && event.text.equals("0")) {
				event.doit = false;
			}*/
		}
	}

	private class ComboSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			columnType = DbDialectManager.getColumnType(dbDialect, typeCombo.getText());
			fireChangPropertyEvent(Column.TYPE, typeCombo.getText());
			if (columnType.isLength() && columnType.getDefaultLength() > 0) {
				String length = String.valueOf(columnType.getDefaultLength());
				fireChangPropertyEvent(Column.LENGTH, length);
			}
			if (!columnType.isLength()) {
				fireChangPropertyEvent(Column.LENGTH, "");
			}
			if (columnType.isDecimal() && columnType.getDefaultDecimal() > 0) {
				String decimal = String.valueOf(columnType.getDefaultDecimal());
				fireChangPropertyEvent(Column.DECIMALLENGTH, decimal);
			}
			if (!columnType.isDecimal()) {
				fireChangPropertyEvent(Column.DECIMALLENGTH, "");
			}
			boolean support = dbDialect.supportAutoIncrement(typeCombo.getText());
			if (support && column.isPk()) {
				buttonAutoIncrement.setEnabled(true);
			} else {
				buttonAutoIncrement.setSelection(false);
				buttonAutoIncrement.setEnabled(false);
			}
			fireChangPropertyEvent(Column.AUTOINCREMENT, buttonAutoIncrement.getSelection());

		}
	}

	private class ButtonSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			Widget widget = e.widget;
			String propertyId = null;
			Object newValue = null;
			fireChangPropertyEvent(Connection.TYPE, typeCombo.getText());
			if (widget == buttonPK) {
				propertyId = Column.PK;
				newValue = buttonPK.getSelection();
				if (buttonPK.getSelection()) {
					buttonNotNull.setSelection(true);
					buttonNotNull.setEnabled(false);
					column.setNotNull(true);
					boolean support = DbDialectManager.getCurrentDbDialect().supportAutoIncrement(typeCombo.getText());
					buttonAutoIncrement.setEnabled(support ? true : false);
				} else {
					buttonNotNull.setEnabled(true);
					buttonAutoIncrement.setEnabled(false);
					buttonAutoIncrement.setSelection(false);
					fireChangPropertyEvent(Column.AUTOINCREMENT, buttonAutoIncrement.getSelection());
				}
			} else if (widget == buttonFK) {
				propertyId = Column.FK;
				newValue = buttonFK.getSelection();
			} else if (widget == buttonNotNull) {
				propertyId = Column.NOTNULL;
				newValue = buttonNotNull.getSelection();
			} else if (widget == buttonUnique) {
				propertyId = Column.UNIQUE;
				newValue = buttonUnique.getSelection();
			} else if (widget == buttonAutoIncrement) {
				propertyId = Column.AUTOINCREMENT;
				newValue = buttonAutoIncrement.getSelection();
			}
			fireChangPropertyEvent(propertyId, newValue);
		}
	}

	private SelectionAdapter buttonSelectionAdapter = new ButtonSelectionAdapter();

	public void hookListeners() {
		listener.startListeningTo(nameText);
		listener.startListeningTo(labelText);
		listener.startListeningTo(commentText);
		typeCombo.addSelectionListener(selectionAdapter);

		listener.startListeningTo(lengthText);
		lengthText.addVerifyListener(new VerifyNumberListener());

		listener.startListeningTo(decimalLengthText);
		decimalLengthText.addVerifyListener(new VerifyNumberListener());

		listener.startListeningTo(defaultValueText);

		buttonPK.addSelectionListener(buttonSelectionAdapter);
		buttonFK.addSelectionListener(buttonSelectionAdapter);
		buttonNotNull.addSelectionListener(buttonSelectionAdapter);
		buttonUnique.addSelectionListener(buttonSelectionAdapter);
		buttonAutoIncrement.addSelectionListener(buttonSelectionAdapter);

	}

	public void unHookListeners() {
		listener.stopListeningTo(nameText);
		listener.stopListeningTo(labelText);
		listener.stopListeningTo(commentText);

		listener.stopListeningTo(lengthText);
		listener.stopListeningTo(decimalLengthText);
		listener.stopListeningTo(defaultValueText);

		buttonPK.removeSelectionListener(buttonSelectionAdapter);
		buttonFK.removeSelectionListener(buttonSelectionAdapter);
		buttonNotNull.removeSelectionListener(buttonSelectionAdapter);
		buttonUnique.removeSelectionListener(buttonSelectionAdapter);
		buttonAutoIncrement.removeSelectionListener(buttonSelectionAdapter);
	}

	private SelectionAdapter selectionAdapter = new ComboSelectionAdapter();

	public void updateValues() {
		if (getModel() instanceof Column) {
			column = (Column) getModel();
			nameText.setText(column.getName());
			labelText.setText(column.getLabel());
			commentText.setText(column.getComment());
			dbDialect = DbDialectManager.getCurrentDbDialect();
			List<ColumnType> columnTypes = dbDialect.getColumnTypes();
			if (typeCombo.getItems().length == 0) {
				for (ColumnType columnType : columnTypes) {
					typeCombo.add(columnType.getType());
				}
			}
			typeCombo.setText(column.getType());
			defaultValueText.setText((String) column.getDefaultValue());

			buttonPK.setSelection(column.isPk() ? true : false);
			buttonFK.setSelection(column.isFk() ? true : false);
			buttonNotNull.setSelection(column.isNotNull() ? true : false);
			buttonUnique.setSelection(column.isUnique() ? true : false);
			buttonNotNull.setEnabled(column.isPk() ? false : true);
			boolean support = dbDialect.supportAutoIncrement(column.getType());
			buttonAutoIncrement.setSelection(column.isAutoIncrement() ? true : false);
			buttonAutoIncrement.setEnabled(support && column.isPk() ? true : false);

			columnType = DbDialectManager.getColumnType(dbDialect, typeCombo.getText());
			if (columnType != null) {
				lengthText.setEnabled(columnType.isLength() ? true : false);
				decimalLengthText.setEnabled(columnType.isDecimal() ? true : false);
				lengthText.setText(columnType.isLength() ? column.getLength() : "");
				decimalLengthText.setText(columnType.isDecimal() ? column.getDecimalLength() : "");
			}

		}
	}

	@Override
	public void dispose() {
		((BaseModel) getModel()).removePropertyChangeListener(this);
		super.dispose();
	}

}
