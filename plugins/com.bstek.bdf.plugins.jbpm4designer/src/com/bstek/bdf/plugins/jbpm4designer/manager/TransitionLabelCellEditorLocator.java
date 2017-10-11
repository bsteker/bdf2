/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.manager;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.jbpm4designer.part.TransitionLabelEditPart;
/**
 * @author Jacky
 */
public class TransitionLabelCellEditorLocator implements CellEditorLocator {
	private TransitionLabelEditPart transitionLabelEditPart;
	public TransitionLabelCellEditorLocator(TransitionLabelEditPart transitionLabelEditPart){
		this.transitionLabelEditPart=transitionLabelEditPart;
	}
	@Override
	public void relocate(CellEditor celleditor) {
		Text text=(Text)celleditor.getControl();
		Rectangle box=this.transitionLabelEditPart.getFigure().getBounds();
		text.setLocation(box.x,box.y);	
		text.setSize(box.width,box.height);
	}
}
