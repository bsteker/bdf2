/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model.base;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.rmi.dgc.VMID;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class BaseModel implements IPropertySource, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String CONSTRAINTS = "CONSTRAINTS";
	public static final String CHILD = "CHILD";
	public static final String MODIFY = "MODIFY";
	public static final String SOURCE = "SOURCE";
	public static final String TARGET = "TARGET";
	public static final String ID = "id";
	
	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	public static final int w = 350;
	public static final int h = 130;
	public static final int i = 25;

	private String id;
	protected Rectangle constraints = new Rectangle(0, 0, w, h);
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue) {
		propertyChangeSupport.firePropertyChange(prop, old, newValue);
	}

	public Rectangle getConstraints() {
		return constraints;
	}

	public Point getLocation() {
		return getConstraints().getLocation();
	}

	public void setConstraints(Rectangle constraints) {
		this.constraints = constraints;
		firePropertyChange(BaseModel.CONSTRAINTS, null, null);
	}

	public void fireModelModifyEvent() {
		firePropertyChange(BaseModel.MODIFY, null, null);
	}

	public void fireModelConstraintsEvent() {
		firePropertyChange(BaseModel.CONSTRAINTS, null, null);
	}

	public void fireModelChildEvent() {
		firePropertyChange(BaseModel.CHILD, null, null);
	}

	public void fireModelSourceEvent() {
		firePropertyChange(BaseModel.SOURCE, null, null);
	}

	public void fireModelTargetEvent() {
		firePropertyChange(BaseModel.TARGET, null, null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static String generateId() {
		return new VMID().toString();
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];
		return EMPTY_ARRAY;
	}

	@Override
	public Object getPropertyValue(Object id) {
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {

	}
}
