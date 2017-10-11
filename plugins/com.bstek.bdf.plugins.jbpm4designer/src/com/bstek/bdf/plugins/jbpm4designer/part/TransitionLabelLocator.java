/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.part;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
/**
 * @author Jacky
 */
public class TransitionLabelLocator implements Locator{
	private String text;
	private Point offset;
	private Polyline polyline;
	public TransitionLabelLocator(String text, Point offset, Polyline polyline) {
		this.text = text;
		this.offset = offset;
		this.polyline = polyline;
	}

	public void relocate(IFigure figure) {
		Dimension minimum = FigureUtilities.getTextExtents(text, figure.getFont());
		figure.setSize(minimum);
		Point location = polyline.getPoints().getMidpoint();
		Point offsetCopy = offset.getCopy();
		offsetCopy.translate(location);
		figure.setLocation(offsetCopy);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Point getOffset() {
		return offset;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}
}
