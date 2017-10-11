/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.wizard.pages;

import java.util.Collections;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.wizard.pages.dialog.ColumnDialog;

public class TableModifyWizardPage extends WizardPage {
	private Table model;
	private Column columnSelected;
	private DataBindingContext dataBindingContext;
	private TableViewer tableViewer;
	private Button buttonAdd;
	private Button buttonDelete;
	private Button buttonModify;
	private Button buttonUp;
	private Button buttonDown;

	public TableModifyWizardPage(String pageName, Table table) {
		super(pageName);
		setTitle("修改表信息");
		setDescription("对数据表进行维护");
		dataBindingContext = new DataBindingContext();
		model = table;
	}

	@Override
	public void createControl(Composite parent) {
		final TabFolder tabFolder = new TabFolder(parent, SWT.NULL);
		String[] tabIndex = new String[] { "基本属性", "备注", "其他" };
		for (int i = 0; i < tabIndex.length - 1; i++) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
			tabItem.setText(tabIndex[i]);
			Composite composite = new Composite(tabFolder, SWT.NULL);
			if (i == 0) {
				createBasicControl(composite);
			} else if (i == 1) {
				createCommentControl(composite);
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
		createTableHeaderInfoControl(container);
		createTableColumnInfoControl(container);

	}

	private void createCommentControl(Composite container) {
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 5;
		layout.marginTop = 20;
		layout.marginWidth = 10;

		Label commentLabel = new Label(container, SWT.NULL);
		commentLabel.setText("&备注:");
		Text commentText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		GridData gridData = new GridData();
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 5;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		commentText.setLayoutData(gridData);
		
		bindWidgetValue(model, Table.COMMENT, commentText);
	}

	private void createTableHeaderInfoControl(Composite container) {
		Label tableName = new Label(container, SWT.NULL);
		tableName.setText("&表名:");
		final Text tableNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		tableNameText.setFocus();
		tableNameText.addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event e) {
				tableNameText.selectAll();
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		tableNameText.setLayoutData(gd);

		Label tableLabel = new Label(container, SWT.NULL);
		tableLabel.setText("&名称:");
		Text tableLabelText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		tableLabelText.setLayoutData(gd);

		bindWidgetValue(model, Table.NAME, tableNameText);
		bindWidgetValue(model, Table.LABEL, tableLabelText);

	}

	private void createTableColumnInfoControl(Composite container) {
		createButtonGroupControl(container);
		createTableControl(container);
		addTableViewerListener();
		addButtonGroupListener();
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

	private void createTableControl(Composite container) {
		tableViewer = new TableViewer(container, SWT.FULL_SELECTION | SWT.BORDER);
		String[] columnIndex = new String[] { "主键", "外键", "列名", "名称", "类型", "不能为空", "是否唯一" };
		Integer[] columnWidth = new Integer[] { 40, 40, 120, 120, 120, 80, 80 };
		Integer[] columnAlign = new Integer[] { SWT.CENTER, SWT.CENTER, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.CENTER, SWT.CENTER };
		for (int i = 0; i < columnIndex.length; i++) {
			TableViewerColumn choice = new TableViewerColumn(tableViewer, SWT.BORDER);
			choice.getColumn().setText(columnIndex[i]);
			choice.getColumn().setWidth(columnWidth[i]);
			choice.getColumn().setAlignment(columnAlign[i]);
		}
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 4;
		tableViewer.getTable().setLayoutData(gd);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TableColumnLabelProvider());
		tableViewer.setInput(model.getColumns());
	}

	private void createButtonGroupControl(Composite container) {
		Composite group1 = new Composite(container, SWT.NONE);
		GridLayout gridLayoutButton = new GridLayout();
		gridLayoutButton.verticalSpacing = 10;
		gridLayoutButton.numColumns = 5;
		group1.setLayout(gridLayoutButton);
		GridData gd = new GridData();
		gd.horizontalSpan = 4;
		group1.setLayoutData(gd);

		buttonAdd = new Button(group1, SWT.PUSH);
		buttonAdd.setText("添加列");

		buttonDelete = new Button(group1, SWT.PUSH);
		buttonDelete.setText("删除列");
		buttonDelete.setEnabled(false);

		buttonModify = new Button(group1, SWT.PUSH);
		buttonModify.setText("修改列");
		buttonModify.setEnabled(false);

		buttonUp = new Button(group1, SWT.PUSH);
		buttonUp.setText("上移");
		buttonUp.setEnabled(false);

		buttonDown = new Button(group1, SWT.PUSH);
		buttonDown.setText("下移");
		buttonDown.setEnabled(false);

	}

	private void addTableViewerListener() {
		tableViewer.addDoubleClickListener(new TableViewerDoubleClickListener());
		tableViewer.addSelectionChangedListener(new TableViewerSelectionChangedListener());
	}

	private void addButtonGroupListener() {
		buttonAdd.addSelectionListener(new ButtonGroupSelectionAdapter());
		buttonDelete.addSelectionListener(new ButtonGroupSelectionAdapter());
		buttonModify.addSelectionListener(new ButtonGroupSelectionAdapter());
		buttonUp.addSelectionListener(new ButtonGroupSelectionAdapter());
		buttonDown.addSelectionListener(new ButtonGroupSelectionAdapter());

	}

	public Table getTable() {
		return model;
	}

	public void setTable(Table table) {
		this.model = table;
	}

	private void setEnabled(Button button, boolean flag) {
		if ((flag == true && button.getEnabled() == false) || (flag == false && button.getEnabled() == true)) {
			button.setEnabled(flag);
		}
	}

	private class TableColumnLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			Column c = (Column) element;
			if (columnIndex == 0) {
				return c.isPk() ? Activator.getImage(Activator.IMAGE_PK) : null;
			} else if (columnIndex == 1) {
				return c.isFk() ? Activator.getImage(Activator.IMAGE_FK) : null;
			}
			return null;
		}

