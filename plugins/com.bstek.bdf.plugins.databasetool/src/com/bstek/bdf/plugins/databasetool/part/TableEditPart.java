/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.part;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.bstek.bdf.plugins.databasetool.action.ActionIdConstants;
import com.bstek.bdf.plugins.databasetool.figure.TableFigure;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.part.base.BaseGraphicalEditPart;
import com.bstek.bdf.plugins.databasetool.policy.BaseComponentEditPolicy;
import com.bstek.bdf.plugins.databasetool.policy.BaseGraphicalNodeEditPolicy;
import com.bstek.bdf.plugins.databasetool.policy.ColumnOrderedLayoutEditPolicy;
import com.bstek.bdf.plugins.databasetool.policy.TableModifyEditPolicy;

public class TableEditPart extends BaseGraphicalEditPart implements NodeEditPart {

	@Override
	protected IFigure createFigure() {
		return new TableFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new BaseGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new BaseComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ColumnOrderedLayoutEditPolicy());
		installEditPolicy(EditPolicy.NODE_ROLE, new TableModifyEditPolicy());

	}
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		Table table = (Table) getModel();
		TableFigure figure = (TableFigure) getFigure();
		((SchemaEditPart) getParent()).setLayoutConstraint(this, figure, table.getConstraints());
		figure.refreshFigure(table);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals(BaseModel.CONSTRAINTS) || prop.equals(BaseModel.MODIFY)) {
			refreshVisuals();
		} else if (prop.equals(BaseModel.SOURCE)) {
			refreshSourceConnections();
		} else if (prop.equals(BaseModel.TARGET)) {
			refreshTargetConnections();
		} else if (prop.equals(BaseModel.CHILD)) {
			refreshChildren();
		}
	}

	@Override
	protected List<Column> getModelChildren() {
		return ((Table) getModel()).getColumns();
	}

	public IFigure getContentPane() {
		TableFigure figure = (TableFigure) getFigure();
		return figure.getColumnFigure();
	}

	public void setSelected(int value) {
		super.setSelected(value);
		TableFigure tableFigure = (TableFigure) getFigure();
		if (value != EditPart.SELECTED_NONE) {
			tableFigure.setSelected(true);
		} else {
			tableFigure.setSelected(false);
		}
		bringToFront();
	}

	public void bringToFront() {
		SchemaEditPart schemaEditPart = (SchemaEditPart) getParent();
		if (schemaEditPart != null) {
			List<Table> modelChildren = schemaEditPart.getModelChildren();
			int index = modelChildren.indexOf(getModel());
			if (index != -1 && modelChildren.size() > 1) {
				Collections.swap(modelChildren, index, modelChildren.size() - 1);
				schemaEditPart.refresh();
				for (Table t : modelChildren) {
					List<Connection> cons = t.getInConnections();
					for (Connection c : cons) {
						c.fireModelBendpointEvent();
					}
				}
			}
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

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof CreateConnectionRequest) {
			CreateConnectionRequest crequest = (CreateConnectionRequest) request;
			Table table = (Table) crequest.getSourceEditPart().getModel();
			Column c = table.getFirstPkColumn();
			if (c == null) {
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "提示", "此表没有主键,请先设置主键！");
				crequest.setStartCommand(null);
			}
		}
		return new ChopboxAnchor(getFigure());
	}

	@Override
	protected List<Connection> getModelSourceConnections() {
		return ((Table) getModel()).getOutConnections();
	}

	@Override
	protected List<Connection> getModelTargetConnections() {
		return ((Table) getModel()).getInConnections();

	}

}
