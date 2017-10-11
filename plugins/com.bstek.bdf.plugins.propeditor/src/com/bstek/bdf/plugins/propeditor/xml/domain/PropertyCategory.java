/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.xml.domain;

import java.util.ArrayList;
import java.util.Collection;

public class PropertyCategory {
	private String name;

	public PropertyCategory(String name) {
		this.name = name;
	}

	Collection<PropertyComment> propertyComments = new ArrayList<PropertyComment>();

	public Collection<PropertyComment> getPropertyComments() {
		return propertyComments;
	}

	public void addPropertyComment(PropertyComment propertyComment) {
		propertyComments.add(propertyComment);
	}

	public void setPropertyComments(Collection<PropertyComment> propertyComments) {
		this.propertyComments = propertyComments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
