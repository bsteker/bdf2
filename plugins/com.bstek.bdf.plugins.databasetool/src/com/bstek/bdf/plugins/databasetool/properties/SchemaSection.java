/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class SchemaSection extends AbstractSection {
	private CLabel dbTypeLabel;
	private Text dbTypeText;

	public void sectionChanged(Control control) {

	}

	private void createDbTypeText(Composite parent) {
		this.dbTypeText = getWidgetFactory().createText(parent, "");
		dbTypeText.setEditable(false);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(100, 0);
		this.dbTypeText.setLayoutData(data);
	}

	private void createDbTypeLabel(Composite parent) {
		this.dbTypeLabel = getWidgetFactory().createCLabel(parent, "数据库:");
		FormData data = new FormData();
		data.top = new FormAttachment(dbTypeText, 0, SWT.CENTER);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(dbTypeText, -ITabbedPropertyConstants.HSPACE);
		this.dbTypeLabel.setLayoutData(data);
	}

	public void createControls(Composite composite, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(composite, tabbedPropertySheetPage);
		Composite parent = getWidgetFactory().createFlatFormComposite(composite);
		createDbTypeText(parent);
		createDbTypeLabel(parent);
	}

	public void hookListeners() {
		listener.startListeningTo(dbTypeText);
	}

	public void unHookListeners() {
		listener.stopListeningTo(dbTypeText);
	}

	public void updateValues() {
		if (getModel() instanceof Schema) {
			Schema s = (Schema) getModel();
			dbTypeText.setText(s.getCurrentDbType());
		}
	}

	@Override
	public void dispose() {
		((BaseModel) getModel()).removePropertyChangeListener(this);
		super.dispose();
	}
}