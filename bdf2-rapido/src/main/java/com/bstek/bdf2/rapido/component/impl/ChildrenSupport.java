/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.component.impl;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;

public class ChildrenSupport extends AbstractSupport {
	@SuppressWarnings("unused")
	private Collection<ISupport> children;
	public String getDisplayName() {
		return "Children";
	}

	public String getFullClassName() {
		return "Children";
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/Children.gif";
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
		return this.getAllAloneComponentSupports();
	}

	public boolean isAlone() {
		return false;
	}

	public void setChildren(Collection<ISupport> children) {
		this.children = children;
	}
}
