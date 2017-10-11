/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part.base;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.DbToolGefEditor;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public abstract class BaseConnectionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener {

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

	public IAction findActionByActionId(String actionId) {
		IEditorPart editor = getEditorPart();
		ActionRegistry actionRegistry = (ActionRegistry) editor.getAdapter(ActionRegistry.class);
		IAction action = actionRegistry.getAction(actionId);
		return action;
	}

	public DbToolGefEditor getEditorPart() {
		return Activator.getEditor();
	}

}
