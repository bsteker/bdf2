/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

public class PropertyChangeCommand extends Command {

	@Override
	public boolean canUndo() {
		return false;
	}
	private IPropertySource model;
	private String propertyId;
	private Object newValue;
	private Object oldValue;

	public PropertyChangeCommand(IPropertySource model, String propertyId, Object newValue, Object oldValue) {
		super();
		this.model = model;
		this.propertyId = propertyId;
		this.newValue = newValue;
		this.oldValue = oldValue;
	}

	public boolean canExecute() {
		if (model == null || propertyId == null || newValue == null) {
			return false;
		} else {
			return true;
		}
	}

	public void execute() {
		if (oldValue == null) {
			oldValue = model.getPropertyValue(propertyId);
		}
		model.setPropertyValue(propertyId, newValue);
	}

	public void undo() {
		model.setPropertyValue(propertyId, oldValue);
	}

}
