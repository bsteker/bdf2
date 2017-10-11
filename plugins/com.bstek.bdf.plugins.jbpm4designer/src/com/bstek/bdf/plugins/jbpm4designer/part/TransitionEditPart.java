/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
import com.bstek.bdf.plugins.jbpm4designer.policy.DeleteTransitionEditPolicy;
import com.bstek.bdf.plugins.jbpm4designer.policy.TransitionBendpointEditPolicy;
import com.bstek.bdf.plugins.jbpm4designer.policy.TransitionConnectionEndpointEditPolicy;
/**
 * @author Jacky
 */
public class TransitionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener{
	public TransitionEditPart(Transition transition){
		this.setModel(transition);
		transition.addPropertyChangeListener(this);
	}

	@Override
	protected List<TransitionLabel> getModelChildren() {
		List<TransitionLabel> names=new ArrayList<TransitionLabel>();
		Transition transition=(Transition)this.getModel();
		if(transition.getTransitionLabel()!=null){
			names.add(transition.getTransitionLabel());			
		}
		return names;
	}

	@Override
	protected IFigure createFigure() {
		final PolylineConnection connection = new PolylineConnection();
		connection.setConnectionRouter(new BendpointConnectionRouter());
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(new PointList(new int[]{0, 0, -2, 2, -2, 0, -2, -2, 0, 0}));
		RGB rgb=Activator.getPreference().getTransitionColor();
		decoration.setBackgroundColor(new Color(null,rgb));
		connection.setTargetDecoration(decoration);
		connection.setAntialias(SWT.ON);
		connection.setForegroundColor(new Color(null,rgb));
		return connection;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new TransitionConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,new TransitionBendpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new DeleteTransitionEditPolicy((Transition)this.getModel()));
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String eventName=evt.getPropertyName();
		if(eventName.equals(Transition.ADD_BENDPOINT) || eventName.equals(Transition.REMOVE_BENDPOINT)){
			this.refreshVisuals();
		}
		if(eventName.equals(Transition.ADD_TRANSITION_LABEL)){
			this.refreshChildren();
		}
	}
	
	@Override
	protected void refreshVisuals() {
		List<AbsoluteBendpoint> points=new ArrayList<AbsoluteBendpoint>();
		List<Point> bendpoints=((Transition)this.getModel()).getBendpoints();
		for(Point p:bendpoints){
			points.add(new AbsoluteBendpoint(p));
		}
		getConnectionFigure().setRoutingConstraint(points);
	}
}
