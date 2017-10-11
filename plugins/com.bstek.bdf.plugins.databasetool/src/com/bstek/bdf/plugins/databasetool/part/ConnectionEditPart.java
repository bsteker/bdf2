/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.action.IAction;

import com.bstek.bdf.plugins.databasetool.action.ActionIdConstants;
import com.bstek.bdf.plugins.databasetool.figure.ConnectionFigure;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.ConnectionBendpoint;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.part.base.BaseConnectionEditPart;
import com.bstek.bdf.plugins.databasetool.policy.ConnectionBendpointEditPolicy;
import com.bstek.bdf.plugins.databasetool.policy.ConnectionDeleteEditPolicy;
import com.bstek.bdf.plugins.databasetool.policy.ConnectionEndpointExtEditPolicy;

public class ConnectionEditPart extends BaseConnectionEditPart {

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointExtEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionDeleteEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new ConnectionBendpointEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		return new ConnectionFigure();
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		Connection connection = (Connection) getModel();
		ConnectionFigure figure = (ConnectionFigure) getFigure();
		figure.refreshFigure(connection);
		refreshBendpoints();
	}

	protected void refreshBendpoints() {
		List<Bendpoint> modelConstraint = ((Connection) getModel()).getBendpoints();
		List<RelativeBendpoint> figureConstraint = new ArrayList<RelativeBendpoint>();
		for (int i = 0; i < modelConstraint.size(); i++) {
			ConnectionBendpoint connectionBendpoint = (ConnectionBendpoint) modelConstraint.get(i);
			RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
			rbp.setRelativeDimensions(connectionBendpoint.getFirstRelativeDimension(), connectionBendpoint.getSecondRelativeDimension());
			rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
			figureConstraint.add(rbp);
		}
		getConnectionFigure().setRoutingConstraint(figureConstraint);
	}
	
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			IAction action = findActionByActionId(ActionIdConstants.MODIFY_TABLE_RELATION_ACTION_ID);
			if (action != null && action.isEnabled()) {
				action.run();
			}
		}
		super.performRequest(req);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (Connection.BENDPOINT.equals(property)) {
			refreshBendpoints();
		}else if(property.equals(BaseModel.MODIFY)){
			refreshVisuals();
		}
	}

}
