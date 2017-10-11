/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class SchemaFilter extends AbstractFilter {
	@Override
	public boolean support(BaseModel element) {
		if (element instanceof Schema) {
			return true;
		}
		return false;
	}
	
}