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
import com.bstek.dorado.view.widget.grid.DataColumn;

public class DataColumnSupport extends AbstractSupport{

	public String getDisplayName() {
		return DataColumn.class.getSimpleName();
	}

	public String getFullClassName() {
		return DataColumn.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/DataColumn.png";
	}
	
	public boolean isSupportEntity() {
		return false;
	}
	public boolean isSupportAction() {
		return false;
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