		public String getColumnText(Object element, int index) {
			Column c = (Column) element;
			String s = "∨";
			switch (index) {
			case 0:
				return null;
			case 1:
				return null;
			case 2:
				return c.getName();
			case 3:
				return c.getLabel();
			case 4:
				String type = c.getType();
				ColumnType columnType = DbDialectManager.getColumnType(DbDialectManager.getCurrentDbDialect(), type);
				String length = c.getLength();
				String decimal = c.getDecimalLength();
				if (columnType.isLength() && columnType.isDecimal() && length.length() > 0 && decimal.length() > 0) {
					type += "(" + length + "," + decimal + ")";
				} else if (columnType.isLength() && length.length() > 0 && decimal.length() == 0) {
					type += "(" + length + ")";
				}
				return type;
			case 5:
				return c.isNotNull() ? s : null;
			case 6:
				return c.isUnique() ? s : null;
			default:
				break;
			}
			return "";
		}
	}

	private class TableViewerSelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			columnSelected = (Column) ((StructuredSelection) event.getSelection()).getFirstElement();
			if (columnSelected != null) {
				int index = model.getColumns().indexOf(columnSelected);
				int count = model.getColumns().size();
				setEnabled(buttonDown, count == index + 1 ? false : true);
				setEnabled(buttonUp, 0 == index ? false : true);
				setEnabled(buttonModify, true);
				setEnabled(buttonDelete, true);

			}

		}

	}

	private class TableViewerDoubleClickListener implements IDoubleClickListener {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			ColumnDialog dialog = new ColumnDialog(getShell(), columnSelected, ColumnDialog.TYPE_MODIFY);
			if (dialog.open() == Dialog.OK) {
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(columnSelected));
			}
		}
	}

	private class ButtonGroupSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			int i = model.getColumns().indexOf(columnSelected);
			if (e.getSource().equals(buttonUp)) {
				Collections.swap(model.getColumns(), i, i - 1);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(columnSelected));
			} else if (e.getSource().equals(buttonDown)) {
				Collections.swap(model.getColumns(), i, i + 1);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(columnSelected));
			} else if (e.getSource().equals(buttonAdd)) {
				Column c = new Column();
				c.setCanDelete(true);
				ColumnDialog dialog = new ColumnDialog(getShell(), c, ColumnDialog.TYPE_NEW);
				if (dialog.open() == Dialog.OK) {
					model.getColumns().add(c);
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(c));
				}
			} else if (e.getSource().equals(buttonModify)) {
				ColumnDialog dialog = new ColumnDialog(getShell(), columnSelected, ColumnDialog.TYPE_MODIFY);
				if (dialog.open() == Dialog.OK) {
					tableViewer.refresh();
					tableViewer.setSelection(new StructuredSelection(columnSelected));
				}

			} else if (e.getSource().equals(buttonDelete)) {
				if (columnSelected.isFk()) {
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "提示", "不能删除外键约束列！");
					return;
				}
				if (!columnSelected.isCanDelete()) {
					MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "提示", "不能删除外键约束主键！");
					return;
				}
				model.removeColumn(columnSelected);
				tableViewer.refresh();
				StructuredSelection selection = null;
				int size = model.getColumns().size();
				if (size != 0) {
					Column c = model.getColumns().get(i < size - 1 ? i : size - 1);
					if (c != null) {
						selection = new StructuredSelection(c);
					} else {
						c = model.getColumns().get(i - 1);
						if (c != null) {
							selection = new StructuredSelection(c);
						}
					}
				}
				if (selection != null) {
					tableViewer.setSelection(selection);
				} else {
					setEnabled(buttonModify, false);
					setEnabled(buttonDelete, false);
				}
			}
		}
	}
}
