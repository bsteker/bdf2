/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.SWT;

import com.bstek.bdf.plugins.databasetool.figure.SchemaFigure;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.part.base.BaseGraphicalEditPart;
import com.bstek.bdf.plugins.databasetool.policy.SchemaXYLayoutEditPolicy;

public class SchemaEditPart extends BaseGraphicalEditPart {

	@Override
	protected List<Table> getModelChildren() {
		return ((Schema) getModel()).getTables();
	}

	@Override
	protected IFigure createFigure() {
		return new SchemaFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new SchemaXYLayoutEditPolicy());
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		ConnectionLayer connectionLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));
		if ((getViewer().getControl().getStyle() & SWT.MIRRORED) == 0) {
			connectionLayer.setAntialias(SWT.ON);
		}
		Animation.run(400);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals(BaseModel.CHILD)) {
			refreshChildren();
		}
		if (prop.equals(BaseModel.MODIFY)) {
			refreshVisuals();
		}
	}

}
