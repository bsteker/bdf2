/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

public abstract class PropertyEntry {
	public static final PropertyEntry[] NO_CHILDREN = {};
	private PropertyEntry parent;

	public PropertyEntry(PropertyEntry parent) {
		this.parent = parent;
	}

	public PropertyEntry getParent() {
		return parent;
	}

	public abstract PropertyEntry[] getChildren();

	public abstract void removeFromParent();
}
