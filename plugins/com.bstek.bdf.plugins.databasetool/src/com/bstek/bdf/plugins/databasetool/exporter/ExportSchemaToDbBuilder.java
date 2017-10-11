package com.bstek.bdf.plugins.databasetool.exporter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.utils.StringUtils;

public class ExportSchemaToDbBuilder {
	private DbDialect dbDialect;
	private Schema schema;
	private Connection conn;
	private Collection<String> dbTableNames;
	private boolean deleteTable;

	private ExportSchemaToDbBuilder(DbDialect dbDialect, Schema schema) {
		this.dbDialect = dbDialect;
		this.schema = schema;
		deleteTable = false;

	}

	private ExportSchemaToDbBuilder(DbDialect dbDialect, Schema schema, boolean deleteTable) {
		this.dbDialect = dbDialect;
		this.schema = schema;
		this.deleteTable = deleteTable;

	}

	public static ExportSchemaToDbBuilder getInstance(DbDialect dbDialect, Schema schema) {
		return new ExportSchemaToDbBuilder(dbDialect, schema);
	}

	public static ExportSchemaToDbBuilder getInstance(DbDialect dbDialect, Schema schema, boolean deleteTable) {
		return new ExportSchemaToDbBuilder(dbDialect, schema, deleteTable);
	}

	public void execute(Connection conn, IProgressMonitor monitor) throws Exception {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		if (conn == null) {
			throw new IllegalArgumentException("java.sql.connection  is  null");
		}
		this.conn = conn;
		monitor.setTaskName("正在同步数据模型到数据库....");
		if (deleteTable) {
			updateProgressMonitor(monitor, "正在执行删除表操作....");
			deleteDbTables(monitor);
		}
		Map<String, List<String>> dbTables = DbJdbcUtils.getDefaultSchemaTables(conn);
		Collection<List<String>> values = dbTables.values();
		for (List<String> list : values) {
			dbTableNames = list;
		}
		List<Table> modelTables = schema.getTables();
		List<Table> model2DbTableList = new ArrayList<Table>();
		for (Table modelTable : modelTables) {
			String modelTableName = modelTable.getName();
			boolean isDbTable = checkIsDbTable(modelTableName);
			if (isDbTable) {
				updateDbColumn(modelTable, monitor);
			} else {
				model2DbTableList.add(modelTable);
			}
		}
		addDbTables(model2DbTableList, monitor);
		updateDbForeignKey(modelTables, monitor);
		updateDbComments(monitor);

	}

	private void addDbTables(List<Table> model2DbTableList, IProgressMonitor monitor) throws Exception {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		for (Table table : model2DbTableList) {
			updateProgressMonitor(monitor, "正在创建表【" + table.getName() + "】....");
			String tableDefination = dbDialect.getCreateTableDefination(table, false);
			String[] sqls = tableDefination.split(";");
			DbJdbcUtils.executeUpdate(conn, sqls);
		}

	}

	private void deleteDbTables(IProgressMonitor monitor) throws Exception {
		Map<String, List<String>> dbTables = DbJdbcUtils.getDefaultSchemaTables(conn);
		Collection<List<String>> values = dbTables.values();
		Collection<String> tables = new ArrayList<String>();
		for (List<String> list : values) {
			tables = list;
		}
		for (Table table : schema.getTables()) {
			updateProgressMonitor(monitor, "正在扫描表【" + table.getName() + "】....");
			for (String dbTableName : tables) {
				if (dbTableName.toLowerCase().equals(table.getName().toLowerCase())) {
					deleteForeignKeyConstraint(table);
					updateProgressMonitor(monitor, "正在删除表【" + table.getName() + "】....");
					String dropTableDefination = dbDialect.getDropTableDefination(table);
					DbJdbcUtils.executeUpdate(conn, dropTableDefination);
				}
			}
		}
	}

