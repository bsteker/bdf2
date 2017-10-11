/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

import org.w3c.dom.Node;
/**
 * @author Jacky
 */
public class StartNode extends AbstractNodeElement{
	public StartNode(String label) {
		super(label);
	}

	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
	}

	@Override
	public String nodeName() {
		return "start";
	}
}
