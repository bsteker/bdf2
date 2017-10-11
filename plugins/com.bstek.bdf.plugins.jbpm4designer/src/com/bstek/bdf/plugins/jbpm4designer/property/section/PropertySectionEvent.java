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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.jbpm4designer.command.AddEventCommand;
import com.bstek.bdf.plugins.jbpm4designer.command.RemoveEventCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.Event;
import com.bstek.bdf.plugins.jbpm4designer.model.EventType;
/**
 * @author Jacky
 */
public class PropertySectionEvent extends AbstractGraphicalPropertySection {
	private ListViewer listViewer;
	private CCombo typeCombo;
	private Text valueText;
	private Composite detailComposite;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			Object obj=((IStructuredSelection)listViewer.getSelection()).getFirstElement();
			if(obj!=null){
				Event event=(Event)obj;
				String typeValue=typeCombo.getText();
				if(typeValue!=null && !typeValue.equals("")){
					event.setType(EventType.valueOf(typeValue));					
					listViewer.refresh();
				}
				event.setListenerClass(valueText.getText());
			}
		}
	};

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		final Composite composite=this.getWidgetFactory().createComposite(parent);
		composite.setLayout(new FormLayout());
		List list=this.getWidgetFactory().createList(composite, SWT.SINGLE|SWT.BORDER);
		FormData listData=new FormData();
		listData.width=120;
		listData.top=new FormAttachment(0,10);
		listData.left=new FormAttachment(0,10);
		listData.bottom=new FormAttachment(100,-10);
		list.setLayoutData(listData);
		listViewer=new ListViewer(list);
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				Event event=(Event)element;
				if(event.getType()!=null){
					return event.getType().toString();					
				}else{
					return "<新事件>";
				}
			}
		});
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object obj=((IStructuredSelection)listViewer.getSelection()).getFirstElement();
				if(obj!=null){
					detailComposite.setVisible(true);
					typeCombo.removeModifyListener(listener);
					valueText.removeModifyListener(listener);
					refreshEventInfo((Event)obj);
					typeCombo.addModifyListener(listener);
					valueText.addModifyListener(listener);
				}else{
					detailComposite.setVisible(false);
				}
			}
		});
		
		Menu menu=new Menu(list);
		MenuItem itemAdd=new MenuItem(menu,SWT.NONE);
		itemAdd.setText("添加事件");
		itemAdd.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Event event=new Event();
				commandStack.execute(new AddEventCommand((AbstractNodeElement)target, event));
				listViewer.refresh();
			}
		});
		MenuItem itemDel=new MenuItem(menu,SWT.NONE);
		itemDel.setText("删除事件");
		itemDel.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj=((IStructuredSelection)listViewer.getSelection()).getFirstElement();
				if(obj!=null){
					Event event=(Event)obj;
					commandStack.execute(new RemoveEventCommand((AbstractNodeElement)target, event));
					listViewer.refresh();
				}
			}
		});
		list.setMenu(menu);		
		detailComposite=this.getWidgetFactory().createFlatFormComposite(composite);
		FormData detailData=new FormData();
		detailData.top=new FormAttachment(0,10);
		detailData.left=new FormAttachment(list,1);
		detailData.right=new FormAttachment(100,-10);
		detailData.bottom=new FormAttachment(100,-10);
		detailComposite.setLayoutData(detailData);
		detailComposite.setLayout(new FormLayout());
		Label eventTypeLabel=this.getWidgetFactory().createLabel(detailComposite, "事件类型：");
		FormData data=new FormData();
		data.left=new FormAttachment(0,22);
		data.top=new FormAttachment(0,10);
		eventTypeLabel.setLayoutData(data);
		
		typeCombo=this.getWidgetFactory().createCCombo(detailComposite, SWT.BORDER_SOLID|SWT.READ_ONLY);
		data=new FormData();
		data.left=new FormAttachment(eventTypeLabel,1);
		data.top=new FormAttachment(0,10);
		data.right=new FormAttachment(100,-10);
		typeCombo.setLayoutData(data);
		for(EventType type:EventType.values()){
			typeCombo.add(type.toString());
		}
		
		Label valueLabel=this.getWidgetFactory().createLabel(detailComposite, "事件实现类：");
		data=new FormData();
		data.left=new FormAttachment(0,10);
		data.top=new FormAttachment(eventTypeLabel,17);
		valueLabel.setLayoutData(data);
		
		this.valueText=this.getWidgetFactory().createText(detailComposite, "",SWT.BORDER);
		data=new FormData();
		data.left=new FormAttachment(valueLabel,1);
		data.top=new FormAttachment(typeCombo,15);
		data.right=new FormAttachment(100,-120);
		this.valueText.setLayoutData(data);
		
		Button button=this.getWidgetFactory().createButton(detailComposite, "选择实现类...",SWT.PUSH);
		data=new FormData();
		data.left=new FormAttachment(valueText,1);
		data.top=new FormAttachment(typeCombo,12);
		data.right=new FormAttachment(100,-10);
		button.setLayoutData(data);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				OpenTypeSelectionDialog dialog=new OpenTypeSelectionDialog(JavaPlugin.getActiveWorkbenchShell(),false,PlatformUI.getWorkbench().getProgressService(),JavaSearchScopeFactory.getInstance().createWorkspaceScope(true),IJavaSearchConstants.TYPE);
				if(dialog.open()==IDialogConstants.OK_ID){
					IType type=(IType)dialog.getResult()[0];
					valueText.setText(type.getFullyQualifiedName());
				}
			}
		});
		detailComposite.setVisible(false);
	}
	
	private void refreshEventInfo(Event event){
		this.typeCombo.setText(event.getType()!=null?event.getType().toString():"");
		this.valueText.setText(event.getListenerClass()!=null?event.getListenerClass():"");
	}

	@Override
	public void refresh() {
		listViewer.setInput(getTargetProperty("events"));
	}
}
