/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class ConnectionSection extends AbstractSection {

	private CLabel constraintNameLabel;
	private Text constraintNameText;

	private CLabel typeLabel;
	private CCombo typeCombo;

	private CLabel pkColumnNameLabel;
	private Text pkColumnNameText;

	private CLabel fkColumnNameLabel;
	private Text fkColumnNameText;

	public void sectionChanged(Control control) {
		String propertyId = null;
		Object newValue = null;
		if (getModel() instanceof Connection) {
			if (control == constraintNameText) {
				propertyId = Connection.CONSTRAINTNAME;
				newValue = constraintNameText.getText();
			}
			fireChangPropertyEvent(propertyId, newValue);
		}
	}

	private void createConstraintNameText(Composite parent) {
		this.constraintNameText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.constraintNameText.setLayoutData(data);
	}

	private void createConstraintNameLabel(Composite parent) {
		this.constraintNameLabel = getWidgetFactory().createCLabel(parent, "约束名称:");
		FormData data = new FormData();
		data.top = new FormAttachment(constraintNameText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(constraintNameText, -ITabbedPropertyConstants.HSPACE);
		this.constraintNameLabel.setLayoutData(data);
	}

	private void createPkColumnNameText(Composite parent) {
		this.pkColumnNameText = getWidgetFactory().createText(parent, "");
		this.pkColumnNameText.setEnabled(false);
		FormData data = new FormData();
		data.top = new FormAttachment(constraintNameText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.pkColumnNameText.setLayoutData(data);
	}

	private void createPkColumnNameLabel(Composite parent) {
		this.pkColumnNameLabel = getWidgetFactory().createCLabel(parent, "主键名称:");
		FormData data = new FormData();
		data.top = new FormAttachment(pkColumnNameText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(pkColumnNameText, -ITabbedPropertyConstants.HSPACE);
		this.pkColumnNameLabel.setLayoutData(data);
	}

	private void createFkColumnNameText(Composite parent) {
		this.fkColumnNameText = getWidgetFactory().createText(parent, "");
		this.fkColumnNameText.setEnabled(false);
		FormData data = new FormData();
		data.top = new FormAttachment(pkColumnNameText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.fkColumnNameText.setLayoutData(data);
	}

	private void createFkColumnNameLabel(Composite parent) {
		this.fkColumnNameLabel = getWidgetFactory().createCLabel(parent, "外键名称:");
		FormData data = new FormData();
		data.top = new FormAttachment(fkColumnNameText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(fkColumnNameText, -ITabbedPropertyConstants.HSPACE);
		this.fkColumnNameLabel.setLayoutData(data);
	}

	private void createTypeCombo(Composite parent) {
		this.typeCombo = getWidgetFactory().createCCombo(parent, SWT.READ_ONLY);
		typeCombo.add(Connection.ONETOMANY);
		typeCombo.add(Connection.ONETOONE);
		FormData data = new FormData();
		data.top = new FormAttachment(fkColumnNameText, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.typeCombo.setLayoutData(data);
	}

	private void createTypeLabel(Composite parent) {
		this.typeLabel = getWidgetFactory().createCLabel(parent, "对应关系:");
		FormData data = new FormData();
		data.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(typeCombo, -ITabbedPropertyConstants.HSPACE);
		this.typeLabel.setLayoutData(data);
	}

	public void createControls(Composite composite, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(composite, tabbedPropertySheetPage);
		Composite parent = getWidgetFactory().createFlatFormComposite(composite);
		createConstraintNameText(parent);
		createConstraintNameLabel(parent);
		createPkColumnNameText(parent);
		createPkColumnNameLabel(parent);
		createFkColumnNameText(parent);
		createFkColumnNameLabel(parent);
		createTypeCombo(parent);
		createTypeLabel(parent);
		hookListeners();
	}

	private class ComboSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			fireChangPropertyEvent(Connection.TYPE, typeCombo.getText());
		}
	}

	private SelectionAdapter selectionAdapter = new ComboSelectionAdapter();

	public void hookListeners() {
		listener.startListeningTo(constraintNameText);
		typeCombo.addSelectionListener(selectionAdapter);
	}

	public void unHookListeners() {
		listener.stopListeningTo(constraintNameText);
	}

	public void updateValues() {
		if (getModel() instanceof Connection) {
			Connection connection = (Connection) getModel();
			constraintNameText.setText(connection.getConstraintName());
			pkColumnNameText.setText(connection.getPkColumn().getName());
			fkColumnNameText.setText(connection.getFkColumn().getName());
			typeCombo.setText(connection.getType());
		}
	}

	@Override
	public void dispose() {
		((BaseModel) getModel()).removePropertyChangeListener(this);
		super.dispose();
	}
}