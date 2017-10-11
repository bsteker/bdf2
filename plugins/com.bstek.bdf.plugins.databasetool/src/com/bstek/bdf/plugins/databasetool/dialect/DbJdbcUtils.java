package com.bstek.bdf.plugins.databasetool.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.utils.ClassUtils;
import com.bstek.bdf.plugins.databasetool.utils.StringUtils;

public class DbJdbcUtils {

	public static Connection getConnection(String dbType, String url, String username, String password) throws Exception {
		String driverLocation = Activator.getDefault().getPreferenceStore().getString(dbType);
		DbDialect dbDialect = DbDialectManager.getDbDialect(dbType);
		Driver driver = (Driver) ClassUtils.loadJdbcDriverClass(dbDialect.getJdbcDriverName(), driverLocation).newInstance();
		Properties info = new Properties();
		info.put("remarksReporting", "true");
		info.setProperty("user", username);
		info.setProperty("password", password);
		Connection con = driver.connect(url, info);
		return con;
	}

	public static Map<String, List<String>> getAllSchemaTables(Connection conn) throws Exception {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		ResultSet rsCatalog = databaseMetaData.getCatalogs();
		ResultSet rsTable = null;
		try {
			while (rsCatalog.next()) {
				String catalog = rsCatalog.getString(1);
				List<String> tables = new ArrayList<String>();
				String[] t = { "TABLE" };
				rsTable = databaseMetaData.getTables(catalog, null, "%", t);
				while (rsTable.next()) {
					tables.add(rsTable.getString(3));
				}
				map.put(catalog, tables);
			}
		} finally {
			DbJdbcUtils.closeResultSet(rsCatalog);
			DbJdbcUtils.closeResultSet(rsTable);
		}
		return map;
	}

	public static Map<String, List<String>> getDefaultSchemaTables(Connection conn) throws Exception {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		List<String> tables = new ArrayList<String>();
		String[] t = { "TABLE" };
		String url = databaseMetaData.getURL();
		String currentDb = null;
		if (url.toLowerCase().contains("oracle")) {
			currentDb = databaseMetaData.getUserName();
		}
		ResultSet rsTable = databaseMetaData.getTables(null, currentDb, "%", t);
		String schema = null;
		try {
			while (rsTable.next()) {
				String tableName = rsTable.getString(3);
				if (!(tableName.startsWith("BIN$") && tableName.endsWith("==$0"))) {
					tables.add(rsTable.getString(3));
					if (rsTable.getString(1) != null) {
						schema = rsTable.getString(1);
					} else if (rsTable.getString(2) != null) {
						schema = rsTable.getString(2);
					}
				}
			}
		} finally {
			DbJdbcUtils.closeResultSet(rsTable);
		}
		if (schema != null) {
			map.put(schema, tables);
		} else {
			map.put(conn.getCatalog() != null ? conn.getCatalog() : currentDb, tables);
		}
		return map;
	}

