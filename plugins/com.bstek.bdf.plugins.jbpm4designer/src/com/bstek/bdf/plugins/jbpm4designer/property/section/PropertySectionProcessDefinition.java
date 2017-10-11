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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
/**
 * @author Jacky
 */
public class PropertySectionProcessDefinition extends AbstractGraphicalPropertySection {
	private Text nameText;
	private Text versionText;
	private Text idText;
	private Text descText;
	private ModifyListener nameModifyListener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("name", nameText.getText()));
		}
	};
	private ModifyListener versionModifyListener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("version", versionText.getText()));
		}
	};
	private ModifyListener idModifyListener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("key", idText.getText()));
		}
	};
	private ModifyListener descModifyListener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("description", descText.getText()));
		}
	};
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		Label nameLabel=this.getWidgetFactory().createLabel(composite, "流程模版名称：");
		FormData nameLabelFormData=new FormData();
		nameLabelFormData.top=new FormAttachment(0,15);
		nameLabelFormData.left=new FormAttachment(0,10);
		nameLabel.setLayoutData(nameLabelFormData);
		
		nameText=this.getWidgetFactory().createText(composite, "");
		FormData nameTextFormData=new FormData();
		nameTextFormData.top=new FormAttachment(0,15);
		nameTextFormData.left=new FormAttachment(nameLabel,1);
		nameTextFormData.right=new FormAttachment(100,-5);
		nameText.setLayoutData(nameTextFormData);
		
		Label versionLabel=this.getWidgetFactory().createLabel(composite, "版本：");
		FormData versionLabelFormData=new FormData();
		versionLabelFormData.top=new FormAttachment(nameLabel,20);
		versionLabelFormData.left=new FormAttachment(0,58);
		versionLabel.setLayoutData(versionLabelFormData);
		
		versionText=this.getWidgetFactory().createText(composite, "");
		FormData versionTextFormData=new FormData();
		versionTextFormData.left=new FormAttachment(versionLabel,1);
		versionTextFormData.top=new FormAttachment(nameText,10);
		versionTextFormData.right=new FormAttachment(100,-5);
		versionText.setLayoutData(versionTextFormData);
		
		Label idLabel=this.getWidgetFactory().createLabel(composite, "流程模版Key：");
		FormData idLabelFormData=new FormData();
		idLabelFormData.top=new FormAttachment(versionLabel,18);
		idLabelFormData.left=new FormAttachment(0,13);
		idLabel.setLayoutData(idLabelFormData);
		
		idText=this.getWidgetFactory().createText(composite, "");
		FormData idTextFormData=new FormData();
		idTextFormData.top=new FormAttachment(versionText,13);
		idTextFormData.left=new FormAttachment(idLabel,1);
		idTextFormData.right=new FormAttachment(100,-5);
		idText.setLayoutData(idTextFormData);
		
		Label descLabel=this.getWidgetFactory().createLabel(composite, "描述：");
		FormData data=new FormData();
		data.top=new FormAttachment(idLabel,10);
		data.left=new FormAttachment(0,58);
		descLabel.setLayoutData(data);
		
		this.descText=this.getWidgetFactory().createText(composite, "",SWT.MULTI|SWT.BORDER);
		data=new FormData();
		data.top=new FormAttachment(idText,10);
		data.left=new FormAttachment(descLabel,1);
		data.bottom=new FormAttachment(100,-10);
		data.right=new FormAttachment(100,-5);
		descText.setLayoutData(data);
	}

	@Override
	public void refresh() {
		this.idText.removeModifyListener(idModifyListener);
		this.versionText.removeModifyListener(versionModifyListener);
		this.nameText.removeModifyListener(nameModifyListener);
		this.descText.removeModifyListener(descModifyListener);
		String key=(String)getTargetProperty("key");
		this.idText.setText(key==null?"":key);
		String name=(String)getTargetProperty("name");
		this.nameText.setText(name==null?"":name);
		String version=(String)getTargetProperty("version");
		this.versionText.setText(version==null?"":version);
		
		String desc=(String)getTargetProperty("description");
		this.descText.setText(desc==null?"":desc);
		
		this.idText.addModifyListener(idModifyListener);
		this.versionText.addModifyListener(versionModifyListener);
		this.nameText.addModifyListener(nameModifyListener);
		this.descText.addModifyListener(descModifyListener);
	}
}
