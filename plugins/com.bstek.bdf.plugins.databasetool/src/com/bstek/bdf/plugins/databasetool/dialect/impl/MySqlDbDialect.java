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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.dialect.DbType;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class MySqlDbDialect extends CommonDialect {

	public MySqlDbDialect() {
		this.setColumnTypeRegister(new MySqlColumnTypeRegister());
	}

	@Override
	public String getDbType() {
		return DbType.MySql.name();
	}

	@Override
	public String getJdbcDriverName() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public String getUrlTemplate() {
		return "jdbc:mysql://<SERVER NAME>:<PORT>/<DB NAME>";
	}

	@Override
	public int getDefaultPort() {
		return 3306;
	}

	@Override
	public String getDefaultColumnType() {
		return ColumnTypeConstants.VARCHAR;
	}

	@Override
	public String getColumnConstraintDefination(Column column) {
		StringBuffer sb = new StringBuffer();
		String value = (String) column.getDefaultValue();
		if (value.length() > 0) {
			sb.append(" DEFAULT '" + value + "'");
		}
		if (column.isNotNull()) {
			sb.append(" NOT NULL");
		}
		if (!column.isPk() && column.isUnique()) {
			sb.append(" UNIQUE");
		}
		if (column.getComment().length() > 0) {
			sb.append(" COMMENT '");
			sb.append(column.getComment());
			sb.append("'");
		}
		return sb.toString();
	}

	@Override
	public boolean supportAutoIncrement() {
		return true;
	}

	@Override
	public String getAutoIncrmentDefinatin() {
		return "NOT NULL AUTO_INCREMENT";
	}

	@Override
	public String getCreateTableDefination(Table table, boolean comment) {
		StringBuffer sb = new StringBuffer();
		if (comment) {
			sb.append("-- " + table.getLabel() + ":" + table.getComment());
			sb.append(line());
		}
		sb.append("CREATE TABLE ");
		sb.append(table.getName());
		sb.append(line());
		sb.append("(");
		sb.append(line());
		List<Column> columns = table.getColumns();
		int i = 1;
		for (Column column : columns) {
			sb.append(tab());
			String csb = getColumnDefination(column, comment);
			sb.append(csb);
			if (i < columns.size() || (columns.size() == i && table.getFirstPkColumn() != null)) {
				sb.append(",");
			}
			sb.append(line());
			i++;
		}
		String pks = getTablePks(table);
		if (pks.length() > 0) {
			sb.append(tab());
			sb.append("PRIMARY KEY (" + getTablePks(table) + ")");
			sb.append(line());
		}
		sb.append(") ");
		sb.append("ENGINE = InnoDB DEFAULT CHARSET=utf8");
		if (table.getComment().length() > 0) {
			sb.append(" COMMENT = '");
			sb.append(table.getComment());
			sb.append("'");
		}
		sb.append(";");
		return sb.toString();
	}

	@Override
	public String getDropUniqueDefination(String tableName, String uniqueIndexName) {
		String defination = " ALTER TABLE " + tableName + " DROP INDEX " + uniqueIndexName;
		return defination;
	}

	@Override
	public String getDropForeignKeyConstraintDefination(String tableName, String constraintName) {
		String defination = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + constraintName;
		return defination;
	}

	@Override
	public Map<String, String> getAllTableComments(java.sql.Connection connection) throws SQLException {
		Map<String, String> tableComments = new Hashtable<String, String>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SHOW TABLE STATUS");
			while (rs.next()) {
				String name = rs.getString("Name");
				String comment = rs.getString("Comment");
				tableComments.put(name, comment);
			}
		} finally {
			DbJdbcUtils.closeResultSet(rs);
			DbJdbcUtils.closeStatement(stmt);
		}
		return tableComments;
	}

	@Override
	public List<String> getColumnUniqueIndexNames(java.sql.Connection connection, String tableName, String columnName) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SHOW INDEX FROM " + tableName);
			while (rs.next()) {
				String unique = rs.getString("Non_unique");
				String name = rs.getString("Column_name");
				if (unique.equals("0") && name.toLowerCase().equals(columnName.toLowerCase())) {
					String keyName = rs.getString("Key_name");
					if (!keyName.equals("PRIMARY")) {
						list.add(keyName);
					}
				}
			}
		} finally {
			DbJdbcUtils.closeResultSet(rs);
			DbJdbcUtils.closeStatement(stmt);
		}
		return list;
	}

	/**
	 * 直接查询表结构获取信息，如set、enum的特殊类型
	 * @param connection
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public String getSpecilColumnLength(java.sql.Connection connection, String tableName, String columnName) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SHOW COLUMNS FROM " + tableName + " like '" + columnName + "'");
			while (rs.next()) {
				String type = rs.getString("Type");
				if (type.indexOf("(") != -1 && type.endsWith(")")) {
					return type.substring(type.indexOf("(")+1, type.length() - 1);
				}
			}
		} finally {
			DbJdbcUtils.closeResultSet(rs);
			DbJdbcUtils.closeStatement(stmt);
		}
		return null;
	}
}
