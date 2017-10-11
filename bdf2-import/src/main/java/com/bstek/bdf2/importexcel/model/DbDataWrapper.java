package com.bstek.bdf2.importexcel.model;

import javax.sql.DataSource;

public class DbDataWrapper implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String dataSourceName;
	private DataSource dataSource;
	private String tableName;
	private String tablePrimaryKey;
	private String tableColumn;
	private String primaryKeyType;

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTablePrimaryKey() {
		return tablePrimaryKey;
	}

	public void setTablePrimaryKey(String tablePrimaryKey) {
		this.tablePrimaryKey = tablePrimaryKey;
	}

	public String getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}

	public String getPrimaryKeyType() {
		return primaryKeyType;
	}

	public void setPrimaryKeyType(String primaryKeyType) {
		this.primaryKeyType = primaryKeyType;
	}

}
