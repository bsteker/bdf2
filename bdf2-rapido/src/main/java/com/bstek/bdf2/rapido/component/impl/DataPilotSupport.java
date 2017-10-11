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
import com.bstek.dorado.view.widget.datacontrol.DataPilot;

public class DataPilotSupport extends AbstractSupport{
	public String getDisplayName() {
		return DataPilot.class.getSimpleName();
	}

	public String getFullClassName() {
		return DataPilot.class.getName();
	}

	@Override
	public Collection<ComponentProperty> createComponentPropertysByComponentId(String componentId) {
		Collection<ComponentProperty> properties=new ArrayList<ComponentProperty>();
		ComponentProperty itemCodes=new ComponentProperty();
		itemCodes.setComponentId(componentId);
		itemCodes.setName("itemCodes");
		itemCodes.setValue("pages,goto,+,-");
		properties.add(itemCodes);		
		return properties;
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/DataPilot.gif";
	}

	public Collection<ISupport> getChildren() {
		return null;
	}

	public boolean isSupportEntity() {
		return true;
	}

	public boolean isSupportLayout() {
		return false;
	}

	public boolean isSupportAction() {
		return false;
	}

	public boolean isContainer() {
		return false;
	}

	public boolean isAlone() {
		return true;
	}


}
