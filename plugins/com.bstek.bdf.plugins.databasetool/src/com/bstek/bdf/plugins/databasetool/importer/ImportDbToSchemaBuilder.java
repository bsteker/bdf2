package com.bstek.bdf.plugins.databasetool.importer;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;
import com.bstek.bdf.plugins.databasetool.dialect.impl.MySqlDbDialect;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.utils.StringUtils;

public class ImportDbToSchemaBuilder {
	private List<Table> builderTableList;
	private DbDialect dbDialect;
	private Map<String, String> tableComments;
	private int taskCounter;
	private int taskSum;

	private ImportDbToSchemaBuilder(DbDialect dbDialect) {
		this.dbDialect = dbDialect;

	}

	public static ImportDbToSchemaBuilder getInstance(DbDialect dbDialect) {
		return new ImportDbToSchemaBuilder(dbDialect);
	}

	public List<Table> builder(List<String> checkedTables, Connection conn, IProgressMonitor monitor) throws Exception {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		taskCounter = 1;
		taskSum = checkedTables.size() * 2;
		builderTables(checkedTables, conn, monitor);
		builderTableConnections(checkedTables, conn, monitor);
		return builderTableList;
	}

	private void builderTables(List<String> checkedTables, Connection conn, IProgressMonitor monitor) throws Exception {
		builderTableList = new ArrayList<Table>();
		int x = 5;
		int y = 5;
		int height = BaseModel.h;
		int size = checkedTables.size();
		long horizontalTableMaxSize = calculateHorizontalTableMaxSize(size);
		int gap = 40;
		int currentGroupMaxHeight = height;
		int index = 1;
		Table lastGroupFistTable = null;
		Table table;
		for (String tableName : checkedTables) {
			String subTaskName = "正在为表【" + tableName + "】创建模型....";
			updateProgressMonitor(monitor, subTaskName);
			if (monitor.isCanceled() == true) {
				break;
			}
			table = new Table();
			table.setName(tableName);
			table.setLabel(tableName);
			table.setComment(getTableComment(conn, null, tableName));
			builderTableColumns(table, tableName, conn, monitor);
			Rectangle rectangle = table.getConstraints();
			table.adjustConstraints();
			int h = table.getConstraints().getCopy().height;
			if (h > currentGroupMaxHeight) {
				currentGroupMaxHeight = h;
			}
			if (index % horizontalTableMaxSize == 1) {
				currentGroupMaxHeight = height;
				x = 5;
				if (index == 1) {
					y = 5;
				} else {
					y = lastGroupFistTable.getConstraints().getBottomLeft().y + currentGroupMaxHeight + gap;
				}
			} else {
				x = x + BaseModel.w + gap;
			}
			rectangle.setLocation(x, y);
			table.setConstraints(rectangle);
			if (index % horizontalTableMaxSize == 1) {
				lastGroupFistTable = table;
			}
			builderTableList.add(table);
			index++;
			taskCounter++;

		}

	}

