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
import com.bstek.dorado.view.widget.form.trigger.CustomDropDown;

public class CustomDropDownSupport extends AbstractSupport {
	private Collection<ISupport> children;
	public String getDisplayName() {
		return CustomDropDown.class.getSimpleName();
	}

	public String getFullClassName() {
		return CustomDropDown.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/CustomDropDown.gif";
	}

	public Collection<ISupport> getChildren() {
		return children;
	}

	public boolean isSupportEntity() {
		return false;
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
		return true;
	}

	public void setChildren(Collection<ISupport> children) {
		this.children = children;
	}
}
