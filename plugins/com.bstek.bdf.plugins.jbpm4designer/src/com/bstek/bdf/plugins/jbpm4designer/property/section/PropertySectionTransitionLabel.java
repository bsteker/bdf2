/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section;

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
public class PropertySectionTransitionLabel extends AbstractGraphicalPropertySection {
	private Text labelText;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("text", labelText.getText()));
		}
	};

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		Label la=this.getWidgetFactory().createLabel(composite,"名称：");
		FormData laData=new FormData();
		laData.top=new FormAttachment(0,12);
		laData.left=new FormAttachment(0,10);
		la.setLayoutData(laData);
		
		this.labelText=this.getWidgetFactory().createText(composite, "");
		FormData textData=new FormData();
		textData.left=new FormAttachment(la,1);
		textData.top=new FormAttachment(0,12);
		textData.right=new FormAttachment(100,-10);
		this.labelText.setLayoutData(textData);
	}

	@Override
	public void refresh() {
		this.labelText.removeModifyListener(listener);
		String text=(String)getTargetProperty("text");
		this.labelText.setText(text==null?"":text);
		this.labelText.addModifyListener(listener);
	}
}
