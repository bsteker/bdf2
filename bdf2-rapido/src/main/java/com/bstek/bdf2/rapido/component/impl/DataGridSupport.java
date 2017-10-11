/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.component.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.dorado.view.widget.grid.DataColumn;
import com.bstek.dorado.view.widget.grid.DataGrid;

public class DataGridSupport  extends AbstractSupport{
	private Collection<ISupport> children;
	
	@Override
	public Collection<ComponentInfo> createChildrenByEntity(Entity entity) {
		Collection<ComponentInfo> components=new ArrayList<ComponentInfo>();
		if(entity.getEntityFields()!=null){
			int order=0;
			for(EntityField field:entity.getEntityFields()){
				order++;
				ComponentInfo component=new ComponentInfo();
				component.setName(field.getName());
				component.setClassName(DataColumn.class.getName());
				component.setOrder(order);
				components.add(component);
			}
		}
		return components;
	}
	
	@Override
	public Collection<ComponentProperty> createComponentPropertysByComponentId(String componentId) {
		Collection<ComponentProperty> properties=new ArrayList<ComponentProperty>();
		ComponentProperty stretchColumnsMode=new ComponentProperty();
		stretchColumnsMode.setComponentId(componentId);
		stretchColumnsMode.setName("stretchColumnsMode");
		stretchColumnsMode.setValue("off");
		properties.add(stretchColumnsMode);
		
		ComponentProperty readOnly=new ComponentProperty();
		readOnly.setComponentId(componentId);
		readOnly.setName("readOnly");
		readOnly.setValue("true");
		properties.add(readOnly);
		return properties;
	}



	public String getDisplayName() {
		return DataGrid.class.getSimpleName();
	}

	public String getFullClassName() {
		return DataGrid.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/DataGrid.png";
	}

	public boolean isSupportEntity() {
		return true;
	}
	public boolean isSupportAction() {
		return false;
	}
	public boolean isSupportLayout() {
		return false;
	}

	public boolean isContainer() {
		return true;
	}
	
	public Collection<ISupport> getChildren() {
		return children;
	}

	public void setChildren(Collection<ISupport> children) {
		this.children = children;
	}

	public boolean isAlone() {
		return true;
	}
}
