/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part;

import java.util.List;

import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.part.base.BaseTreeEditPart;

public class SchemaTreeEditPart extends BaseTreeEditPart {
	@Override
	protected List<Table> getModelChildren() {
		Schema model = (Schema) this.getModel();
		return model.getTables();
	}

}
