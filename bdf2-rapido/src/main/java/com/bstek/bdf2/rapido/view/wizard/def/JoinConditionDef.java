/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.wizard.def;

public class JoinConditionDef {
	private TableDef sourceTable;
	private String sourceField;
	private String joinField;
	private TableDef joinTable;
	public TableDef getSourceTable() {
		return sourceTable;
	}
	public void setSourceTable(TableDef sourceTable) {
		this.sourceTable = sourceTable;
	}
	public TableDef getJoinTable() {
		return joinTable;
	}
	public void setJoinTable(TableDef joinTable) {
		this.joinTable = joinTable;
	}
	
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
	public String getJoinField() {
		return joinField;
	}
	public void setJoinField(String joinField) {
		this.joinField = joinField;
	}
	public String buildJoinSQL(){
		return this.joinTable.getAlias()+"."+this.joinField+"="+this.sourceTable.getAlias()+"."+this.sourceField;
	}
}
