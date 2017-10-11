/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.jbpm4designer.model.EndType;
/**
 * @author Jacky
 */
public class PropertySectionEndCancel extends AbstractGraphicalPropertySection {
	private Button buttonExecution;
	private Button buttonProcessInstance;
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		Label label=this.getWidgetFactory().createLabel(composite, "取消操作类型：");
		FormData data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(0,10);
		label.setLayoutData(data);
		
		buttonExecution=this.getWidgetFactory().createButton(composite, "execution",SWT.RADIO);
		data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(label,1);
		buttonExecution.setLayoutData(data);
		SelectionListener selectionListener=new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(buttonExecution.getSelection()){
					commandStack.execute(createPropertyChangeCommand("ends", EndType.execution));
				}
				if(buttonProcessInstance.getSelection()){
					commandStack.execute(createPropertyChangeCommand("ends", EndType.processInstance));
				}
			}
		};
		buttonExecution.addSelectionListener(selectionListener);
		
		buttonProcessInstance=this.getWidgetFactory().createButton(composite, "process-instance",SWT.RADIO);
		data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(buttonExecution,10);
		buttonProcessInstance.setLayoutData(data);
		buttonProcessInstance.addSelectionListener(selectionListener);
	}
	@Override
	public void refresh() {
		EndType type=(EndType)getTargetProperty("ends");
		if(type.equals(EndType.execution)){
			buttonExecution.setSelection(true);
		}
		if(type.equals(EndType.processInstance)){
			buttonProcessInstance.setSelection(true);
		}
	}
}
