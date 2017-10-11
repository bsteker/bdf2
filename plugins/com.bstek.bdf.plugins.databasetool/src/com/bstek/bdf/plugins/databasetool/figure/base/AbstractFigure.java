/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.figure.base;

import org.eclipse.draw2d.Figure;

import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public abstract class AbstractFigure extends Figure {
	public AbstractFigure() {
		super();
	
	}
	public abstract void refreshFigure(BaseModel model);

}
