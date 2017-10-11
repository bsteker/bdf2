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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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

import com.bstek.bdf.plugins.jbpm4designer.model.Assignment;
import com.bstek.bdf.plugins.jbpm4designer.model.AssignmentType;
/**
 * @author Jacky
 */
public class PropertySectionTaskAssign extends AbstractGraphicalPropertySection {
	private CCombo typeCombo;
	private Text valueText;
	private Button openTypeButton;
	private ModifyListener assignmentListener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			Assignment assignment=(Assignment)getTargetProperty("assignment");
			if(assignment==null){
				assignment=new Assignment();
			}
			AssignmentType type=null;
			for(AssignmentType at:AssignmentType.values()){
				if(at.toString().equals(typeCombo.getText())){
					type=at;
				}
			}
			assignment.setType(type);
			assignment.setValue(valueText.getText());
			commandStack.execute(createPropertyChangeCommand("assignment", assignment));
		}
	};
	@Override
	public void createControls(Composite parent,final TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		final Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		composite.setLayout(new FormLayout());
		Label typeLabel=this.getWidgetFactory().createLabel(composite, "分配方式：");
		FormData typeData=new FormData();
		typeData.top=new FormAttachment(0,15);
		typeData.left=new FormAttachment(0,10);
		typeLabel.setLayoutData(typeData);
		typeCombo=this.getWidgetFactory().createCCombo(composite, SWT.DROP_DOWN|SWT.READ_ONLY|SWT.BORDER_SOLID);
		FormData typeComboData=new FormData();
		typeComboData.top=new FormAttachment(0,15);
		typeComboData.left=new FormAttachment(typeLabel,1);
		typeComboData.right=new FormAttachment(100,-10);
		typeCombo.setLayoutData(typeComboData);
		for(AssignmentType type:AssignmentType.values()){
			typeCombo.add(type.toString());
		}
		
		Label valueLabel=this.getWidgetFactory().createLabel(composite, "具体值：");
		FormData valueLabelData=new FormData();
		valueLabelData.top=new FormAttachment(typeLabel,22);
		valueLabelData.left=new FormAttachment(0,20);
		valueLabel.setLayoutData(valueLabelData);
		this.valueText=this.getWidgetFactory().createText(composite, "");
		final FormData valueTextData=new FormData();
		valueTextData.top=new FormAttachment(typeCombo,18);
		valueTextData.left=new FormAttachment(valueLabel,1);
		valueTextData.right=new FormAttachment(100,-100);
		this.valueText.setLayoutData(valueTextData);
		
		this.openTypeButton=this.getWidgetFactory().createButton(composite, "选择实现类...",SWT.PUSH);
		FormData openTypeData=new FormData();
		openTypeData.left=new FormAttachment(valueText,1);
		openTypeData.top=new FormAttachment(typeCombo,16);
		openTypeData.right=new FormAttachment(100,-10);
		this.openTypeButton.setLayoutData(openTypeData);
		this.openTypeButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				OpenTypeSelectionDialog dialog=new OpenTypeSelectionDialog(JavaPlugin.getActiveWorkbenchShell(),false,PlatformUI.getWorkbench().getProgressService(),JavaSearchScopeFactory.getInstance().createWorkspaceScope(true),IJavaSearchConstants.TYPE);
				if(dialog.open()!=IDialogConstants.OK_ID){
					return;
				}
				Object[] results=dialog.getResult();
				if(results!=null && results.length>0){
					IType type=(IType)results[0];
					valueText.setText(type.getFullyQualifiedName());
				}
			}
			
		});
		typeCombo.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Assignment assignment=(Assignment)getTargetProperty("assignment");
				if(assignment==null){
					assignment=new Assignment();
					commandStack.execute(createPropertyChangeCommand("assignment", assignment));
				}
				for(AssignmentType type:AssignmentType.values()){
					String selectValue=typeCombo.getItem(typeCombo.getSelectionIndex());
					if(type.toString().equals(selectValue)){
						assignment.setType(type);
					}
					if(AssignmentType.assignmenthandler.toString().equals(selectValue)){
						openTypeButton.setVisible(true);
						valueTextData.right=new FormAttachment(100,-100);
					}else{
						openTypeButton.setVisible(false);						
						valueTextData.right=new FormAttachment(100,-10);
					}
					composite.layout();
				}
			}
		});
	}

	@Override
	public void refresh() {
		this.typeCombo.removeModifyListener(assignmentListener);
		this.valueText.removeModifyListener(assignmentListener);
		Assignment assignment=(Assignment)getTargetProperty("assignment");
		if(assignment!=null){
			this.typeCombo.setText(assignment.getType().toString()==null?"":assignment.getType().toString());
			this.valueText.setText(assignment.getValue()==null?"":assignment.getValue());
		}
		this.typeCombo.addModifyListener(assignmentListener);
		this.valueText.addModifyListener(assignmentListener);
	}
}
