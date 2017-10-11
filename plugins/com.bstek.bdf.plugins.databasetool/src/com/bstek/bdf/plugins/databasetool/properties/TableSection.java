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

import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class TableSection extends AbstractSection {
	private CLabel nameLabel;
	private CLabel labelLabel;
	private CLabel commentLabel;
	private Text nameText;
	private Text labelText;
	private Text commentText;

	public void sectionChanged(Control control) {
		String propertyId = null;
		Object newValue = null;
		if (getModel() instanceof Table) {
			if (control == nameText) {
				propertyId = Table.NAME;
				newValue = nameText.getText();
			} else if (control == labelText) {
				propertyId = Table.LABEL;
				newValue = labelText.getText();
			} else if (control == commentText) {
				propertyId = Table.COMMENT;
				newValue = commentText.getText();
			}
			fireChangPropertyEvent(propertyId, newValue);
		}
	}

	private void createNameText(Composite parent) {
		this.nameText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 5);
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
		this.labelText = new Text(parent, SWT.SINGLE | SWT.BORDER);
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

	private void createCommentText(Composite parent) {
		this.commentText = new Text(parent, SWT.MULTI | SWT.BORDER);
		FormData data = new FormData();
		data.top = new FormAttachment(labelText, 5);
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
		createNameText(parent);
		createNameLabel(parent);

		createLabelText(parent);
		createLabelLabel(parent);

		createCommentText(parent);
		createCommentLabel(parent);
		hookListeners();
	}

	public void hookListeners() {
		listener.startListeningTo(nameText);
		listener.startListeningTo(labelText);
		listener.startListeningTo(commentText);
	}

	public void unHookListeners() {
		listener.stopListeningTo(nameText);
		listener.stopListeningTo(labelText);
		listener.stopListeningTo(commentText);
	}

	public void updateValues() {
		if (getModel() instanceof Table) {
			Table t = (Table) getModel();
			nameText.setText(t.getName());
			labelText.setText(t.getLabel());
			commentText.setText(t.getComment());
		}
	}

	@Override
	public void dispose() {
		((BaseModel) getModel()).removePropertyChangeListener(this);
		super.dispose();
	}
}