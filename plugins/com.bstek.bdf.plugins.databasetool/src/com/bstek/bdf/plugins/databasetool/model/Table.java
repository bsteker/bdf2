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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class Table extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String SIMPLE_NAME = "table";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String COMMENT = "comment";
	private Schema schema;
	private String name;
	private String label;
	private String comment;
	private List<Column> columns = new ArrayList<Column>(0);
	private List<Connection> inConnections = new ArrayList<Connection>();
	private List<Connection> outConnections = new ArrayList<Connection>();

	public Table() {
		setId(BaseModel.generateId());
	}

	public void addColumn(Column column) {
		columns.add(column);
		firePropertyChange(CHILD, null, column);
		adjustConstraints();

	}

	public void addColumn(int index, Column column) {
		columns.add(index, column);
		firePropertyChange(CHILD, null, column);
		adjustConstraints();
	}

	public void removeColumn(Column column) {
		columns.remove(column);
		firePropertyChange(CHILD, null, column);
		adjustConstraints();
	}

	public void addInConnection(Connection inConnection) {
		inConnections.add(inConnection);
		firePropertyChange(BaseModel.TARGET, null, inConnection);
	}

	public void removeInConnection(Connection inConnection) {
		inConnections.remove(inConnection);
		firePropertyChange(BaseModel.TARGET, null, inConnection);

	}

	public void addOutConnection(Connection outConnection) {
		outConnections.add(outConnection);
		firePropertyChange(BaseModel.SOURCE, null, outConnection);

	}

	public void removeOutConnection(Connection outConnection) {
		outConnections.remove(outConnection);
		firePropertyChange(BaseModel.SOURCE, null, outConnection);

	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		firePropertyChange(CHILD, null, this.columns = columns);
		adjustConstraints();
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String temp = name.trim().replace(" ", "_");
		firePropertyChange(NAME, this.name, this.name = temp);
		fireModelModifyEvent();
	}

	public String getLabel() {
		if (label == null) {
			return getName();
		}
		return label;
	}

	public void setLabel(String label) {
		firePropertyChange(LABEL, this.label, this.label = label);
		fireModelModifyEvent();
	}

	public List<Connection> getInConnections() {
		return inConnections;
	}

	public void setInConnections(List<Connection> inConnections) {
		this.inConnections = inConnections;
	}

	public List<Connection> getOutConnections() {
		return outConnections;
	}

	public void setOutConnections(List<Connection> outConnections) {
		this.outConnections = outConnections;
	}

	public String getTitle() {
		return label + "/" + name;
	}

	public String getComment() {
		if (comment == null) {
			return "";
		}
		return comment;
	}

	public void setComment(String comment) {
		firePropertyChange(COMMENT, this.comment, this.comment = comment);
		fireModelModifyEvent();
	}

	public void adjustConstraints() {
		int size = getColumns().size();
		Rectangle r = getConstraints().getCopy();
		Point point = r.getLocation();
		Dimension dimension = r.getSize();
		setConstraints(new Rectangle(point, new Dimension(dimension.width(), size >= 5 ? size * (i + 3) + 5 : h)));
	}

	public Column getFirstPkColumn() {
		List<Column> columns = this.getColumns();
		for (Column c : columns) {
			if (c.isPk()) {
				return c;
			}
		}
		return null;
	}

	public List<Column> getPkColumns() {
		List<Column> columns = new ArrayList<Column>();
		for (Column c : this.getColumns()) {
			if (c.isPk()) {
				columns.add(c);
			}
		}
		return columns;
	}

	public List<Column> getUniqueColumns() {
		List<Column> columns = new ArrayList<Column>();
		for (Column c : this.getColumns()) {
			if (c.isUnique()) {
				columns.add(c);
			}
		}
		return columns;
	}

	public Column getFirstFkColumn() {
		List<Column> columns = this.getColumns();
		for (Column c : columns) {
			if (c.isFk()) {
				return c;
			}
		}
		return null;
	}

	public Table getTableCopy() {
		Table table = new Table();
		table.setName(this.getName());
		table.setLabel(this.getLabel());
		table.setComment(this.getComment());
		return table;
	}

	public Connection getSelfConnection() {
		List<Connection> cons = this.getInConnections();
		for (Connection c : cons) {
			if (c.getTarget().equals(c.getSource())) {
				return c;
			}
		}
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor((String) id, this.getClass());
			Method methodSet = pd.getWriteMethod();
			methodSet.invoke(this, value);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Object getPropertyValue(Object id) {
		try {
			Field f = Table.class.getDeclaredField((String) id);
			return f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.getPropertyValue(id);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
		TextPropertyDescriptor textPropertyDescriptor = new TextPropertyDescriptor(NAME, "表名");
		textPropertyDescriptor.setDescription("0");
		list.add(textPropertyDescriptor);

		textPropertyDescriptor = new TextPropertyDescriptor(LABEL, "名称");
		textPropertyDescriptor.setDescription("1");
		list.add(textPropertyDescriptor);

		textPropertyDescriptor = new TextPropertyDescriptor(COMMENT, "备注");
		textPropertyDescriptor.setDescription("2");
		list.add(textPropertyDescriptor);

		return list.toArray(new IPropertyDescriptor[] {});
	}

}
