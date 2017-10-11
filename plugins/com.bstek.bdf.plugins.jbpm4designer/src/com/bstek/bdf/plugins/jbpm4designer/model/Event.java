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
public class Event{
	private EventType type;
	private String listenerClass;
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public String getListenerClass() {
		return listenerClass;
	}
	public void setListenerClass(String listenerClass) {
		this.listenerClass = listenerClass;
	}
}
