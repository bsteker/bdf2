/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.dialect.impl;

import java.sql.CallableStatement;
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
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.utils.StringUtils;

public class SqlServer2008DbDialect extends CommonDialect {

	public SqlServer2008DbDialect() {
		this.setColumnTypeRegister(new SqlServer2008ColumnTypeRegister());
	}

	@Override
	public String getDbType() {
		return DbType.SqlServer2008.name();
	}

	@Override
	public String getJdbcDriverName() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	public String getUrlTemplate() {
		return "jdbc:sqlserver://<SERVER NAME>:<PORT>;database=<DB NAME>";
	}

	@Override
	public int getDefaultPort() {
		return 1433;
	}

	@Override
	public String getDefaultColumnType() {
		return ColumnTypeConstants.VARCHAR;
	}

	@Override
	public boolean supportAutoIncrement() {
		return true;
	}

	@Override
	public boolean supportAppendConstraintRestrict() {
		return false;
	}

	@Override
	public String getAutoIncrmentDefinatin() {
		return "IDENTITY NOT NULL";
	}

	@Override
	public String getUpdateColumnDefination(Table table, Column column) {
		StringBuilder sb = new StringBuilder();
		String tableName = table.getName();
		sb.append(" ALTER TABLE  " + tableName + " ALTER COLUMN ");
		sb.append(column.getName() + " ");
		sb.append(getColumnTypeDefination(column));
		if (column.isNotNull()) {
			sb.append(" NOT NULL");
		} else {
			sb.append(" NULL");
		}
		return sb.toString();
	}

	@Override
	public String getTablePrimaryIndexName(java.sql.Connection connection, String tableName) throws SQLException {
		String pkName = null;
		CallableStatement call = connection.prepareCall("{call sp_pkeys(?)}");
		call.setString(1, tableName);
		ResultSet rs = call.executeQuery();
		try {
			while (rs.next()) {
				pkName = rs.getString("PK_NAME");
			}
		} finally {
			DbJdbcUtils.closeResultSet(rs);
		}
		return pkName;
	}

	@Override
	public String getDropPrimaryKeyDefination(Table table, String constraintName) {
		String defination = " ALTER TABLE " + table.getName() + " DROP CONSTRAINT " + constraintName;
		return defination;
	}

	@Override
	public boolean supportAppendCommentDefination() {
		return true;
	}

	@Override
	public String getCommentDefination(Schema schema) {
		StringBuffer sb = new StringBuffer();
		List<Table> tables = schema.getTables();
		for (Table table : tables) {
			String comment = table.getComment();
			if (StringUtils.hasText(comment)) {
				String template = "EXECUTE sp_addextendedproperty N'MS_Description', N'%s', N'user', N'dbo', N'table', N'%s', NULL, NULL";
				sb.append(String.format(template, table.getComment(), table.getName()));
				sb.append(";");
				sb.append(line());
			}
			List<Column> columns = table.getColumns();
			for (Column column : columns) {
				comment = column.getComment();
				if (StringUtils.hasText(comment)) {
					String template = "EXECUTE sp_addextendedproperty N'MS_Description', N'%s', N'user', N'dbo', N'table', N'%s', N'COLUMN', N'%s'";
					sb.append(String.format(template, column.getComment(), column.getTable().getName(), column.getName()));
					sb.append(";");
					sb.append(line());
				}
			}
		}
		return sb.toString();
	}

	@Override
	public List<String> getColumnUniqueIndexNames(java.sql.Connection connection, String tableName, String columnName) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			stmt = connection.createStatement();
			String sql = "SELECT IDX.Name FROM sys.indexes IDX INNER JOIN sys.index_columns IDXC ON IDX.[object_id]=IDXC.[object_id] AND IDX.index_id=IDXC.index_id "
					+ "LEFT JOIN sys.key_constraints KC ON IDX.[object_id]=KC.[parent_object_id] AND IDX.index_id=KC.unique_index_id INNER JOIN sys.objects O ON O.[object_id]=IDX.[object_id] INNER JOIN sys.columns C ON"
					+ " O.[object_id]=C.[object_id] AND O.type='U' AND O.is_ms_shipped=0 AND "
					+ "IDXC.Column_id=C.Column_id"
					+ " where O.Name='"
					+ tableName + "' and  IDX.is_primary_key=0 and IDX.is_unique=1 and C.Name='" + columnName + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("Name");
				list.add(name);
			}
		} finally {
			DbJdbcUtils.closeResultSet(rs);
			DbJdbcUtils.closeStatement(stmt);
		}
		return list;
	}

	@Override
	public Map<String, String> getAllTableComments(java.sql.Connection connection) throws SQLException {
		Map<String, String> tableComments = new Hashtable<String, String>();
		/*
		 * Statement stmt = null; ResultSet rs = null; String sql =
		 * "SELECT t.name,p.value FROM sys.tables t right JOIN sys.columns c ON c.object_id = t.object_id right JOIN sys.extended_properties p ON p.major_id = c.object_id  where p.minor_id='0'  AND c.column_id='1'"
		 * ; try { stmt = connection.createStatement(); rs =
		 * stmt.executeQuery(sql); while (rs.next()) { String name =
		 * rs.getString("name"); String comment = rs.getString("value");
		 * tableComments.put(name, comment); } } finally {
		 * DbJdbcUtils.closeResultSet(rs); DbJdbcUtils.closeStatement(stmt); }
		 */
		return tableComments;
	}

	@Override
	public Map<String, String> getTableColumnComments(java.sql.Connection connection, String tableName) throws SQLException {
		Map<String, String> map = new Hashtable<String, String>();
		/*
		 * Statement stmt = null; ResultSet rs = null; String template =
		 * "SELECT t.name AS table_name,c.name AS column_name,p.value AS column_description FROM sys.tables"
		 * +
		 * " t INNER JOIN sys.columns c ON c.object_id = t.object_id LEFT JOIN sys.extended_properties p"
		 * +
		 * " ON p.major_id = c.object_id AND p.minor_id = c.column_id WHERE t.name = '%s'"
		 * ; String sql = String.format(template, tableName); try { stmt =
		 * connection.createStatement(); rs = stmt.executeQuery(sql); while
		 * (rs.next()) { String name = rs.getString("column_name"); String
		 * comment = rs.getString("column_description"); map.put(name, comment);
		 * } } finally { DbJdbcUtils.closeResultSet(rs);
		 * DbJdbcUtils.closeStatement(stmt); }
		 */
		return map;
	}
}
