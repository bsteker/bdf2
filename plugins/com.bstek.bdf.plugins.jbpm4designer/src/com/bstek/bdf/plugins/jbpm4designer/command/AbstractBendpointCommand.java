/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
/**
 * @author Jacky
 */
public class AbstractBendpointCommand extends Command{
	protected Transition transition;
	protected int index;
	protected Point point;
	protected int oldIndex;
	protected Point oldPoint;
	public Transition getTransition() {
		return transition;
	}
	public void setTransition(Transition transition) {
		this.transition = transition;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
}
