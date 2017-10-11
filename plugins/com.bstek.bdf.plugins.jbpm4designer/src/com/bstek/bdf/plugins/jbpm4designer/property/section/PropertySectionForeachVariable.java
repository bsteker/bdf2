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
public class PropertySectionForeachVariable extends AbstractGraphicalPropertySection {
	private Text textCollection;
	private Text textVariable;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("collection", textCollection.getText()));
			commandStack.execute(createPropertyChangeCommand("variable", textVariable.getText()));
		}
	};
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		Label collectionLabel=this.getWidgetFactory().createLabel(composite, "要分拆的集合：");
		FormData data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(0,10);
		collectionLabel.setLayoutData(data);
		textCollection=this.getWidgetFactory().createText(composite, "",SWT.BORDER);
		textCollection.setToolTipText("可以是流程变量,也可以是以逗号分隔的字符串");
		data=new FormData();
		data.top=new FormAttachment(0,10);
		data.left=new FormAttachment(collectionLabel,1);
		data.right=new FormAttachment(100,-10);
		textCollection.setLayoutData(data);
		
		Label variableLabel=this.getWidgetFactory().createLabel(composite, "变量名：");
		data=new FormData();
		data.top=new FormAttachment(collectionLabel,20);
		data.left=new FormAttachment(0,45);
		variableLabel.setLayoutData(data);
		this.textVariable=this.getWidgetFactory().createText(composite, "");
		data=new FormData();
		data.top=new FormAttachment(textCollection,15);
		data.left=new FormAttachment(variableLabel,1);
		data.right=new FormAttachment(100,-10);
		textVariable.setLayoutData(data);
		textVariable.setToolTipText("定义的集合会被拆分，拆分后每个变量都将存放到这个变量名当中");
		
	}
	@Override
	public void refresh() {
		this.textCollection.removeModifyListener(listener);
		this.textVariable.removeModifyListener(listener);
		String collection=(String)getTargetProperty("collection");
		this.textCollection.setText(collection!=null?collection:"");
		String variable=(String)getTargetProperty("variable");
		this.textVariable.setText(variable!=null?variable:"");
		this.textCollection.addModifyListener(listener);
		this.textVariable.addModifyListener(listener);
	}
}
