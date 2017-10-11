/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.dialect;

import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.dialect.impl.DB2DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.impl.MySqlDbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.impl.OracleDbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.impl.SqlServer2005DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.impl.SqlServer2008DbDialect;

public class DbDialectManager {
	private static List<DbDialect> dbDialects = new ArrayList<DbDialect>();
	static {
		dbDialects.add(new OracleDbDialect());
		dbDialects.add(new MySqlDbDialect());
		dbDialects.add(new SqlServer2005DbDialect());
		dbDialects.add(new SqlServer2008DbDialect());
		dbDialects.add(new DB2DbDialect());
	}

	public static void registerDialect(DbDialect dbDialect) {
		if (!hasRegister(dbDialect)) {
			dbDialects.add(dbDialect);
		}
	}

	public static List<DbDialect> getDialects() {
		return dbDialects;
	}

	public static List<String> getDialectTypes() {
		List<String> dialectTypes = new ArrayList<String>();
		for (DbDialect dbDialect : getDialects()) {
			dialectTypes.add(dbDialect.getDbType());
		}
		return dialectTypes;
	}

	public static DbDialect getDbDialect(String name) {
		if (name != null) {
			for (DbDialect dialect : dbDialects) {
				if (dialect.getDbType().toLowerCase().equals(name.toLowerCase())) {
					return dialect;
				}
			}
		}
		throw new RuntimeException("没有找到合适的方言!");
	}

	public static DbDialect getCurrentDbDialect() {
		String dbType = Activator.getEditor().getModel().getCurrentDbType();
		return getDbDialect(dbType);
	}

	public static ColumnType getColumnType(DbDialect dbDialect, String columnType) {
		List<ColumnType> columnTypes = dbDialect.getColumnTypes();
		for (ColumnType ct : columnTypes) {
			if (ct.getType().toLowerCase().equals(columnType.toLowerCase())) {
				return ct;
			}
		}
		return null;
	}

	private static boolean hasRegister(DbDialect dbDialect) {
		for (DbDialect dialect : dbDialects) {
			if (dialect.getDbType().toLowerCase().equals(dbDialect.getDbType().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
