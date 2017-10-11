/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class PropPropertySource implements IPropertySource {
	public PropPropertySource(PropertyElement propertyElement) {
		this.propertyElement = propertyElement;
	}

	private PropertyElement propertyElement;

	@Override
	public Object getEditableValue() {
		return propertyElement;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] { new TextPropertyDescriptor("key", "Key"),
				new TextPropertyDescriptor("value", "Value") };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if ("key".equals(id)) {
			return propertyElement.getKey();
		} else if ("value".equals(id)) {
			return propertyElement.getValue();
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if ("key".equals(id)) {
			propertyElement.setKey((String) value);
		} else if ("value".equals(id)) {
			propertyElement.setValue((String) value);
		}
	}

}
