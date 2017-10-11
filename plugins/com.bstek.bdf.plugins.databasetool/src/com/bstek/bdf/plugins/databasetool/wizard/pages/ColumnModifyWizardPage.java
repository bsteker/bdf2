/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.databasetool.model.Column;

public class ColumnModifyWizardPage extends WizardPage {
	private Column column;
	private DataBindingContext dataBindingContext;

	public ColumnModifyWizardPage(String pageName, Column column) {
		super(pageName);
		setTitle("修改列信息");
		setDescription("修改列信息");
		dataBindingContext = new DataBindingContext();
		this.column = column;
	}

	@Override
	public void createControl(Composite parent) {
		final TabFolder tabFolder = new TabFolder(parent, SWT.BORDER);
		String[] tabIndex = new String[] { "基本属性", "描述", "其他" };
		for (int i = 0; i < tabIndex.length; i++) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
			tabItem.setText(tabIndex[i]);
			Composite composite = new Composite(tabFolder, SWT.NULL);
			if (i == 0) {
				createBasicControl(composite);
			}
			tabItem.setControl(composite);
		}
		setControl(parent);
	}

	private void createBasicControl(Composite container) {
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;
		layout.verticalSpacing = 5;
		layout.marginTop = 20;
		layout.marginWidth = 10;
		createTableInfoControl(container);
		// createTableColumnControl(container);

	}

	private void createTableInfoControl(Composite container) {
		Label tableName = new Label(container, SWT.NULL);
		tableName.setText("&表名:");
		Text tableNameVaue = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		tableNameVaue.setLayoutData(gd);

		Label tableLabel = new Label(container, SWT.NULL);
		tableLabel.setText("&名称:");
		Text tableLabelValue = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		tableLabelValue.setLayoutData(gd);

		bindTextWidgetValue(column, "name", tableNameVaue);
		bindTextWidgetValue(column, "label", tableLabelValue);

	}

	private void bindTextWidgetValue(Object obj, String property, Text text) {
		IObservableValue model = BeanProperties.value(obj.getClass(), property).observe(obj);
		IObservableValue widget = WidgetProperties.text(SWT.Modify).observe(text);
		dataBindingContext.bindValue(widget, model);
	}

}
