/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.policy;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;
/**
 * @author Jacky
 */
public class TransitionConnectionEndpointEditPolicy extends
		ConnectionEndpointEditPolicy {

	@SuppressWarnings("unchecked")
	@Override
	protected List<Figure> createSelectionHandles() {
		List<Figure> list=super.createSelectionHandles();
		List<IFigure> figures=this.getHostFigure().getChildren();
		for(IFigure figure:figures){
			if(figure instanceof Label){
				list.add(new MoveHandle((GraphicalEditPart)this.getHost(),new MoveHandleLocator(figure)));
			}
		}
		return list;
	}


}
