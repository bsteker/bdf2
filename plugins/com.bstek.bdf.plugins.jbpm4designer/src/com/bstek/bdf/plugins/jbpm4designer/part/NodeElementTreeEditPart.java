/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
/**
 * @author Jacky
 */
public class NodeElementTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener{
	public NodeElementTreeEditPart(AbstractNodeElement node){
		node.addPropertyChangeListener(this);
		this.setModel(node);
	}

	@Override
	protected String getText() {
		AbstractNodeElement node=(AbstractNodeElement)this.getModel();
		return node.getLabel();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName=evt.getPropertyName();
		if(propertyName.equals(AbstractNodeElement.LABEL_CHANGED)){
			this.refreshVisuals();
		}
	}
}
