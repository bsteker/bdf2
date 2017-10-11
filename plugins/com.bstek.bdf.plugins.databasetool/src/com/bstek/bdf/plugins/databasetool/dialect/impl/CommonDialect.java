/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.dialect.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeRegister;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeUtils;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.utils.StringUtils;

public abstract class CommonDialect implements DbDialect {

	private DbDriverMetaData dbDriverMetaData = new DbDriverMetaData();
	private ColumnTypeRegister columnTypeRegister;

	@Override
	public String getUrl(String serverName, int port, String dataBaseName) {
		String temp = serverName.replaceAll("\\\\", "\\\\\\\\");
		String url = getUrlTemplate().replaceAll("<SERVER NAME>", temp);
		url = url.replaceAll("<PORT>", String.valueOf(port));
		temp = dataBaseName.replaceAll("\\\\", "\\\\\\\\");
		url = url.replaceAll("<DB NAME>", temp);
		return url;
	}

	@Override
	public boolean supportAutoIncrement() {
		return false;
	}

	@Override
	public boolean supportAutoIncrement(String type) {
		if (type != null && supportAutoIncrement()) {
			List<String> list = ColumnTypeUtils.getIntegerColumnType();
			if (list.contains(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> getColumnUniqueIndexNames(java.sql.Connection connection, String tableName, String columnName) throws SQLException {
		return null;
	}

	@Override
	public List<ColumnType> getColumnTypes() {
		return columnTypeRegister.getAllColumnTypes();
	}

	@Override
	public DbDriverMetaData getDbDriverMetaData() {
		return dbDriverMetaData;
	}

	@Override
	public String getDefaultColumnType() {
		return ColumnTypeConstants.VARCHAR;
	}

	@Override
	public String getDropTableDefination(Table table) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP TABLE ");
		sb.append(table.getName());
		return sb.toString();
	}

	@Override
	public String getColumnDefination(Column column, boolean comment) {
		StringBuffer sb = new StringBuffer();
		if (comment) {
			sb.append("-- " + column.getLabel());
			if (StringUtils.hasText(column.getComment())) {
				sb.append(": ");
				if (column.getComment().indexOf("\n") == -1) {
					sb.append(column.getComment());
				} else {
					String[] commentArray = column.getComment().split("\n");
					sb.append(commentArray[0]);
					sb.append(tab());
					for (int i = 1; i < commentArray.length; i++) {
						sb.append("-- " + commentArray[i]);
						if (i != commentArray.length - 1) {
							sb.append(tab());
						}
					}
				}
			}
			sb.append(line());
			sb.append(tab());
		}
		sb.append(column.getName() + " ");
		String type = column.getType();
		DbDialect dbDialect = DbDialectManager.getDbDialect(column.getTable().getSchema().getCurrentDbType());
		ColumnType columnType = DbDialectManager.getColumnType(dbDialect, type);
		String length = column.getLength();
		String decimal = column.getDecimalLength();
		if (columnType.isLength() && columnType.isDecimal() && length.length() > 0 && decimal.length() > 0) {
			type += "(" + length + "," + decimal + ")";
		} else if (columnType.isLength() && length.length() > 0 && decimal.length() == 0) {
			type += "(" + length + ")";
		}
		sb.append(type);
		if (column.isAutoIncrement() && supportAutoIncrement()) {
			sb.append(" ");
			sb.append(getAutoIncrmentDefinatin().trim());
		} else {
			sb.append(" ");
			sb.append(getColumnConstraintDefination(column).trim());
		}
		return sb.toString();
	}

	public String getColumnDefination(Column column) {
		return getColumnDefination(column, true);

	}

	public String getColumnConstraintDefination(Column column) {
		StringBuffer sb = new StringBuffer();
		String value = (String) column.getDefaultValue();
		if (value.length() > 0) {
			sb.append(" DEFAULT '" + value + "'");
		}
		if (column.isNotNull()) {
			sb.append(" NOT NULL");
		}
		return sb.toString();
	}

	@Override
	public String getAutoIncrmentDefinatin() {
		return "";
	}

	@Override
	public String getAutoIncrmentDefinatin(Column column) {
		return "";
	}

	@Override
	public String getCreateTableDefination(Table table, boolean comment) {
		StringBuffer sb = new StringBuffer();
		if (comment) {
			sb.append("-- " + table.getLabel());
			if (StringUtils.hasText(table.getComment().trim())) {
				sb.append(": ");
				if (table.getComment().indexOf("\n") == -1) {
					sb.append(table.getComment());
				} else {
					String[] commentArray = table.getComment().split("\n");
					sb.append(commentArray[0]);
					for (int i = 1; i < commentArray.length; i++) {
						sb.append("-- " + commentArray[i]);
					}
				}
			}
			sb.append(line());
		}
		sb.append("CREATE TABLE ");
		sb.append(table.getName());
		sb.append(line());
		sb.append("(");
		sb.append(line());
		List<Column> columns = table.getColumns();
		int i = 1;
		String pks = getTablePks(table);
		String uniques = getTableUniques(table);
		for (Column column : columns) {
			sb.append(tab());
			String csb = getColumnDefination(column, comment);
			sb.append(csb);
			if (i < columns.size() || (columns.size() == i && (pks.length() > 0 || uniques.length() > 0))) {
				sb.append(",");
			}
			sb.append(line());
			i++;
		}
		if (pks.length() > 0) {
			sb.append(tab());
			sb.append("PRIMARY KEY (" + pks + ")");
			if (uniques.length() > 0) {
				sb.append(",");
			}
			sb.append(line());
		}

		if (uniques.length() > 0) {
			sb.append(tab());
			sb.append("UNIQUE (" + uniques + ")");
			sb.append(line());
		}
		sb.append(")");
		sb.append(";");
		return sb.toString();
	}

	public String getCreateTableDefination(Table table) {
		return getCreateTableDefination(table, true);
	}

	@Override
	public String getAddForeignKeyConstraintDefination(Connection connection) {
		String constraintName = connection.getConstraintName().trim();
		if (!StringUtils.hasText(constraintName)) {
			constraintName = "FK_"+connection.getSource().getName() + "_" + connection.getPkColumn().getName() + "_" + connection.getTarget().getName()
					+ "_" + connection.getFkColumn().getName();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ");
		sb.append(connection.getTarget().getName() + " ");
		sb.append("ADD CONSTRAINT ");
		sb.append(constraintName + " ");
		sb.append("FOREIGN KEY (");
		sb.append(connection.getFkColumn().getName());
		sb.append(") ");
		sb.append("REFERENCES ");
		sb.append(connection.getSource().getName());
		sb.append("(");
		sb.append(connection.getPkColumn().getName());
		sb.append(") ");
		if (supportAppendConstraintRestrict()) {
			sb.append(getAppendConstraintRestrict());
		}
		return sb.toString();
	}

	@Override
	public String getDropForeignKeyConstraintDefination(String tableName, String constraintName) {
		String defination = " ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName;
		return defination;
	}

	@Override
	public String getUpdateColumnIsNullDefination(Table table, Column column) {
		return "";
	}

	@Override
	public String getUpdateColumnNotNullDefination(Table table, Column column) {
		return "";
	}

	public boolean supportAppendConstraintRestrict() {
		return true;
	}

	public String getAppendConstraintRestrict() {
		return "ON UPDATE RESTRICT ON DELETE RESTRICT";
	}

	@Override
	public boolean supportAppendCommentDefination() {
		return false;
	}

	@Override
	public String getCommentDefination(Schema schema) {
		StringBuffer sb = new StringBuffer();
		List<Table> tables = schema.getTables();
		for (Table table : tables) {
			String comment = table.getComment();
			if (StringUtils.hasText(comment)) {
				sb.append("COMMENT ON TABLE " + table.getName() + " IS '");
				sb.append(table.getComment());
				sb.append("'");
				sb.append(";");
				sb.append(line());
			}
			List<Column> columns = table.getColumns();
			for (Column column : columns) {
				comment = column.getComment();
				if (StringUtils.hasText(comment)) {
					sb.append("COMMENT ON COLUMN " + table.getName() + "." + column.getName() + " IS '");
					sb.append(column.getComment());
					sb.append("'");
					sb.append(";");
					sb.append(line());
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String generateDDLSQL(Schema schema) {
		StringBuffer sql = new StringBuffer();
		List<Table> tables = schema.getTables();
		sql.append("--==================================================");
		sql.append(line());
		sql.append("-- DBMS Name:   " + schema.getCurrentDbType() + "");
		sql.append(line());
		sql.append("-- Created Date:  " + getCurrentDate() + "");
		sql.append(line());
		sql.append("-- Created User:  " + getUserName() + "");
		sql.append(line());
		sql.append("--==================================================");
		sql.append(line(2));
		sql.append("/* Drop Tables */");
		sql.append(line(2));
		for (Table table : tables) {
			String tsql = getDropTableDefination(table);
			sql.append(tsql);
			sql.append(";");
			sql.append(line());
		}
		sql.append(line());
		sql.append("/* Create Tables */");
		sql.append(line(2));
		for (Table table : tables) {
			if (!table.getColumns().isEmpty()) {
				sql.append(getCreateTableDefination(table));
				sql.append(line(2));
			}
		}
		sql.append(line());
		sql.append("/* Create Foreign Keys */");
		sql.append(line(2));
		for (Table table : tables) {
			List<Connection> cons = table.getOutConnections();
			for (Connection c : cons) {
				String csql = getAddForeignKeyConstraintDefination(c);
				sql.append(csql);
				sql.append(";");
				sql.append(line());
			}
		}
		if (supportAppendCommentDefination()) {
			String comment = getCommentDefination(schema);
			if (comment != null && comment.length() > 0) {
				sql.append(line());
				sql.append("/* Create Comment */");
				sql.append(line(2));
				sql.append(comment);
				sql.append(line(2));
			}
		}
		return sql.toString();
	}

	@Override
	public String getDropPrimaryKeyDefination(Table table, String constraintName) {
		String tableName = table.getName();
		String defination = " ALTER TABLE " + tableName + " DROP PRIMARY KEY";
		return defination;
	}

	@Override
	public String getDropUniqueDefination(String tableName, String constraintName) {
		String defination = " ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName;
		return defination;
	}

	@Override
	public ColumnTypeRegister getColumnTypeRegister() {
		return columnTypeRegister;
	}

	public void setColumnTypeRegister(ColumnTypeRegister columnTypeRegister) {
		this.columnTypeRegister = columnTypeRegister;
	}

	@Override
	public String getAddUniqueDefination(Table table, String constraintName) {
		String uniqueColumns = getTableUniques(table);
		if (uniqueColumns.trim().length() > 0) {
			return " ALTER TABLE " + table.getName() + " ADD CONSTRAINT UQ_" + table.getName() + "_UNIQUE UNIQUE(" + uniqueColumns + ")";
		}
		return "";
	}

	@Override
	public String getAlertPrimaryKeyDefination(Table table, String constraintName) {
		StringBuilder sb = new StringBuilder();
		String pks = getTablePks(table);
		String tableName = table.getName();
		if (pks.length() > 0) {
			sb.append(" ALTER TABLE ");
			sb.append(tableName);
			sb.append(" ADD CONSTRAINT PK_");
			if (StringUtils.hasText(constraintName)) {
				sb.append(constraintName);
			} else {
				sb.append("PK_" + tableName);
			}
			sb.append(" PRIMARY KEY (" + pks + ")");
		}
		return sb.toString();
	}

	@Override
	public String getTablePrimaryIndexName(java.sql.Connection connection, String tableName) throws SQLException {
		return "";
	}

	@Override
	public String getAddColumnDefination(Table table, Column column) {
		StringBuilder sb = new StringBuilder();
		String tableName = table.getName();
		sb.append(" ALTER TABLE " + tableName + " ADD ");
		String columnDefination = getColumnDefination(column, false);
		sb.append(columnDefination);
		if (column.isPk()) {
			sb.append(";");
			sb.append(getDropPrimaryKeyDefination(table, null));
			sb.append(";");
			sb.append(getAlertPrimaryKeyDefination(table, null));
		}

		return sb.toString();
	}

	@Override
	public String getUpdateColumnDefination(Table table, Column column) {
		StringBuilder sb = new StringBuilder();
		String tableName = table.getName();
		sb.append(" ALTER TABLE  " + tableName + " MODIFY  ");
		String columnDefination = getColumnDefination(column, false);
		sb.append(columnDefination);
		return sb.toString();
	}

	@Override
	public String getDeleteColumnDefination(Table table, String columnName) {
		String tableName = table.getName();
		String defination = " ALTER TABLE " + tableName + "  DROP COLUMN " + columnName;
		return defination;
	}

	@Override
	public String getDeleteTableRecordDefination(Table table) {
		String tableName = table.getName();
		String defination = " DELETE FROM " + tableName;
		return defination;
	}

	@Override
	public Map<String, String> getAllTableComments(java.sql.Connection connection) throws SQLException {
		return new HashMap<String, String>();
	}

	@Override
	public Map<String, String> getTableColumnComments(java.sql.Connection connection, String tableName) throws SQLException {
		return new HashMap<String, String>();
	}

	public String getTablePks(Table table) {
		String s = "";
		List<Column> columns = table.getPkColumns();
		int i = 1;
		for (Column column : columns) {
			s += column.getName();
			if (i != columns.size()) {
				s += ",";
			}
			i++;
		}
		return s;
	}

	public String getTableUniques(Table table) {
		String s = "";
		List<Column> columns = table.getUniqueColumns();
		int i = 1;
		for (Column column : columns) {
			s += column.getName();
			if (i != columns.size()) {
				s += ",";
			}
			i++;
		}
		return s;
	}

	public String getColumnTypeDefination(Column column) {
		String type = column.getType();
		String length = column.getLength();
		String decimal = column.getDecimalLength();
		if (length.length() > 0 && decimal.length() > 0) {
			type += "(" + length + "," + decimal + ")";
		} else if (length.length() > 0 && decimal.length() == 0) {
			type += "(" + length + ")";
		}
		return type;
	}

	public String line() {
		String s = System.getProperty("line.separator");
		return s;
	}

	public String line(int i) {
		String s = "";
		for (int j = 1; j <= i; j++) {
			s += System.getProperty("line.separator");
		}
		return s;
	}

	public String tab() {
		return "\t";
	}

	public String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public String getUserName() {
		return System.getProperty("user.name");
	}

}
