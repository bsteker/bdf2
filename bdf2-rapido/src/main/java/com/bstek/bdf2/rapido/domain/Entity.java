/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.domain;

import java.util.Collection;

public class Entity {
	private String id;
	private String name;
	private String tableName;
	private String desc;
	private String querySql;
	private String parentId;
	private boolean recursive;
	private int pageSize;
	private Collection<Parameter> parameters;
	private Collection<EntityField> entityFields;
	private Collection<Entity> children;	
	private String packageId;
	public Collection<EntityField> getEntityFields() {
		return entityFields;
	}
	public void setEntityFields(Collection<EntityField> entityFields) {
		this.entityFields = entityFields;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Collection<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(Collection<Parameter> parameters) {
		this.parameters = parameters;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Collection<Entity> getChildren() {
		return children;
	}
	public void setChildren(Collection<Entity> children) {
		this.children = children;
	}
	public boolean isRecursive() {
		return recursive;
	}
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}
}
