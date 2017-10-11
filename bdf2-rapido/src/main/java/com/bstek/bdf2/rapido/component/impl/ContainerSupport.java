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
import com.bstek.dorado.view.widget.Container;

public class ContainerSupport extends AbstractSupport {
	public String getDisplayName() {
		return Container.class.getSimpleName();
	}

	public String getFullClassName() {
		return Container.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/Container.gif";
	}

	public Collection<ISupport> getChildren() {
		return this.getAllAloneComponentSupports();
	}

	public boolean isSupportEntity() {
		return false;
	}

	public boolean isSupportLayout() {
		return true;
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
}
