/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;

import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public abstract class AbstractFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (object instanceof EditPart && ((EditPart) object).getModel() instanceof BaseModel) {
			BaseModel element = (BaseModel) ((EditPart) object).getModel();
			if (element != null) {
				if (support(element)) {
					return true;
				}
			}
		}
		return false;
	}

	public abstract boolean support(BaseModel element);

}
