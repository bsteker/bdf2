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
import com.bstek.dorado.view.widget.base.Dialog;

public class DialogSupport extends AbstractSupport{
	private Collection<ISupport> children;
	public String getDisplayName() {
		return Dialog.class.getSimpleName();
	}

	public String getFullClassName() {
		return Dialog.class.getName();
	}
	
	
	@Override
	public Collection<ComponentProperty> createComponentPropertysByComponentId(String componentId) {
		Collection<ComponentProperty> properties=new ArrayList<ComponentProperty>();
		ComponentProperty caption=new ComponentProperty();
		caption.setComponentId(componentId);
		caption.setName("caption");
		caption.setValue("");
		properties.add(caption);
		
		ComponentProperty center=new ComponentProperty();
		center.setComponentId(componentId);
		center.setName("center");
		center.setValue("true");
		properties.add(center);
		
		ComponentProperty modal=new ComponentProperty();
		modal.setComponentId(componentId);
		modal.setName("modal");
		modal.setValue("true");
		properties.add(modal);
		
		ComponentProperty width=new ComponentProperty();
		width.setComponentId(componentId);
		width.setName("width");
		width.setValue("600");
		properties.add(width);
		
		ComponentProperty height=new ComponentProperty();
		height.setComponentId(componentId);
		height.setName("height");
		height.setValue("400");
		properties.add(height);
		
		ComponentProperty closeable=new ComponentProperty();
		closeable.setComponentId(componentId);
		closeable.setName("closeable");
		closeable.setValue("false");
		properties.add(closeable);
		
		return properties;
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/Dialog.gif";
	}

	public boolean isSupportEntity() {
		return false;
	}
	public boolean isSupportAction() {
		return false;
	}
	public boolean isSupportLayout() {
		return true;
	}

	public boolean isContainer() {
		return true;
	}

	public Collection<ISupport> getChildren() {
		return children;
	}

	public boolean isAlone() {
		return true;
	}

	public void setChildren(Collection<ISupport> children) {
		this.children = children;
	}
}
