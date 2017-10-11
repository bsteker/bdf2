/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.action;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.part.NodeElementEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.ProcessDefinitionEditPart;
/**
 * @author Jacky
 */
public class CutAction extends CopyAction {
	public CutAction(IWorkbenchPart part) {
		super(part);
		this.setId(ActionFactory.CUT.getId());
	}

	@Override
	public void run() {
		super.run();
		for(Object obj:super.getSelectedObjects()){
			if(obj instanceof NodeElementEditPart){
				NodeElementEditPart editpart=(NodeElementEditPart)obj;
				AbstractNodeElement node=editpart.getModel();
				ProcessDefinition pd=(ProcessDefinition)((ProcessDefinitionEditPart)editpart.getParent()).getModel();
				pd.removeNode(node);
			}
		}
	}
}
