/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section;

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
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessType;
/**
 * @author Jacky
 */
public class PropertySectionSubprocess extends AbstractGraphicalPropertySection {
	private Button[] typeButtons=new Button[2];
	private Text textId;
	private Text textKey;
	private Text textOutcome;
	private Composite idComposite;
	private Composite keyComposite;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			SubprocessType type=(SubprocessType)getTargetProperty("type");
			if(typeButtons[0].getSelection()){
				type=SubprocessType.id;
				commandStack.execute(createPropertyChangeCommand("id", textId.getText()));
			}
			if(typeButtons[1].getSelection()){
				type=SubprocessType.key;
				commandStack.execute(createPropertyChangeCommand("key", textKey.getText()));
			}
			commandStack.execute(createPropertyChangeCommand("type", type));
			commandStack.execute(createPropertyChangeCommand("outcome", textOutcome.getText()));
		}
	};
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		
		Label outcomeLabel=this.getWidgetFactory().createLabel(composite, "Transition名称：");
		FormData data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(0,10);
		outcomeLabel.setLayoutData(data);
		this.textOutcome=this.getWidgetFactory().createText(composite, "");
		textOutcome.setToolTipText("在执行完子流程离开当前节点后，要流向下一节点采用的Transition的名称");
		data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(outcomeLabel,1);
		data.right=new FormAttachment(100,-10);
		textOutcome.setLayoutData(data);
		
		typeButtons[0]=this.getWidgetFactory().createButton(composite, "通过ID指定子流程", SWT.RADIO);
		data=new FormData();
		data.top=new FormAttachment(outcomeLabel,20);
		data.left=new FormAttachment(0,10);
		typeButtons[0].setLayoutData(data);
		typeButtons[1]=this.getWidgetFactory().createButton(composite, "通过KEY指定子流程", SWT.RADIO);
		data=new FormData();
		data.top=new FormAttachment(outcomeLabel,20);
		data.left=new FormAttachment(typeButtons[0],15);
		typeButtons[1].setLayoutData(data);
		
		idComposite=this.getWidgetFactory().createFlatFormComposite(composite);
		data=new FormData();
		data.top=new FormAttachment(0,80);
		data.left=new FormAttachment(0,0);
		data.right=new FormAttachment(100,0);
		idComposite.setLayoutData(data);
		idComposite.setLayout(new FormLayout());
		
		Label idLabel=this.getWidgetFactory().createLabel(idComposite, "子流程ID：");
		data=new FormData();
		data.top=new FormAttachment(0,1);
		data.left=new FormAttachment(0,40);
		idLabel.setLayoutData(data);
		
		this.textId=this.getWidgetFactory().createText(idComposite, "");
		data=new FormData();
		data.top=new FormAttachment(0,1);
		data.left=new FormAttachment(idLabel,1);
		data.right=new FormAttachment(100,-10);
		textId.setLayoutData(data);
		idComposite.setVisible(false);
		
		keyComposite=this.getWidgetFactory().createFlatFormComposite(composite);
		data=new FormData();
		data.top=new FormAttachment(0,80);
		data.left=new FormAttachment(0,0);
		data.right=new FormAttachment(100,0);
		keyComposite.setLayoutData(data);
		keyComposite.setLayout(new FormLayout());
		
		Label keyLabel=this.getWidgetFactory().createLabel(keyComposite, "子流程Key：");
		data=new FormData();
		data.top=new FormAttachment(0,1);
		data.left=new FormAttachment(0,40);
		keyLabel.setLayoutData(data);
		this.textKey=this.getWidgetFactory().createText(keyComposite, "");
		data=new FormData();
		data.top=new FormAttachment(0,1);
		data.left=new FormAttachment(keyLabel,1);
		data.right=new FormAttachment(100,-10);
		textKey.setLayoutData(data);
		keyComposite.setVisible(false);
		
		typeButtons[0].addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(typeButtons[0].getSelection()){
					idComposite.setVisible(true);
					keyComposite.setVisible(false);
				}
			}
		});
		typeButtons[1].addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(typeButtons[1].getSelection()){
					idComposite.setVisible(false);
					keyComposite.setVisible(true);
				}
			}
		});
	}

	@Override
	public void refresh() {
		this.textId.removeModifyListener(listener);
		this.textKey.removeModifyListener(listener);
		this.textOutcome.removeModifyListener(listener);
		String id=(String)getTargetProperty("id");
		String key=(String)getTargetProperty("key");
		String outcome=(String)getTargetProperty("outcome");
		SubprocessType type=(SubprocessType)getTargetProperty("type");
		this.textId.setText(id!=null?id:"");
		this.textKey.setText(key!=null?key:"");
		this.textOutcome.setText(outcome!=null?outcome:"");
		if(type!=null && type.equals(SubprocessType.id)){
			this.typeButtons[0].setSelection(true);
			this.idComposite.setVisible(true);
		}
		if(type!=null && type.equals(SubprocessType.key)){
			this.typeButtons[1].setSelection(true);
			this.keyComposite.setVisible(true);
		}
		this.textId.addModifyListener(listener);
		this.textKey.addModifyListener(listener);
		this.textOutcome.addModifyListener(listener);
	}
}