	private void updateDbColumn(Table modelTable, IProgressMonitor monitor) throws Exception {
		updateProgressMonitor(monitor, "正在检查更新表【" + modelTable.getName() + "】....");
		List<Map<Integer, Object>> columnInfoList = DbJdbcUtils.getTableColumn(conn, null, modelTable.getName());
		List<Map<Integer, Object>> primaryKeys = DbJdbcUtils.getTablePrimaryKeys(conn, null, modelTable.getName());
		for (Column column : modelTable.getColumns()) {
			updateProgressMonitor(monitor, "正在检查更新列【" + column.getName() + "】....");
			String columnName = (String) column.getName();
			boolean isExist = checkExistColumnName(columnInfoList, columnName);
			if (!isExist) {
				//add dbcolumn
				String columnDefination = dbDialect.getAddColumnDefination(modelTable, column);
				DbJdbcUtils.executeUpdate(conn, columnDefination);
			} else {
				// delete unique column index
				List<String> indexNames = dbDialect.getColumnUniqueIndexNames(conn, modelTable.getName(), column.getName());
				if (indexNames != null && !indexNames.isEmpty()) {
					for (String indexName : indexNames) {
						String indexNmaeDefination = dbDialect.getDropUniqueDefination(modelTable.getName(), indexName);
						DbJdbcUtils.executeUpdate(conn, indexNmaeDefination);
					}
				}
				String columnDefination = dbDialect.getUpdateColumnDefination(modelTable, column);
				DbJdbcUtils.executeUpdate(conn, columnDefination);

				// set column not null or null
				if (checkColumnNotNull(columnInfoList, columnName)) {
					if (!column.isNotNull()) {
						String defination = dbDialect.getUpdateColumnIsNullDefination(modelTable, column);
						DbJdbcUtils.executeUpdate(conn, defination);
					}
				} else {
					if (column.isNotNull()) {
						String defination = dbDialect.getUpdateColumnNotNullDefination(modelTable, column);
						DbJdbcUtils.executeUpdate(conn, defination);
					}
				}

			}
		}
		// delete db column
		for (Map<Integer, Object> map : columnInfoList) {
			String columnName = (String) map.get(4);
			Column column = findColumnByName(modelTable, columnName);
			if (column == null) {
				if (checkPrimaryKey(columnName, primaryKeys)) {
					String primaryKeyConstaintName = dbDialect.getTablePrimaryIndexName(conn, modelTable.getName());
					String dropPkDefination = dbDialect.getDropPrimaryKeyDefination(modelTable, primaryKeyConstaintName);
					DbJdbcUtils.executeUpdate(conn, dropPkDefination);
				}
				String columnDefination = dbDialect.getDeleteColumnDefination(modelTable, columnName);
				DbJdbcUtils.executeUpdate(conn, columnDefination);
			}
		}
		// add unique index;
		String uniqueDefinations = dbDialect.getAddUniqueDefination(modelTable, null);
		DbJdbcUtils.executeUpdate(conn, uniqueDefinations);
		// check primary keys
		primaryKeys = DbJdbcUtils.getTablePrimaryKeys(conn, null, modelTable.getName());
		if (checkPrimarykeyChanged(modelTable, primaryKeys)) {
			if (!primaryKeys.isEmpty()) {
				String primaryKeyConstaintName = dbDialect.getTablePrimaryIndexName(conn, modelTable.getName());
				String dropPkDefination = dbDialect.getDropPrimaryKeyDefination(modelTable, primaryKeyConstaintName);
				DbJdbcUtils.executeUpdate(conn, dropPkDefination);
			}
			String alertPkDefination = dbDialect.getAlertPrimaryKeyDefination(modelTable, null);
			DbJdbcUtils.executeUpdate(conn, alertPkDefination);
		}

	}

	private void updateDbForeignKey(List<Table> modelTables, IProgressMonitor monitor) throws Exception {
		for (Table modelTable : modelTables) {
			deleteForeignKeyConstraint(modelTable);
			updateProgressMonitor(monitor, "正在检查更新表【" + modelTable.getName() + "】外键关系....");
			List<com.bstek.bdf.plugins.databasetool.model.Connection> connections = modelTable.getOutConnections();
			for (com.bstek.bdf.plugins.databasetool.model.Connection c : connections) {
				String defination = dbDialect.getAddForeignKeyConstraintDefination(c);
				DbJdbcUtils.executeUpdate(conn, defination);
			}
		}
	}

	private void deleteForeignKeyConstraint(Table modelTable) throws Exception {
		List<Map<Integer, Object>> exportedKeys = DbJdbcUtils.getTableExportedKeys(conn, null, modelTable.getName());
		for (Map<Integer, Object> exportedKey : exportedKeys) {
			String PKTABLE_NAME = (String) exportedKey.get(3);
			String FKTABLE_NAME = (String) exportedKey.get(7);
			String FK_NAME = (String) exportedKey.get(12);
			if (modelTable.getName().toLowerCase().equals(PKTABLE_NAME.toLowerCase())) {
				String constraintName = FK_NAME;
				String tableName = FKTABLE_NAME;
				String defination = dbDialect.getDropForeignKeyConstraintDefination(tableName, constraintName);
				DbJdbcUtils.executeUpdate(conn, defination);
			}
		}

	}

	private void updateDbComments(IProgressMonitor monitor) throws Exception {
		if (dbDialect.supportAppendCommentDefination()) {
			updateProgressMonitor(monitor, "正在检查同步表注释....");
			String[] definations = dbDialect.getCommentDefination(schema).split(";");
			for (String def : definations) {
				if (StringUtils.hasText(def)) {
					DbJdbcUtils.executeUpdate(conn, def);
				}
			}
		}
	}

	private boolean checkPrimarykeyChanged(Table modelTable, List<Map<Integer, Object>> primaryKeys) throws Exception {
		List<Column> pkColumns = modelTable.getPkColumns();
		if (primaryKeys.size() == pkColumns.size()) {
			for (Column column : pkColumns) {
				boolean isPk = checkPrimaryKey(column.getName(), primaryKeys);
				if (!isPk) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;

	}

	private boolean checkPrimaryKey(String columnName, List<Map<Integer, Object>> primaryKeys) {
		for (Map<Integer, Object> map : primaryKeys) {
			String name = (String) map.get(4);
			if (columnName.toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkIsDbTable(String modelTableName) {
		for (String dbTableName : dbTableNames) {
			if (dbTableName.toLowerCase().equals(modelTableName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private Column findColumnByName(Table table, String columnName) {
		if (table == null) {
			return null;
		}
		for (Column column : table.getColumns()) {
			if (column.getName().toLowerCase().equals(columnName.toLowerCase())) {
				return column;
			}
		}
		return null;
	}

	private boolean checkExistColumnName(List<Map<Integer, Object>> list, String columnName) {
		for (Map<Integer, Object> map : list) {
			String name = (String) map.get(4);
			if (name.toLowerCase().equals(columnName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkColumnNotNull(List<Map<Integer, Object>> list, String columnName) {
		for (Map<Integer, Object> map : list) {
			String name = (String) map.get(4);
			if (name.toLowerCase().equals(columnName.toLowerCase())) {
				String IS_NULLABLE = (String) map.get(18);
				if (IS_NULLABLE.equals("NO")) {
					return true;
				}
			}
		}
		return false;
	}

	private void updateProgressMonitor(IProgressMonitor monitor, String subTaskName) {
		monitor.worked(1);
		monitor.subTask(subTaskName);
	}
}
