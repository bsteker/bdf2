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

import com.bstek.bdf.plugins.jbpm4designer.model.DecisionType;
/**
 * @author Jacky
 */
public class PropertySectionDecision extends AbstractGraphicalPropertySection {
	private Button[] radioButtons=new Button[2];
	private Text textExpression;
	private Text textHandlerClass;
	private Composite expressionComposite;
	private Composite handlerClassComposite;
	private Composite composite;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			DecisionType type=(DecisionType)getTargetProperty("type");
			if(radioButtons[0].getSelection()){
				type=DecisionType.expression;
				commandStack.execute(createPropertyChangeCommand("expression", textExpression.getText()));
			}
			if(radioButtons[1].getSelection()){
				type=DecisionType.handlerClass;
				commandStack.execute(createPropertyChangeCommand("handlerClass", textHandlerClass.getText()));
			}
			commandStack.execute(createPropertyChangeCommand("type", type));
		}
	};
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		radioButtons[0]=this.getWidgetFactory().createButton(composite, "表达式",SWT.RADIO);
		FormData data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(0,22);
		radioButtons[0].setLayoutData(data);
		
		radioButtons[1]=this.getWidgetFactory().createButton(composite, "实现类",SWT.RADIO);
		data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(radioButtons[0],1);
		radioButtons[1].setLayoutData(data);
		
		expressionComposite=this.getWidgetFactory().createFlatFormComposite(composite);
		expressionComposite.setVisible(false);
		data=new FormData();
		data.top=new FormAttachment(radioButtons[0],10);
		data.left=new FormAttachment(0,10);
		data.right=new FormAttachment(100,-10);
		expressionComposite.setLayoutData(data);
		Label expressionLabel=this.getWidgetFactory().createLabel(expressionComposite, "表达式：");
		data=new FormData();
		data.top=new FormAttachment(0,2);
		data.left=new FormAttachment(0,10);
		expressionLabel.setLayoutData(data);
		
		this.textExpression=this.getWidgetFactory().createText(expressionComposite, "");
		data=new FormData();
		data.top=new FormAttachment(0,2);
		data.left=new FormAttachment(expressionLabel,1);
		data.right=new FormAttachment(100,-10);
		this.textExpression.setLayoutData(data);
		
		handlerClassComposite=this.getWidgetFactory().createFlatFormComposite(composite);
		handlerClassComposite.setVisible(false);
		final FormData classData=new FormData();
		classData.top=new FormAttachment(0,40);
		classData.left=new FormAttachment(0,10);
		classData.right=new FormAttachment(100,-10);
		handlerClassComposite.setLayoutData(classData);
		Label handlerClassLabel=this.getWidgetFactory().createLabel(handlerClassComposite, "实现类：");
		data=new FormData();
		data.top=new FormAttachment(0,2);
		data.left=new FormAttachment(0,10);
		handlerClassLabel.setLayoutData(data);
		
		this.textHandlerClass=this.getWidgetFactory().createText(handlerClassComposite, "");
		data=new FormData();
		data.top=new FormAttachment(0,2);
		data.left=new FormAttachment(handlerClassLabel,1);
		data.right=new FormAttachment(100,-100);
		this.textHandlerClass.setLayoutData(data);
		
		Button browserButton=this.getWidgetFactory().createButton(handlerClassComposite, "选择实现类...", SWT.PUSH);
		data=new FormData();
		data.top=new FormAttachment(0,0);
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
		radioButtons[0].addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(radioButtons[0].getSelection()){
					expressionComposite.setVisible(true);
					handlerClassComposite.setVisible(false);
				}
			}
		});
		radioButtons[1].addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(radioButtons[1].getSelection()){
					handlerClassComposite.setVisible(true);
					expressionComposite.setVisible(false);
				}
			}
		});
	}

	@Override
	public void refresh() {
		this.textExpression.removeModifyListener(listener);
		this.textHandlerClass.removeModifyListener(listener);
		if(getTargetProperty("type")!=null){
			DecisionType type=(DecisionType)getTargetProperty("type");
			if(type.equals(DecisionType.expression)){
				this.radioButtons[0].setSelection(true);
				String expr=(String)getTargetProperty("expression");
				this.textExpression.setText(expr!=null?expr:"");
				this.expressionComposite.setVisible(true);
			}
			if(type.equals(DecisionType.handlerClass)){
				this.radioButtons[1].setSelection(true);
				String handlerClass=(String)getTargetProperty("handlerClass");
				this.textHandlerClass.setText(handlerClass!=null?handlerClass:"");
				this.handlerClassComposite.setVisible(true);
			}
		}
		this.textExpression.addModifyListener(listener);
		this.textHandlerClass.addModifyListener(listener);
	}
}
