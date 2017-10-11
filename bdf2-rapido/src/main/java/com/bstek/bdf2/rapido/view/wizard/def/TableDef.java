/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.wizard.def;

import java.util.Collection;

public class TableDef {
	private String name;
	private boolean master;
	private String primaryKeys;
	private JoinType joinType;
	private Collection<JoinConditionDef> joinConditions;
	private Collection<TableDef> children;
	private Collection<ColumnDef> columns;
	private Collection<ParameterDef> parameters;
	private Collection<OrderDef> orders;
	private String alias;
	private TableDef parent;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
	public String getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(String primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public Collection<TableDef> getChildren() {
		return children;
	}
	public void setChildren(Collection<TableDef> children) {
		this.children = children;
	}
	public Collection<ColumnDef> getColumns() {
		return columns;
	}
	public void setColumns(Collection<ColumnDef> columns) {
		this.columns = columns;
	}
	
	public Collection<JoinConditionDef> getJoinConditions() {
		return joinConditions;
	}
	public void setJoinConditions(Collection<JoinConditionDef> joinConditions) {
		this.joinConditions = joinConditions;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public JoinType getJoinType() {
		return joinType;
	}
	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}
	public Collection<ParameterDef> getParameters() {
		return parameters;
	}
	public void setParameters(Collection<ParameterDef> parameters) {
		this.parameters = parameters;
	}
	public Collection<OrderDef> getOrders() {
		return orders;
	}
	public void setOrders(Collection<OrderDef> orders) {
		this.orders = orders;
	}
	public TableDef getParent() {
		return parent;
	}
	public void setParent(TableDef parent) {
		this.parent = parent;
	}
}