	public static Map<Integer, Object> getTable(Connection conn, String tableSchema, String tableName) throws Exception {
		Map<Integer, Object> table = new HashMap<Integer, Object>();
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		if (tableSchema == null) {
			tableSchema = getDefaultTableSchema(conn);
		}
		String[] t = { "TABLE" };
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getTables(null, tableSchema, tableName.toUpperCase(), t);
			while (rs.next()) {
				int i = 1;
				while (i <= 10) {
					Object obj = null;
					try {
						obj = rs.getObject(i);
					} catch (SQLException e) {
						break;
					}
					table.put(i, obj);
					i++;
				}
			}
			return table;
		} finally {
			DbJdbcUtils.closeResultSet(rs);
		}
	}

	public static List<Map<Integer, Object>> getTableColumn(Connection conn, String tableSchema, String tableName) throws Exception {
		List<Map<Integer, Object>> columnInfos = new ArrayList<Map<Integer, Object>>();
		Map<Integer, Object> columnInfo;
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		if (tableSchema == null) {
			tableSchema = getDefaultTableSchema(conn);
		}
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getColumns(null, tableSchema, tableName.toUpperCase(), "%");
			while (rs.next()) {
				columnInfo = new HashMap<Integer, Object>();
				int i = 1;
				while (i <= 23) {
					Object obj = null;
					try {
						obj = rs.getObject(i);
					} catch (SQLException e) {
						break;
					}
					columnInfo.put(i, obj);
					i++;
				}
				columnInfos.add(columnInfo);
			}
			return columnInfos;
		} finally {
			DbJdbcUtils.closeResultSet(rs);
		}
	}

	public static String getDefaultTableSchema(Connection conn) throws SQLException {
		String name = conn.getCatalog();
		if (conn.getMetaData().getURL().toLowerCase().contains("oracle")) {
			name = conn.getMetaData().getUserName();
		} else if (conn.getMetaData().getURL().toLowerCase().contains("sqlserver")) {
			name = null;
		}
		return name;
	}

	public static List<Map<Integer, Object>> getTablePrimaryKeys(Connection conn, String tableSchema, String tableName) throws Exception {
		List<Map<Integer, Object>> primaryKeys = new ArrayList<Map<Integer, Object>>();
		Map<Integer, Object> primaryKey;
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		if (tableSchema == null) {
			tableSchema = getDefaultTableSchema(conn);
		}
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getPrimaryKeys(null, tableSchema, tableName.toUpperCase());
			while (rs.next()) {
				primaryKey = new HashMap<Integer, Object>();
				int i = 1;
				while (i <= 6) {
					primaryKey.put(i, rs.getObject(i));
					i++;
				}
				primaryKeys.add(primaryKey);
			}
			return primaryKeys;
		} finally {
			DbJdbcUtils.closeResultSet(rs);
		}
	}

	public static List<Map<Integer, Object>> getTableExportedKeys(Connection conn, String tableSchema, String tableName) throws Exception {
		List<Map<Integer, Object>> exportedKeys = new ArrayList<Map<Integer, Object>>();
		Map<Integer, Object> exportedKey;
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		if (tableSchema == null) {
			tableSchema = getDefaultTableSchema(conn);
		}
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getExportedKeys(null, tableSchema, tableName.toUpperCase());
			while (rs.next()) {
				exportedKey = new HashMap<Integer, Object>();
				int i = 1;
				while (i <= 14) {
					exportedKey.put(i, rs.getObject(i));
					i++;
				}
				exportedKeys.add(exportedKey);
			}
			return exportedKeys;
		} finally {
			DbJdbcUtils.closeResultSet(rs);
		}
	}

	public static List<Map<Integer, Object>> getTableImportedKeys(Connection conn, String tableSchema, String tableName) throws Exception {
		List<Map<Integer, Object>> importedKeys = new ArrayList<Map<Integer, Object>>();
		Map<Integer, Object> importedKey;
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		if (tableSchema == null) {
			tableSchema = getDefaultTableSchema(conn);
		}
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getImportedKeys(null, tableSchema, tableName.toUpperCase());
			while (rs.next()) {
				importedKey = new HashMap<Integer, Object>();
				int i = 1;
				while (i <= 14) {
					importedKey.put(i, rs.getObject(i));
					i++;
				}
				importedKeys.add(importedKey);
			}
			return importedKeys;
		} finally {
			DbJdbcUtils.closeResultSet(rs);
		}
	}

	public static int[] executeUpdate(Connection conn, String[] sqls) throws Exception {
		boolean isShowSql = false;
		List<String> sqlList = new ArrayList<String>();
		for (String sql : sqls) {
			if (StringUtils.hasText(sql)) {
				sqlList.add(sql);
			}
		}
		String[] sqlArray = sqlList.toArray(new String[] {});
		if (isShowSql) {
			for (String s : sqlArray) {
				System.out.println(s);
			}
		}
		Statement st = null;
		int[] rowsAffected = new int[] { sqlArray.length };
		try {
			conn.setAutoCommit(true);
			st = conn.createStatement();
			if (DbJdbcUtils.supportsBatchUpdates(conn)) {
				for (String currentSql : sqlArray) {
					st.addBatch(currentSql);
				}
				rowsAffected = st.executeBatch();
			} else {
				for (int i = 0; i < sqlArray.length; i++) {
					if (!st.execute(sqlArray[i])) {
						rowsAffected[i] = st.getUpdateCount();
					}
				}
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			DbJdbcUtils.closeStatement(st);
		}
		return rowsAffected;
	}

	public static int[] executeUpdate(Connection conn, String sql) throws Exception {
		String[] sqls = sql.split(";");
		return executeUpdate(conn, sqls);
	}

	public static boolean supportsBatchUpdates(Connection conn) throws Exception {
		DatabaseMetaData dbmd = conn.getMetaData();
		if (dbmd != null) {
			if (dbmd.supportsBatchUpdates()) {
				return true;
			}
		}
		return false;
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}