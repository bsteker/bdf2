/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.policy;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import com.bstek.bdf.plugins.databasetool.command.BaseBendpointCommand;
import com.bstek.bdf.plugins.databasetool.command.CreateBendpointCommand;
import com.bstek.bdf.plugins.databasetool.command.DeleteBendpointCommand;
import com.bstek.bdf.plugins.databasetool.command.MoveBendpointCommand;
import com.bstek.bdf.plugins.databasetool.model.Connection;

public class ConnectionBendpointEditPolicy extends BendpointEditPolicy {
	private org.eclipse.draw2d.Connection conn;
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		CreateBendpointCommand com = new CreateBendpointCommand();
		Point p = request.getLocation();
		conn = getConnection();
		conn.translateToRelative(p);
		com.setLocation(p);
		Point ref1 = conn.getSourceAnchor().getReferencePoint();
		Point ref2 = conn.getTargetAnchor().getReferencePoint();

		conn.translateToRelative(ref1);
		conn.translateToRelative(ref2);

		com.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
		com.setConn((Connection) request.getSource().getModel());
		com.setIndex(request.getIndex());
		return com;
	}

	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendpointCommand com = new MoveBendpointCommand();
		Point p = request.getLocation();
		conn = getConnection();
		conn.translateToRelative(p);

		com.setLocation(p);

		Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
		Point ref2 = getConnection().getTargetAnchor().getReferencePoint();

		conn.translateToRelative(ref1);
		conn.translateToRelative(ref2);

		com.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
		com.setConn((Connection) request.getSource().getModel());
		com.setIndex(request.getIndex());
		return com;
	}

	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		BaseBendpointCommand com = new DeleteBendpointCommand();
		Point p = request.getLocation();
		com.setLocation(p);
		com.setConn((Connection) request.getSource().getModel());
		com.setIndex(request.getIndex());
		return com;
	}

}
