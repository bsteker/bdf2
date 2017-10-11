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
import com.bstek.dorado.view.widget.tree.BindingConfig;

public class BindingConfigSupport extends AbstractSupport {
	private Collection<ISupport> children;
	public String getDisplayName() {
		return BindingConfig.class.getSimpleName();
	}

	public String getFullClassName() {
		return BindingConfig.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/BindingConfig.gif";
	}

	public Collection<ISupport> getChildren() {
		return children;
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
		return true;
	}

	public boolean isAlone() {
		return false;
	}

	public void setChildren(Collection<ISupport> children) {
		this.children = children;
	}
}
