/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.action.IAction;

import com.bstek.bdf.plugins.databasetool.action.ActionIdConstants;
import com.bstek.bdf.plugins.databasetool.figure.ColumnFigure;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.part.base.BaseGraphicalEditPart;
import com.bstek.bdf.plugins.databasetool.policy.BaseComponentEditPolicy;
import com.bstek.bdf.plugins.databasetool.policy.ColumnModifyEditPolicy;

public class ColumnEditPart extends BaseGraphicalEditPart {

	@Override
	protected IFigure createFigure() {
		return new ColumnFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new BaseComponentEditPolicy());
		installEditPolicy(EditPolicy.NODE_ROLE, new ColumnModifyEditPolicy());
	}

	protected void refreshVisuals() {
		super.refreshVisuals();
		ColumnFigure figure = (ColumnFigure) getFigure();
		Column model = (Column) getModel();
		figure.refreshFigure(model);
	}

	public void setSelected(int value) {
		super.setSelected(value);
		ColumnFigure columnFigure = (ColumnFigure) getFigure();
		if (value != EditPart.SELECTED_NONE) {
			columnFigure.setSelected(true);
		} else {
			columnFigure.setSelected(false);
		}
		TableEditPart tableEditPart=(TableEditPart)getParent();
		if(tableEditPart!=null){
			tableEditPart.bringToFront();
		}
	}

	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			IAction action = findActionByActionId(ActionIdConstants.MODIFY_ACTION_ID);
			if (action != null && action.isEnabled()) {
				action.run();
			}
		}
		super.performRequest(req);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(BaseModel.MODIFY) || evt.getPropertyName().equals(BaseModel.CONSTRAINTS)) {
			refreshVisuals();
		}
	}

}