	private void builderTableColumns(Table table, String tableName, Connection conn, IProgressMonitor monitor) throws Exception {
		List<Map<Integer, Object>> columnInfoList = DbJdbcUtils.getTableColumn(conn, null, tableName);
		List<Map<Integer, Object>> primaryKeys = DbJdbcUtils.getTablePrimaryKeys(conn, null, tableName);
		List<Map<Integer, Object>> importedKeys = DbJdbcUtils.getTableImportedKeys(conn, null, tableName);
		Column column;
		for (Map<Integer, Object> map : columnInfoList) {
			System.out.println(map);
			column = new Column();
			column.setTable(table);
			String column_name = (String) map.get(4);
			column.setName(column_name);
			column.setLabel(column_name);

			boolean isPk = checkPrimaryKey(column_name, primaryKeys);
			column.setPk(isPk);

			boolean isFk = checkForgeinKey(column_name, importedKeys);
			column.setFk(isFk);

			String type_name = (String) map.get(6);
			if (type_name.indexOf("(") != -1) {
				type_name = type_name.substring(0, type_name.indexOf("("));
			}
			if (type_name.toLowerCase().endsWith("identity")) {
				type_name = type_name.substring(0, type_name.indexOf(" "));
			}
			column.setType(type_name.toUpperCase());

			if (map.get(7) != null) {
				if (validateHasLength(type_name)) {
					column.setLength(map.get(7).toString());
				}
			} else {
				column.setLength("");
			}
			if (map.get(9) != null) {
				String length = map.get(7).toString();
				if (!length.equals("0") && validateHasDecimalLength(type_name)) {
					column.setDecimalLength(length);
				} else {
					column.setDecimalLength("");
				}
			} else if (supportDecimalLength(type_name)) {
				column.setDecimalLength("0");
			} else {
				column.setDecimalLength("");

			}
			if (validateHasLength(type_name)) {
				String column_def = (String) map.get(13);
				column.setDefaultValue(column_def);
			} else {
				column.setDefaultValue("");
			}
			String is_nullable = (String) map.get(18);
			column.setNotNull(is_nullable.equals("YES") ? false : true);

			if (map.get(23) != null) {
				String is_autoincrement = (String) map.get(23);
				column.setAutoIncrement(is_autoincrement.equals("YES") ? true : false);
			} else {
				if (map.get(6) != null && ((String) map.get(6)).toLowerCase().endsWith("identity")) {
					column.setAutoIncrement(true);
				}
			}
			if (isPk) {
				column.setUnique(false);
			} else {
				List<String> indexNames = dbDialect.getColumnUniqueIndexNames(conn, tableName, column_name);
				if (indexNames == null || indexNames.isEmpty()) {
					column.setUnique(false);
				} else {
					column.setUnique(true);
				}
			}
			String remarks = (String) map.get(12);
			if (!StringUtils.hasText(remarks)) {
				remarks = getColumnComment(conn, tableName, column.getName());
			}
			column.setComment(remarks);
			if (StringUtils.hasText(remarks)) {
				column.setLabel(remarks);
			}
			if (dbDialect instanceof MySqlDbDialect) {
				MySqlDbDialect mySqlDbDialect = (MySqlDbDialect) dbDialect;
				if (column.getType().toLowerCase().equals("enum") || column.getType().toLowerCase().equals("set")
						|| column.getType().toLowerCase().equals("year")) {
					String spType = mySqlDbDialect.getSpecilColumnLength(conn, tableName, column_name);
					column.setLength(spType);
				}
			}

			table.addColumn(column);

		}
	}

	private void builderTableConnections(List<String> checkedTables, Connection conn, IProgressMonitor monitor) throws Exception {
		if (builderTableList == null) {
			return;
		}
		for (String tableName : checkedTables) {
			String subTaskName = "正在为表【" + tableName + "】创建主外键关联模型....";
			updateProgressMonitor(monitor, subTaskName);
			if (monitor.isCanceled() == true) {
				break;
			}
			List<Map<Integer, Object>> exportedKeys = DbJdbcUtils.getTableExportedKeys(conn, null, tableName);
			com.bstek.bdf.plugins.databasetool.model.Connection tableRelation = null;
			for (Map<Integer, Object> exportedKey : exportedKeys) {
				String PKTABLE_NAME = (String) exportedKey.get(3);
				String PKCOLUMN_NAME = (String) exportedKey.get(4);
				String FKTABLE_NAME = (String) exportedKey.get(7);
				String FKCOLUMN_NAME = (String) exportedKey.get(8);
				String FK_NAME = (String) exportedKey.get(12);
				Table sourceTable = findTableByName(builderTableList, PKTABLE_NAME);
				Column pkColumn = findColumnByName(sourceTable, PKCOLUMN_NAME);
				Table targetTable = findTableByName(builderTableList, FKTABLE_NAME);
				Column fkColumn = findColumnByName(targetTable, FKCOLUMN_NAME);
				String constraintName = FK_NAME;
				if (sourceTable != null && targetTable != null) {
					tableRelation = new com.bstek.bdf.plugins.databasetool.model.Connection(sourceTable, targetTable, pkColumn, fkColumn,
							constraintName);
					tableRelation.connect();
					if (sourceTable.equals(targetTable)) {
						tableRelation.setSelfConnectionBendpoint();
					}
				}
			}
			taskCounter++;
		}
	}

