/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.figure;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
/**
 * @author Jacky
 */
public class EndNodeFigure extends AbstractNodeFigure {

	public EndNodeFigure(AbstractNodeElement node, Image icon) {
		super(node, icon);
		this.setCornerDimensions(new Dimension(30,30));
	}
}
