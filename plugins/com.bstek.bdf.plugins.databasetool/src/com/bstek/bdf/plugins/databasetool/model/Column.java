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

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class Column extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String SIMPLE_NAME = "column";
	public static final String PK = "pk";
	public static final String FK = "fk";
	public static final String AUTOINCREMENT = "autoIncrement";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String TYPE = "type";
	public static final String LENGTH = "length";
	public static final String DECIMALLENGTH = "decimalLength";
	public static final String DEFAULTVALUE = "defaultValue";
	public static final String NOTNULL = "notNull";
	public static final String UNIQUE = "unique";
	public static final String COMMENT = "comment";
	private boolean pk;
	private boolean fk;
	private boolean autoIncrement;
	private String name;
	private String label;
	private String type;
	private String length;
	private String decimalLength;
	private Object defaultValue;
	private boolean notNull;
	private boolean unique;
	private String comment;

	private boolean canDelete = false;
	private Table table;

	public Column() {
		setId(BaseModel.generateId());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String temp = name.trim().replace(" ", "_");
		firePropertyChange(NAME, this.name, this.name = temp);
	}

	public String getLabel() {
		if (label == null)
			return getName();
		return label;
	}

	public void setLabel(String label) {
		firePropertyChange(LABEL, this.label, this.label = label);
	}

	public String getType() {
		if (type == null)
			return "";
		return type;
	}

	public void setType(String type) {
		firePropertyChange(TYPE, this.type, this.type = type);

	}

	public String getLength() {
		if (length == null)
			return "";
		return length;
	}

	public void setLength(String length) {
		firePropertyChange(LENGTH, this.length, this.length = length);
	}

	public boolean isPk() {
		return pk;
	}

	public boolean isFk() {
		return fk;
	}

	public boolean isUnique() {
		return unique;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public void setPk(boolean pk) {
		firePropertyChange(PK, this.pk, this.pk = pk);

	}

	public void setFk(boolean fk) {
		firePropertyChange(FK, this.fk, this.fk = fk);
	}

	public void setUnique(boolean unique) {
		firePropertyChange(UNIQUE, this.unique, this.unique = unique);

	}

	public String getDecimalLength() {
		if (decimalLength == null)
			return "";
		return decimalLength;
	}

	public Object getDefaultValue() {
		if (defaultValue == null)
			return "";
		return defaultValue;
	}

	public String getComment() {
		if (comment == null)
			return "";
		return comment;
	}

	public void setDecimalLength(String decimalLength) {
		firePropertyChange(DECIMALLENGTH, this.decimalLength, this.decimalLength = decimalLength);

	}

	public void setDefaultValue(Object defaultValue) {
		firePropertyChange(DEFAULTVALUE, this.defaultValue, this.defaultValue = defaultValue);

	}

	public void setComment(String comment) {
		firePropertyChange("comment", this.comment, this.comment = comment);

	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		firePropertyChange(NOTNULL, this.notNull, this.notNull = notNull);
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public Column getPropertyCopyNewColumn() {
		Column column = new Column();
		column.setId(this.getId());
		column.setFk(this.isFk());
		column.setPk(this.isPk());
		column.setAutoIncrement(this.isAutoIncrement());
		column.setLabel(this.getLabel());
		column.setName(this.getName());
		column.setType(this.getType());
		column.setLength(this.getLength());
		column.setDecimalLength(this.getDecimalLength());
		column.setDefaultValue(this.getDefaultValue());
		column.setNotNull(this.isNotNull());
		column.setUnique(this.isUnique());
		column.setComment(this.getComment());
		column.setTable(this.getTable());
		return column;
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
			Field f = Column.class.getDeclaredField((String) id);
			return f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.getPropertyValue(id);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
		TextPropertyDescriptor textPropertyDescriptor = new TextPropertyDescriptor(NAME, NAME);
		textPropertyDescriptor.setDescription("0");
		list.add(textPropertyDescriptor);

		textPropertyDescriptor = new TextPropertyDescriptor(LABEL, LABEL);
		textPropertyDescriptor.setDescription("1");
		list.add(textPropertyDescriptor);

		textPropertyDescriptor = new TextPropertyDescriptor(COMMENT, COMMENT);
		textPropertyDescriptor.setDescription("2");
		list.add(textPropertyDescriptor);

		return list.toArray(new IPropertyDescriptor[] {});
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

}
