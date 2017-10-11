/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.exporter;

import java.util.ArrayList;
import java.util.List;

public class JavaBeanFieldProperty {
	private String name;
	private String type;
	private String label;
	private String comment;
	private String columnName;
	private List<String> extraDatas = new ArrayList<String>();
	private boolean pk;
	private boolean simple;

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		if (label == null) {
			return "";
		}
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getComment() {
		if (comment == null) {
			return "";
		}
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getColumnName() {
		return columnName;
	}

	public boolean isPk() {
		return pk;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public List<String> getExtraDatas() {
		return extraDatas;
	}

	public boolean isSimple() {
		return simple;
	}

	public void setExtraDatas(List<String> extraDatas) {
		this.extraDatas = extraDatas;
	}

	public void addExtraData(String extraData) {
		extraDatas.add(extraData);
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}
}
