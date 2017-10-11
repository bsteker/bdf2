/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public abstract class BaseTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

	@Override
	public void activate() {
		super.activate();
		((BaseModel) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((BaseModel) getModel()).removePropertyChangeListener(this);
	}

	public EditPart getEditPartForChild(Object child) {
		return (EditPart) getViewer().getEditPartRegistry().get(child);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refresh();
	}
}
