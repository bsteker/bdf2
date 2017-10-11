/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.dialect.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.dialect.DbType;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class OracleDbDialect extends CommonDialect {
	
	public OracleDbDialect() {
		this.setColumnTypeRegister(new OracleColumnTypeRegister());
	}

	@Override
	public String getDbType() {
		return DbType.Oracle.name();
	}

	@Override
	public String getJdbcDriverName() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	public String getUrlTemplate() {
		return "jdbc:oracle:thin:@<SERVER NAME>:<PORT>:<DB NAME>";
	}

	@Override
	public int getDefaultPort() {
		return 1521;
	}

	@Override
	public String getDefaultColumnType() {
		return ColumnTypeConstants.VARCHAR2;
	}

	@Override
	public boolean supportAppendCommentDefination() {
		return true;
	}

	@Override
	public boolean supportAppendConstraintRestrict() {
		return false;
	}

	@Override
	public String getDropPrimaryKeyDefination(Table table, String constraintName) {
		String tableName = table.getName();
		String defination = "ALTER TABLE " + tableName + " DROP PRIMARY KEY CASCADE";
		return defination;
	}

	@Override
	public String getUpdateColumnDefination(Table table, Column column) {
		StringBuilder sb = new StringBuilder();
		String tableName = table.getName();
		sb.append("ALTER TABLE  " + tableName + " MODIFY  ");
		sb.append(column.getName() + " ");
		sb.append(getColumnTypeDefination(column));
		String value = (String) column.getDefaultValue();
		if (value.length() > 0) {
			sb.append(" DEFAULT '" + value + "'");
		}
		return sb.toString();
	}

	@Override
	public String getUpdateColumnIsNullDefination(Table table, Column column) {
		StringBuilder sb = new StringBuilder();
		String tableName = table.getName();
		sb.append("ALTER TABLE  " + tableName + " MODIFY  ");
		sb.append(column.getName() + " ");
		sb.append(getColumnTypeDefination(column));
		sb.append(" NULL");
		return sb.toString();
	}

	@Override
	public String getUpdateColumnNotNullDefination(Table table, Column column) {
		StringBuilder sb = new StringBuilder();
		String tableName = table.getName();
		sb.append("ALTER TABLE  " + tableName + " MODIFY  ");
		sb.append(column.getName() + " ");
		sb.append(getColumnTypeDefination(column));
		sb.append(" NOT NULL");
		return sb.toString();
	}

	@Override
	public List<String> getColumnUniqueIndexNames(java.sql.Connection connection, String tableName, String columnName) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			stmt = connection.createStatement();
			String schema = DbJdbcUtils.getDefaultTableSchema(connection);
			rs = stmt
					.executeQuery("select cu.table_name,constraint_type,column_name,au.constraint_name from user_cons_columns cu, user_constraints au where cu.constraint_name=au.constraint_name and au.owner='"
							+ schema
							+ "' and au.table_name='"
							+ tableName.toUpperCase()
							+ "' and au.constraint_type='U' and column_name='"
							+ columnName + "'");
			while (rs.next()) {
				String name = rs.getString("CONSTRAINT_NAME");
				list.add(name);
			}
		} finally {
			DbJdbcUtils.closeResultSet(rs);
			DbJdbcUtils.closeStatement(stmt);
		}
		return list;
	}
}
