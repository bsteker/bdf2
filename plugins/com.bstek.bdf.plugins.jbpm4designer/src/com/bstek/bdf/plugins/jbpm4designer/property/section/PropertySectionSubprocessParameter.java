/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.jbpm4designer.command.AddSubprocessInParameterCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.AddSubprocessOutParameterCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.RemoveSubprocessInParameterCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.RemoveSubprocessOutParameterCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessNode;
import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessParameter;
import com.bstek.bdf.plugins.jbpm4designer.property.section.table.ParameterCellModifier;
import com.bstek.bdf.plugins.jbpm4designer.property.section.table.ParameterTableLabelProvider;
/**
 * @author Jacky
 */
public class PropertySectionSubprocessParameter extends AbstractGraphicalPropertySection {
	private TableViewer inViewer;
	private TableViewer outViewer;
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		CTabFolder tabFolder=this.getWidgetFactory().createTabFolder(composite, SWT.BORDER);
		tabFolder.marginHeight=5;
		tabFolder.marginWidth=5;
		tabFolder.setTabHeight(22);
		tabFolder.setSimple(false);
		tabFolder.setBackground(ColorConstants.white);
		CTabItem inItem=this.getWidgetFactory().createTabItem(tabFolder, SWT.NONE);
		inItem.setText("父流程到子流程变量设定");
		Composite inComposite=this.getWidgetFactory().createFlatFormComposite(tabFolder);
		inItem.setControl(inComposite);
		inItem.setToolTipText("用于设置将父流程中相关流程变量传递到子流程当中，供子流程使用");
		inComposite.setLayout(new FillLayout());
		FormData data=new FormData();
		data.top=new FormAttachment(0,10);
		data.left=new FormAttachment(0,10);
		data.right=new FormAttachment(100,-10);
		data.bottom=new FormAttachment(100,-10);
		tabFolder.setLayoutData(data);
		inComposite.setLayout(new FillLayout());
		Table inTable=this.getWidgetFactory().createTable(inComposite, SWT.NONE);
		inTable.setLinesVisible(true);
		inTable.setHeaderVisible(true);
		this.inViewer=new TableViewer(inTable);
		TableColumn inVarCol=new TableColumn(inTable,SWT.NONE);
		inVarCol.setText("父流程变量名");
		inVarCol.setWidth(150);
		TableColumn inSubvarCol=new TableColumn(inTable,SWT.NONE);
		inSubvarCol.setText("子流程变量名");
		inSubvarCol.setWidth(150);
		inViewer.setLabelProvider(new ParameterTableLabelProvider());
		inViewer.setContentProvider(new ArrayContentProvider());
		inViewer.setCellEditors(new CellEditor[]{new TextCellEditor(inTable),new TextCellEditor(inTable)});
		inViewer.setColumnProperties(new String[]{"var","subvar"});
		inViewer.setCellModifier(new ParameterCellModifier(inViewer));
		Menu inMenu=new Menu(inTable);
		MenuItem addInItem=new MenuItem(inMenu,SWT.NONE);
		addInItem.setText("添加");
		addInItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				SubprocessParameter p=new SubprocessParameter();
				p.setVar("var");
				p.setSubvar("subvar");
				commandStack.execute(new AddSubprocessInParameterCommand(p,(SubprocessNode)target));
				inViewer.refresh();
			}
		});
		MenuItem delInItem=new MenuItem(inMenu,SWT.NONE);
		delInItem.setText("删除");
		delInItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				SubprocessParameter p=(SubprocessParameter)((IStructuredSelection)inViewer.getSelection()).getFirstElement();
				commandStack.execute(new RemoveSubprocessInParameterCommand(p,(SubprocessNode)target));
				inViewer.refresh();
			}
		});
		inTable.setMenu(inMenu);
		
		CTabItem outItem=this.getWidgetFactory().createTabItem(tabFolder, SWT.NONE);
		Composite outComposite=this.getWidgetFactory().createFlatFormComposite(tabFolder);
		outItem.setText("子流程到父流程变量设定");
		outItem.setControl(outComposite);
		outItem.setToolTipText("实现将子流程中相关流程变量传递到父流程当中，供父流程使用");
		outComposite.setLayout(new FillLayout());
		Table outTable=this.getWidgetFactory().createTable(outComposite, SWT.NONE);
		outTable.setHeaderVisible(true);
		outTable.setLinesVisible(true);
		this.outViewer=new TableViewer(outTable);
		TableColumn outVarCol=new TableColumn(outTable,SWT.NONE);
		outVarCol.setText("父流程变量名");
		outVarCol.setWidth(150);
		TableColumn outSubvarCol=new TableColumn(outTable,SWT.NONE);
		outSubvarCol.setText("子流程变量名");
		outSubvarCol.setWidth(150);
		outViewer.setLabelProvider(new ParameterTableLabelProvider());
		outViewer.setContentProvider(new ArrayContentProvider());
		outViewer.setCellEditors(new CellEditor[]{new TextCellEditor(outTable),new TextCellEditor(outTable)});
		outViewer.setColumnProperties(new String[]{"var","subvar"});
		outViewer.setCellModifier(new ParameterCellModifier(outViewer));
		Menu outMenu=new Menu(outTable);
		MenuItem addOutItem=new MenuItem(outMenu,SWT.NONE);
		addOutItem.setText("添加");
		addOutItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				SubprocessParameter p=new SubprocessParameter();
				p.setVar("var");
				p.setSubvar("subvar");
				commandStack.execute(new AddSubprocessOutParameterCommand(p,(SubprocessNode)target));
				outViewer.refresh();
			}
		});
		MenuItem delOutItem=new MenuItem(outMenu,SWT.NONE);
		delOutItem.setText("删除");
		delOutItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				SubprocessParameter p=(SubprocessParameter)((IStructuredSelection)outViewer.getSelection()).getFirstElement();
				commandStack.execute(new RemoveSubprocessOutParameterCommand(p,(SubprocessNode)target));
				outViewer.refresh();
			}
		});
		outTable.setMenu(outMenu);
		
		
		tabFolder.setSelection(0);
	}

	@Override
	public void refresh() {
		this.inViewer.setInput(getTargetProperty("inParameters"));
		this.outViewer.setInput(getTargetProperty("outParameters"));
	}
}
