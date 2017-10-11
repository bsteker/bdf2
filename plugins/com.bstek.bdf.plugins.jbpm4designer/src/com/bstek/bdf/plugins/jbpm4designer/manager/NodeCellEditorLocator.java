/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.manager;

import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
/**
 * @author Jacky
 */
public class NodeCellEditorLocator implements CellEditorLocator {
	private AbstractNodeElement node;
	public NodeCellEditorLocator(AbstractNodeElement node){
		this.node=node;
	}
	@Override
	public void relocate(CellEditor celleditor) {
		Text text=(Text)celleditor.getControl();
		text.setBounds(node.getX(), node.getY(),node.getWidth(), node.getHeight());
	}
}
