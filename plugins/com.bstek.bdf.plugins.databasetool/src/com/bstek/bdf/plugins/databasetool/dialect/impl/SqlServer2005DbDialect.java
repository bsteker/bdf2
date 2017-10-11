package com.bstek.bdf.plugins.databasetool.dialect.impl;

import com.bstek.bdf.plugins.databasetool.dialect.DbType;

public class SqlServer2005DbDialect extends SqlServer2008DbDialect {
	
	public SqlServer2005DbDialect() {
		this.setColumnTypeRegister(new SqlServer2005ColumnTypeRegister());
	}

	@Override
	public String getDbType() {
		return DbType.SqlServer2005.name();
	}

}
