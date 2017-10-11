/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.action;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.bstek.bdf.plugins.jbpm4designer.part.NodeElementEditPart;
/**
 * @author Jacky
 */
public class CopyAction extends SelectionAction {

	public CopyAction(IWorkbenchPart part) {
		super(part);
		this.setId(ActionFactory.COPY.getId());
	}

	@Override
	public void run() {
		Clipboard.getDefault().setContents(super.getSelectedObjects());
	}

	@Override
	protected boolean calculateEnabled() {
		boolean enable=false;
		for(Object obj:super.getSelectedObjects()){
			if(obj instanceof NodeElementEditPart){
				enable=true;
				break;
			}
		}
		return enable;
	}
}
