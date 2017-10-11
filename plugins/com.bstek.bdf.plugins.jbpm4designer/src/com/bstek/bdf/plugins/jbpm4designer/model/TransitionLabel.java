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

import org.eclipse.draw2d.geometry.Point;
/**
 * @author Jacky
 */
public class TransitionLabel{
	public static final String TRANSITION_LABEL_CHANGED="TRANSITION_LABEL_MOVE";
	private String text;
	private Point offset=new Point(5,-10);
	private PropertyChangeSupport listeners=new PropertyChangeSupport(this);
	public TransitionLabel(String text){
		this.text=text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		this.firePropertyChange(TRANSITION_LABEL_CHANGED, null, null);
	}

	public void setOffset(Point offset){
		this.offset=offset;
		this.firePropertyChange(TRANSITION_LABEL_CHANGED, null, null);
	}
	public Point getOffset() {
		return offset;
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
		this.listeners.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		this.listeners.removePropertyChangeListener(listener);
	}
	public void firePropertyChange(String propertyName,Object oldValue,Object newValue){
		this.listeners.firePropertyChange(propertyName, oldValue, newValue);
	}
}
