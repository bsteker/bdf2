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
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.dorado.view.widget.base.tab.ControlTab;

public class ControlTabSupport  extends AbstractSupport{
	private Collection<ISupport> children;
	public String getDisplayName() {
		return ControlTab.class.getSimpleName();
	}

	@Override
	public Collection<ComponentProperty> createComponentPropertysByComponentId(String componentId) {
		Collection<ComponentProperty> properties=new ArrayList<ComponentProperty>();
		ComponentProperty caption=new ComponentProperty();
		caption.setComponentId(componentId);
		caption.setName("caption");
		caption.setValue("");
		properties.add(caption);
		
		ComponentProperty icon=new ComponentProperty();
		icon.setComponentId(componentId);
		icon.setName("icon");
		icon.setValue("");
		properties.add(icon);
		
		return properties;
	}
	
	public String getFullClassName() {
		return ControlTab.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/ControlTab.png";
	}

	public boolean isSupportEntity() {
		return false;
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
		return false;
	}
}
