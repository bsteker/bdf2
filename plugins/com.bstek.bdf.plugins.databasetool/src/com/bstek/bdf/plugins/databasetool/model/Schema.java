/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model;

import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.databasetool.dialect.DbType;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class Schema extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String SIMPLE_NAME = "schema";
	public static final String DB_TYPE = "dbtype";
	private List<Table> tables = new ArrayList<Table>(0);
	private String currentDbType;

	public void addTable(Table table) {
		tables.add(table);
		firePropertyChange(CHILD, null, table);
	}

	public void removeTable(Table table) {
		tables.remove(table);
		firePropertyChange(CHILD, null, table);
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public String getCurrentDbType() {
		return currentDbType;
	}

	public void setCurrentDbType(String currentDbType) {
		this.currentDbType = currentDbType;
	}

	public boolean validateDbType() {
		if (this.getCurrentDbType() == null || this.getCurrentDbType().equals("")) {
			return false;
		}
		DbType[] dbTypes = DbType.values();
		for (DbType dbType : dbTypes) {
			if (dbType.name().toLowerCase().equals(this.getCurrentDbType().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
