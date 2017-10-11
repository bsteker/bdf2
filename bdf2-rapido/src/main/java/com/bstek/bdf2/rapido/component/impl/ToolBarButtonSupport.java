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
import com.bstek.dorado.view.widget.base.toolbar.Button;

public class ToolBarButtonSupport extends AbstractSupport{

	public String getDisplayName() {
		return "ToolBarButton";
	}

	public String getFullClassName() {
		return Button.class.getName();
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
	
	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/ToolBarButton.png";
	}
	
	public boolean isSupportEntity() {
		return false;
	}
	public boolean isSupportAction() {
		return true;
	}
	public boolean isSupportLayout() {
		return false;
	}

	public boolean isContainer() {
		return false;
	}

	public Collection<ISupport> getChildren() {
		return null;
	}

	public boolean isAlone() {
		return false;
	}
}
