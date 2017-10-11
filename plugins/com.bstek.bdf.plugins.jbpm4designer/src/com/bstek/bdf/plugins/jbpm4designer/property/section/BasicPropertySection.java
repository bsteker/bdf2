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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
/**
 * @author Jacky
 */
public class BasicPropertySection extends AbstractGraphicalPropertySection {
	private Text nameText;
	private Text descText;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("label",nameText.getText()));
			commandStack.execute(createPropertyChangeCommand("description",descText.getText()));
		}
	};

	@Override
	public void createControls(Composite parent,TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		FormData data=new FormData();
		data.left=new FormAttachment(0,10);
		data.top=new FormAttachment(0,14);
		Label label=this.getWidgetFactory().createLabel(composite, "节点名称：");
		label.setLayoutData(data);
		data=new FormData();
		data.top=new FormAttachment(0,10);
		data.left=new FormAttachment(label,1);
		data.right=new FormAttachment(100,-10);
		this.nameText=this.getWidgetFactory().createText(composite, "");
		this.nameText.setLayoutData(data);
		
		Label descLabel=this.getWidgetFactory().createLabel(composite, "描述：");
		data=new FormData();
		data.top=new FormAttachment(label,10);
		data.left=new FormAttachment(0,33);
		descLabel.setLayoutData(data);
		
		this.descText=this.getWidgetFactory().createText(composite, "",SWT.MULTI|SWT.BORDER);
		data=new FormData();
		data.top=new FormAttachment(nameText,10);
		data.left=new FormAttachment(descLabel,1);
		data.bottom=new FormAttachment(100,-10);
		data.right=new FormAttachment(100,-10);
		descText.setLayoutData(data);
	}

	@Override
	public void refresh() {
		this.nameText.removeModifyListener(listener);
		this.descText.removeModifyListener(listener);
		this.nameText.setText((String)this.getTargetProperty("label"));
		String desc=(String)this.getTargetProperty("description");
		this.descText.setText(desc==null?"":desc);
		this.nameText.addModifyListener(listener);
		this.descText.addModifyListener(listener);
	}
}
