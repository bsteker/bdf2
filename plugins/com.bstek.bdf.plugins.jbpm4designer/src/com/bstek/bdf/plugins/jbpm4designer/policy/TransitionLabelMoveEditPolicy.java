/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.bstek.bdf.plugins.jbpm4designer.command.MoveTransitionLabelCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
/**
 * @author Jacky
 */
public class TransitionLabelMoveEditPolicy extends NonResizableEditPolicy{
	@Override
	protected Command getMoveCommand(ChangeBoundsRequest request) {
		TransitionLabel transitionLabel=(TransitionLabel)(this.getHost().getModel());
		Point p=request.getMoveDelta();
		IFigure parentFigure=((GraphicalEditPart)this.getHost().getParent()).getFigure();
		return new MoveTransitionLabelCommand(transitionLabel,parentFigure,p);
	}
}
