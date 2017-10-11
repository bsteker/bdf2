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
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.part.ColumnEditPart;
import com.bstek.bdf.plugins.databasetool.part.ConnectionEditPart;
import com.bstek.bdf.plugins.databasetool.part.SchemaEditPart;
import com.bstek.bdf.plugins.databasetool.part.TableEditPart;

public class DbToolEditPartFactory implements EditPartFactory {
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof Schema) {
			editPart = new SchemaEditPart();
		} else if (model instanceof Table) {
			editPart = new TableEditPart();
		} else if (model instanceof Column) {
			editPart = new ColumnEditPart();
		} else if (model instanceof Connection) {
			editPart = new ConnectionEditPart();
		}
		if(editPart!=null){			
			editPart.setModel(model);
		}
		return editPart;
	}

}
