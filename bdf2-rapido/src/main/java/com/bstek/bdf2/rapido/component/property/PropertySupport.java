/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.component.property;

public class PropertySupport {
	private String propertyName;
	private boolean show;
	private PropertySelectDialog propertySelectDialog;
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public PropertySelectDialog getPropertySelectDialog() {
		return propertySelectDialog;
	}
	public void setPropertySelectDialog(PropertySelectDialog propertySelectDialog) {
		this.propertySelectDialog = propertySelectDialog;
	}

}
