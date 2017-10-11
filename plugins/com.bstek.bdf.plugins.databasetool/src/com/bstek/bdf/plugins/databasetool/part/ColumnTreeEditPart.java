/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part;

import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.part.base.BaseTreeEditPart;

public class ColumnTreeEditPart extends BaseTreeEditPart {
	@Override
	protected Image getImage() {
		Column column = (Column) getModel();
		if (column.isPk()) {
			return Activator.getImage(Activator.IMAGE_PK);
		} else if (column.isFk()) {
			return Activator.getImage(Activator.IMAGE_FK);
		}
		return super.getImage();
	}

	@Override
	protected String getText() {
		Column column = (Column) getModel();
		return column.getLabel() + "/" + column.getName() + "  " + column.getType();
	}

}
