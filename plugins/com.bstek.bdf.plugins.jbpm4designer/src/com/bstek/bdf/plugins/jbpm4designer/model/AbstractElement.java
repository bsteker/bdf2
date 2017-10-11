/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 * @author Jacky
 */
public abstract class AbstractElement{
	public static final String NODE_MOVE="NODE_MOVE";
	public static final String NODE_RESIZE="NODE_RESIZE";
	public static final String NODE_ADD="NODE_ADD";
	public static final String NODE_REMOVE="NODE_REMOVE";
	private PropertyChangeSupport listeners=new PropertyChangeSupport(this);
	private int x;
	private int y;
	private int width;
	private int height;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
		this.firePropertyChange(NODE_MOVE,null,null);
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
		this.firePropertyChange(NODE_MOVE,null,null);
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
		this.firePropertyChange(NODE_RESIZE,null,null);
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
		this.firePropertyChange(NODE_RESIZE,null,null);
	}
	
	protected void firePropertyChange(String eventName,Object oldValue,Object newValue){
		this.listeners.firePropertyChange(eventName, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		this.listeners.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		this.listeners.removePropertyChangeListener(listener);
	}
}
