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

import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
/**
 * @author Jacky
 */
public class PropertySectionTransition extends AbstractGraphicalPropertySection {
	private Text textExpression;
	private Text textTransitionName;
	private ModifyListener expressionModifylistener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("expression", textExpression.getText()));
		}
	};
	private ModifyListener nameModifylistener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			TransitionLabel label=(TransitionLabel)getTargetProperty("transitionLabel");
			if(label==null){
				label=new TransitionLabel("");
				commandStack.execute(createPropertyChangeCommand("transitionLabel", label));
			}
			label.setText(textTransitionName.getText());
		}
	};
	public PropertySectionTransition() {
	}

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		System.out.println(parent);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		Label nameLabel=this.getWidgetFactory().createLabel(composite, "Transition名称：");
		FormData nameLabelFormData=new FormData();
		nameLabelFormData.top=new FormAttachment(0,10);
		nameLabelFormData.left=new FormAttachment(0,10);
		nameLabel.setLayoutData(nameLabelFormData);
		
		this.textTransitionName=this.getWidgetFactory().createText(composite, "");
		FormData nameFormData=new FormData();
		nameFormData.top=new FormAttachment(0,10);
		nameFormData.left=new FormAttachment(nameLabel,1);
		nameFormData.right=new FormAttachment(100,-10);
		this.textTransitionName.setLayoutData(nameFormData);
		
		Label label=this.getWidgetFactory().createLabel(composite, "条件表达式：");
		FormData formData=new FormData();
		formData.left=new FormAttachment(0,30);
		formData.top=new FormAttachment(nameLabel,15);
		label.setLayoutData(formData);
		this.textExpression=this.getWidgetFactory().createText(composite, "",SWT.MULTI|SWT.V_SCROLL|SWT.WRAP);
		
		formData=new FormData();
		formData.top=new FormAttachment(this.textTransitionName,10);
		formData.left=new FormAttachment(label,1);
		formData.right=new FormAttachment(100,-10);
		formData.bottom=new FormAttachment(100,-10);
		this.textExpression.setLayoutData(formData);
		this.textExpression.setEnabled(false);
	}

	@Override
	public void refresh() {
		this.textExpression.removeModifyListener(expressionModifylistener);
		this.textTransitionName.removeModifyListener(nameModifylistener);
		String expression=(String)getTargetProperty("expression");
		TransitionLabel label=(TransitionLabel)getTargetProperty("transitionLabel");
		this.textExpression.setText(expression!=null?expression:"");
		if(label!=null){
			textTransitionName.setText(label.getText());
		}
		this.textExpression.addModifyListener(expressionModifylistener);
		this.textTransitionName.addModifyListener(nameModifylistener);
	}
}
