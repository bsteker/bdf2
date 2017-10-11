/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.part.ColumnTreeEditPart;
import com.bstek.bdf.plugins.databasetool.part.SchemaTreeEditPart;
import com.bstek.bdf.plugins.databasetool.part.TableTreeEditPart;

public class DbToolTreeEditPartFactory implements EditPartFactory {
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof Schema) {
			editPart = new SchemaTreeEditPart();
		} else if (model instanceof Table) {
			editPart = new TableTreeEditPart();
		} else if (model instanceof Column) {
			editPart = new ColumnTreeEditPart();
		}
		if(editPart!=null){			
			editPart.setModel(model);
		}
		return editPart;
	}
}