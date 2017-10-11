/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Dimension;

import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class Connection extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String SIMPLE_NAME = "connection";
	public static final String TYPE = "type";
	public static final String BENDPOINT = "BENDPOINT";
	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	public static final String PKCOLUMN = "pk";
	public static final String FKCOLUMN = "fk";
	public static final String ONETOONE = "一对一";
	public static final String ONETOMANY = "一对多";
	public static final String CONSTRAINTNAME = "constraintName";
	private Table source;
	private Table target;
	private Column pkColumn;
	private Column fkColumn;
	private String type = ONETOMANY;
	private String constraintName;
	private List<Bendpoint> bendpoints = new ArrayList<Bendpoint>();

	public Connection() {
	}

	public Connection(Table source, Table target, Column pkColumn, Column fkColumn, String constraintName) {
		this.source = source;
		this.target = target;
		this.pkColumn = pkColumn;
		this.fkColumn = fkColumn;
		this.constraintName = constraintName;
	}

	public void disconnect() {
		if (source.getOutConnections().contains(this)) {
			source.removeOutConnection(this);
		}
		if (target.getInConnections().contains(this)) {
			target.removeInConnection(this);

		}
	}

	public void reconnect() {
		if (!source.getOutConnections().contains(this)) {
			source.addOutConnection(this);
		}
		if (!target.getInConnections().contains(this)) {
			target.addInConnection(this);
		}
	}

	public void connect() {
		reconnect();
	}

	public void insertBendpoint(int index, Bendpoint point) {
		getBendpoints().add(index, point);
		firePropertyChange(BENDPOINT, null, null);
	}

	public void removeBendpoint(int index) {
		getBendpoints().remove(index);
		firePropertyChange(BENDPOINT, null, null);
	}

	public void setBendpoint(int index, Bendpoint point) {
		getBendpoints().set(index, point);
		firePropertyChange(BENDPOINT, null, null);
	}

	public void setSelfConnectionBendpoint() {
		bendpoints = new ArrayList<Bendpoint>();
		Dimension size = source.getConstraints().getSize();
		int height = size.height();
		int width = size.width();
		int c = 30;
		int w = 0;
		int h = -(c + height / 2);
		Dimension dimension = new Dimension(w, h);
		ConnectionBendpoint cbp = new ConnectionBendpoint();
		cbp.setRelativeDimensions(dimension, dimension);
		insertBendpoint(0, cbp);
		w = c + width / 2;
		dimension = new Dimension(w, h);
		cbp = new ConnectionBendpoint();
		cbp.setRelativeDimensions(dimension, dimension);
		insertBendpoint(1, cbp);
		h = 0;
		dimension = new Dimension(w, h);
		cbp = new ConnectionBendpoint();
		cbp.setRelativeDimensions(dimension, dimension);
		insertBendpoint(2, cbp);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor((String) id, this.getClass());
			Method methodSet = pd.getWriteMethod();
			methodSet.invoke(this, value);
			fireModelModifyEvent();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Object getPropertyValue(Object id) {
		try {
			Field f = this.getClass().getDeclaredField((String) id);
			return f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getPropertyValue(id);
	}

	public void fireModelBendpointEvent() {
		firePropertyChange(BENDPOINT, null, null);
	}

	public List<Bendpoint> getBendpoints() {
		return bendpoints;
	}

	public Table getSource() {
		return source;
	}

	public void setSource(Table source) {
		this.source = source;
	}

	public Table getTarget() {
		return target;
	}

	public void setTarget(Table target) {
		this.target = target;
	}

	public Column getFkColumn() {
		return fkColumn;
	}

	public void setFkColumn(Column fkColumn) {
		this.fkColumn = fkColumn;
	}

	public Column getPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(Column pkColumn) {
		this.pkColumn = pkColumn;
	}

	public void setBendpoints(List<Bendpoint> bendpoints) {
		this.bendpoints = bendpoints;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConstraintName() {
		if (constraintName == null) {
			return "";
		}
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

}
