/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.part.base.BaseTreeEditPart;

public class TableTreeEditPart extends BaseTreeEditPart {
	@Override
	protected Image getImage() {
		return Activator.getImage(Activator.IMAGE_COLUMN_16);
	}

	@Override
	protected String getText() {
		return ((Table) getModel()).getTitle();
	}

	@Override
	protected List<Column> getModelChildren() {
		Table model = (Table) this.getModel();
		return model.getColumns();
	}
}
