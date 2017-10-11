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

public class JavaBeanProperty {
	private String packageName;
	private String className;
	private String tableName;
	private String comment;
	private String content;
	private List<JavaBeanFieldProperty> fields = new ArrayList<JavaBeanFieldProperty>();

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<JavaBeanFieldProperty> getFields() {
		return fields;
	}

	public void setFields(List<JavaBeanFieldProperty> fields) {
		this.fields = fields;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void addField(JavaBeanFieldProperty field) {
		fields.add(field);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
