/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.dialect;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;

public interface DbDialect {

	public String getDbType();

	public String getJdbcDriverName();

	public String getUrlTemplate();

	public String getUrl(String serverName, int port, String dataBaseName);

	public DbDriverMetaData getDbDriverMetaData();

	public int getDefaultPort();

	public List<ColumnType> getColumnTypes();

	public ColumnTypeRegister getColumnTypeRegister();

	public String getDefaultColumnType();

	public boolean supportAutoIncrement();

	public boolean supportAutoIncrement(String type);

	public String getAutoIncrmentDefinatin();

	public String getAutoIncrmentDefinatin(Column column);

	public List<String> getColumnUniqueIndexNames(java.sql.Connection connection, String tableName, String columnName) throws SQLException;

	public String getTablePrimaryIndexName(java.sql.Connection connection, String tableName) throws SQLException;

	public String generateDDLSQL(Schema schema);

	public String getDropPrimaryKeyDefination(Table table, String constraintName);

	public String getAlertPrimaryKeyDefination(Table table, String constraintName);

	public String getDropUniqueDefination(String tableName, String constraintName);

	public String getAddUniqueDefination(Table table, String constraintName);

	public String getAddColumnDefination(Table table, Column column);

	public String getUpdateColumnDefination(Table table, Column column);

	public String getUpdateColumnIsNullDefination(Table table, Column column);

	public String getUpdateColumnNotNullDefination(Table table, Column column);

	public String getDeleteColumnDefination(Table table, String columnName);

	public String getColumnDefination(Column column, boolean comment);

	public String getDropTableDefination(Table table);

	public String getDeleteTableRecordDefination(Table table);

	public String getCreateTableDefination(Table table, boolean comment);

	public String getAddForeignKeyConstraintDefination(Connection connection);

	public String getDropForeignKeyConstraintDefination(String tableName, String constraintName);

	public boolean supportAppendCommentDefination();

	public String getCommentDefination(Schema schema);

	public Map<String, String> getAllTableComments(java.sql.Connection connection) throws SQLException;

	public Map<String, String> getTableColumnComments(java.sql.Connection connection, String tableName) throws SQLException;

}
