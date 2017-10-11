/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.OpenTypeSelectionDialog;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
/**
 * @author Jacky
 */
public class PropertySectionCustom extends AbstractGraphicalPropertySection {
	private Text textHandlerClass;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("clazz", textHandlerClass.getText()));
		}
	};
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		Label handlerClassLabel=this.getWidgetFactory().createLabel(composite, "实现类：");
		FormData data=new FormData();
		data.top=new FormAttachment(0,12);
		data.left=new FormAttachment(0,10);
		handlerClassLabel.setLayoutData(data);
		
		this.textHandlerClass=this.getWidgetFactory().createText(composite, "");
		data=new FormData();
		data.top=new FormAttachment(0,10);
		data.left=new FormAttachment(handlerClassLabel,1);
		data.right=new FormAttachment(100,-100);
		this.textHandlerClass.setLayoutData(data);
		
		Button browserButton=this.getWidgetFactory().createButton(composite, "选择实现类...", SWT.PUSH);
		data=new FormData();
		data.top=new FormAttachment(0,8);
		data.left=new FormAttachment(textHandlerClass,1);
		data.right=new FormAttachment(100,-10);
		browserButton.setLayoutData(data);
		browserButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				OpenTypeSelectionDialog dialog=new OpenTypeSelectionDialog(JavaPlugin.getActiveWorkbenchShell(),false, PlatformUI.getWorkbench().getProgressService(),JavaSearchScopeFactory.getInstance().createWorkspaceScope(true),IJavaSearchConstants.TYPE);
				if(dialog.open()!=IDialogConstants.OK_ID){
					return;
				}
				Object[] obj=dialog.getResult();
				if(obj!=null && obj.length>0){
					textHandlerClass.setText(((IType)obj[0]).getFullyQualifiedName());
				}
			}
		});
	}

	@Override
	public void refresh() {
		String clazz=(String)getTargetProperty("clazz");
		if(clazz!=null){
			this.textHandlerClass.setText(clazz);
		}
		this.textHandlerClass.removeModifyListener(listener);
		this.textHandlerClass.addModifyListener(listener);
	}
}
