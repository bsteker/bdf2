/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.component;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.rapido.component.property.PropertySelectDialog;
import com.bstek.bdf2.rapido.component.property.PropertySupport;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.Entity;

public abstract class AbstractSupport implements ISupport,ApplicationContextAware{
	private Collection<ISupport> allAloneComponentSupports;
	private Collection<PropertySupport> supports=new ArrayList<PropertySupport>();
	protected Collection<ISupport> getAllAloneComponentSupports(){
		return allAloneComponentSupports;
	}
	
	public Collection<ComponentInfo> createChildrenByEntity(Entity entity) {
		return null;
	}
	
	public Collection<PropertySupport> getPropertySupports() {
		return supports;
	}

	public Collection<ComponentProperty> createComponentPropertysByComponentId(String componentId){
		return null;
	}
	
	protected PropertySupport createIconPropertySupport(){
		PropertySupport support=new PropertySupport();
		support.setShow(true);
		support.setPropertyName("icon");
		PropertySelectDialog propertySelect=new PropertySelectDialog();
		propertySelect.setDialogId("$dialogSelectIcon");
		propertySelect.setDialogImportUrl("com.bstek.bdf.rapid.view.component.assist.IconSelect#$dialogSelectIcon");
		support.setPropertySelectDialog(propertySelect);
		return support;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		allAloneComponentSupports=new ArrayList<ISupport>();
		for(ISupport support:applicationContext.getBeansOfType(ISupport.class).values()){
			if(support.isAlone()){
				allAloneComponentSupports.add(support);
			}
		}
		supports.add(this.createIconPropertySupport());
		PropertySupport dataSetSupport=new PropertySupport();
		dataSetSupport.setPropertyName("dataSet");
		dataSetSupport.setShow(false);
		supports.add(dataSetSupport);
	}

	public void setAllAloneComponentSupports(
			Collection<ISupport> allAloneComponentSupports) {
		this.allAloneComponentSupports = allAloneComponentSupports;
	}
}
