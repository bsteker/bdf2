/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class ConnectionBendpoint implements java.io.Serializable, Bendpoint {
	private static final long serialVersionUID = 1L;
	public static final String SIMPLE_NAME = "bendpoint";
	public static final String DX1 = "dx1";
	public static final String DY1 = "dy1";
	public static final String DX2 = "dx2";
	public static final String DY2 = "dy2";

	private float weight = 0.5f;
	private Dimension d1, d2;

	public ConnectionBendpoint() {
	}

	public Dimension getFirstRelativeDimension() {
		return d1;
	}

	public Point getLocation() {
		return null;
	}

	public Dimension getSecondRelativeDimension() {
		return d2;
	}

	public float getWeight() {
		return weight;
	}

	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	public void setWeight(float w) {
		weight = w;
	}

}
