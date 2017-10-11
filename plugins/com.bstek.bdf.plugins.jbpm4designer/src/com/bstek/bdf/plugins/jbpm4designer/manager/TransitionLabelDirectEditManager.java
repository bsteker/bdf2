/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.manager;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
/**
 * @author Jacky
 */
public class TransitionLabelDirectEditManager extends DirectEditManager {

	public TransitionLabelDirectEditManager(GraphicalEditPart source,
			Class<?> editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
	}

	@Override
	protected void initCellEditor() {
		Text text=(Text)this.getCellEditor().getControl();
		TransitionLabel transitionLabel=(TransitionLabel)this.getEditPart().getModel();
		text.setText(transitionLabel.getText());
	}
}
