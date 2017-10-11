/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

/**
 * @author Jacky
 */
public class StateNode extends AbstractNodeElement {
	public StateNode(String label) {
		super(label);
	}

	@Override
	public String nodeName() {
		return "state";
	}
}
