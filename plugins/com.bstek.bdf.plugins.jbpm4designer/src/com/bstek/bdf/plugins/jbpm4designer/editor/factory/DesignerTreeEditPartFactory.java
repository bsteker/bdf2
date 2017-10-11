/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.part.NodeElementTreeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.ProcessDefinitionTreeEditPart;
/**
 * @author Jacky
 */
public class DesignerTreeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part=null;
		if(model instanceof ProcessDefinition){
			part=new ProcessDefinitionTreeEditPart((ProcessDefinition)model);
		}
		if(model instanceof AbstractNodeElement){
			part=new NodeElementTreeEditPart((AbstractNodeElement)model);
		}
		return part;
	}

}
