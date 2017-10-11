/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.part;

import org.eclipse.draw2d.IFigure;

import com.bstek.bdf.plugins.jbpm4designer.figure.TaskNodeFigure;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
/**
 * @author Jacky
 */
public class TaskNodeEditPart extends NodeElementEditPart {

	public TaskNodeEditPart(AbstractNodeElement node) {
		super(node);
	}
	@Override
	protected IFigure createFigure() {
		return new TaskNodeFigure(this.getModel(),this.getNodeImage());
	}
}