	private Table findTableByName(List<Table> builderTableList, String tableName) {
		for (Table table : builderTableList) {
			if (table.getName().equals(tableName)) {
				return table;
			}
		}
		return null;
	}

	private Column findColumnByName(Table table, String columnName) {
		if (table == null) {
			return null;
		}
		for (Column column : table.getColumns()) {
			if (column.getName().equals(columnName)) {
				return column;
			}
		}
		return null;
	}

	private boolean checkPrimaryKey(String columnName, List<Map<Integer, Object>> primaryKeys) {
		for (Map<Integer, Object> map : primaryKeys) {
			String COLUMN_NAME = (String) map.get(4);
			if (columnName.equals(COLUMN_NAME)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkForgeinKey(String columnName, List<Map<Integer, Object>> importedKeys) {
		for (Map<Integer, Object> map : importedKeys) {
			String FKCOLUMN_NAME = (String) map.get(8);
			if (columnName.equals(FKCOLUMN_NAME)) {
				return true;
			}
		}
		return false;
	}

	private String getTableComment(Connection conn, String tableSchema, String tableName) throws Exception {
		Map<Integer, Object> tableMap = DbJdbcUtils.getTable(conn, tableSchema, tableName);
		String comment = (String) tableMap.get(5);
		if (!StringUtils.hasText(comment)) {
			if (tableComments == null && dbDialect != null) {
				tableComments = dbDialect.getAllTableComments(conn);
			}
			comment = tableComments.get(tableName);
		}
		return comment;
	}

	private String getColumnComment(Connection conn, String tableName, String columnName) throws Exception {
		Map<String, String> comments = dbDialect.getTableColumnComments(conn, tableName);
		return comments.get(columnName);
	}

	private boolean validateHasLength(String typeName) {
		String name = typeName.toLowerCase();
		String[] filterTypes = new String[] { "date", "time", "year", "text", "blob", "clob", "varbinary", "binary", "image" };
		for (String filterType : filterTypes) {
			if (name.indexOf(filterType) != -1) {
				return false;
			}
		}
		ColumnType columnType = DbDialectManager.getColumnType(dbDialect, typeName);
		if (columnType != null) {
			return columnType.isLength();
		}
		return true;
	}

	private boolean validateHasDecimalLength(String typeName) {
		ColumnType columnType = DbDialectManager.getColumnType(dbDialect, typeName);
		if (columnType != null) {
			return columnType.isDecimal();
		}
		return false;
	}

	private boolean supportDecimalLength(String typeName) {
		String name = typeName.toLowerCase();
		String[] filterTypes = new String[] { "float", "double", "decimal", "number", "numeric" };
		for (String filterType : filterTypes) {
			if (name.indexOf(filterType) != -1) {
				return true;
			}
		}
		ColumnType columnType = DbDialectManager.getColumnType(dbDialect, typeName);
		if (columnType != null) {
			return columnType.isDecimal();
		}
		return false;
	}

	private long calculateHorizontalTableMaxSize(int count) {
		double pow = Math.pow(count, 0.5);
		return Math.round(pow) + 1;
	}

	private String calculatePercent(int i, int j) {
		Format format = new DecimalFormat("##0.00%");
		return format.format((double) i / j);
	}

	private String getTaskName() {
		return "共" + taskSum + "个任务，" + "当前第" + taskCounter + "个任务，已经完成" + calculatePercent(taskCounter, taskSum);
	}

	private void updateProgressMonitor(IProgressMonitor monitor, String subTaskName) {
		monitor.worked(1);
		monitor.setTaskName(getTaskName());
		monitor.subTask(subTaskName);
	}

	public List<Table> getBuilderTableList() {
		return builderTableList;
	}

	public void setBuilderTableList(List<Table> builderTableList) {
		this.builderTableList = builderTableList;
	}

	public DbDialect getDbDialect() {
		return dbDialect;
	}

	public void setDbDialect(DbDialect dbDialect) {
		this.dbDialect = dbDialect;
	}
}
