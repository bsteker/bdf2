/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.figure;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;

import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class ConnectionFigure extends PolylineConnection {
	private ConnectionEndpointLocator targetEndpointLocator = new ConnectionEndpointLocator(this, true);
	private ConnectionEndpointLocator sourceEndpointLocator = new ConnectionEndpointLocator(this, false);
	private Label manyLabel;
	private Label oneLabel;

	public ConnectionFigure() {
		super();
		setForegroundColor(ColorConstants.lightBlue);
		setConnectionRouter(new BendpointConnectionRouter());
		EmptyTriangleDecoration decoration = new EmptyTriangleDecoration();
		decoration.setLineWidth(1);
		setLineStyle(SWT.LINE_SOLID);
		setTargetDecoration(decoration);
		targetEndpointLocator.setVDistance(12);
		sourceEndpointLocator.setVDistance(12);
	}

	public void refreshFigure(BaseModel model) {
		if (oneLabel != null) {
			this.remove(oneLabel);
		}
		if (manyLabel != null) {
			this.remove(manyLabel);
		}
		Connection connection = (Connection) model;
		oneLabel = new Label("1");
		if (Connection.ONETOONE.equals(connection.getType())) {
			manyLabel = new Label("1");
		} else {
			manyLabel = new Label("1..*");
		}
		this.add(manyLabel, targetEndpointLocator);
		this.add(oneLabel, sourceEndpointLocator);

	}

	private class EmptyTriangleDecoration extends PolygonDecoration {
		private PointList decorationPointList = new PointList();

		public EmptyTriangleDecoration() {
			super();
			createPointList();
			this.setTemplate(decorationPointList);
		}

		private void createPointList() {
			decorationPointList.addPoint(0, 0);
			decorationPointList.addPoint(-2, 2);
			decorationPointList.addPoint(-2, -2);
			this.setFill(true);
			this.setBackgroundColor(ColorConstants.white);
		}

	}

}
