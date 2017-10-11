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
import java.util.List;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;

public class AutoFormSupport extends AbstractSupport {
	private Collection<ISupport> children;
	
	@Override
	public Collection<ComponentInfo> createChildrenByEntity(Entity entity) {
		Collection<ComponentInfo> components=new ArrayList<ComponentInfo>();
		if(entity.getEntityFields()!=null){
			List<EntityField> fields=(List<EntityField>)entity.getEntityFields();
			for(int i=fields.size()-1;i>-1;i--){
				EntityField field=fields.get(i);
				ComponentInfo component=new ComponentInfo();
				component.setName(field.getName());
				component.setClassName(AutoFormElement.class.getName());
				components.add(component);
			}
		}
		return components;
	}

	public String getDisplayName() {
		return AutoForm.class.getSimpleName();
	}

	public String getFullClassName() {
		return AutoForm.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/AutoForm.png";
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

	public boolean isSupportEntity() {
		return true;
	}

	public boolean isSupportLayout() {
		return false;
	}

	public boolean isContainer() {
		return true;
	}

	public boolean isSupportAction() {
		return false;
	}
}
