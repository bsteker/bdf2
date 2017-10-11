/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.jbpm4designer.model.LockModeType;
/**
 * @author Jacky
 */
public class PropertySectionJoin extends AbstractGraphicalPropertySection {
	private CCombo comboLockMode;
	private Text textMultiplicity;
	private ModifyListener listener=new ModifyListener(){
		@Override
		public void modifyText(ModifyEvent e) {
			commandStack.execute(createPropertyChangeCommand("multiplicity", textMultiplicity.getText()));
		}
	};
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite=this.getWidgetFactory().createFlatFormComposite(parent);
		Label lockmodeLabel=this.getWidgetFactory().createLabel(composite,"锁定模式(LockMode)：");
		FormData data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(0,70);
		lockmodeLabel.setLayoutData(data);
		String modes[]=new String[LockModeType.values().length];
		comboLockMode=this.getWidgetFactory().createCCombo(composite,SWT.READ_ONLY);
		int i=0;
		for(LockModeType mode:LockModeType.values()){
			modes[i]=mode.toString();
			i++;
		}
		comboLockMode.setItems(modes);
		data=new FormData();
		data.top=new FormAttachment(0,15);
		data.left=new FormAttachment(lockmodeLabel,1);
		data.right=new FormAttachment(100,-10);
		comboLockMode.setLayoutData(data);
		
		comboLockMode.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(comboLockMode.getText()!=null && comboLockMode.getText().trim().length()>0){
					LockModeType lockMode=null;
					for(LockModeType mode:LockModeType.values()){
						if(comboLockMode.getText().equals(mode.toString())){
							lockMode=mode;
						}
					}
					commandStack.execute(createPropertyChangeCommand("lockmode", lockMode));
				}
			}
		});
		
		Label multipilicityLabel=this.getWidgetFactory().createLabel(composite, "到达的Excution数量(Multiplicity)：");
		multipilicityLabel.setToolTipText("表示有多少Execution分支到达该Join节点时，流程可以继续向下流转");
		data=new FormData();
		data.top=new FormAttachment(lockmodeLabel,10);
		data.left=new FormAttachment(0,10);
		multipilicityLabel.setLayoutData(data);
		
		textMultiplicity=this.getWidgetFactory().createText(composite, "");
		textMultiplicity.setToolTipText("表示有多少Execution分支到达该Join节点时，流程可以继续向下流转，此处输入一个数字或一个表达式");
		data=new FormData();
		data.top=new FormAttachment(lockmodeLabel,10);
		data.left=new FormAttachment(multipilicityLabel,1);
		data.right=new FormAttachment(100,-10);
		textMultiplicity.setLayoutData(data);
	}

	@Override
	public void refresh() {
		textMultiplicity.removeModifyListener(listener);
		LockModeType type=(LockModeType)getTargetProperty("lockmode"); 
		if(type!=null){
			comboLockMode.setText(type.toString());
		}
		String multiplicity=(String)getTargetProperty("multiplicity");
		if(multiplicity!=null){
			textMultiplicity.setText(multiplicity);
		}
		textMultiplicity.addModifyListener(listener);
	}
}
