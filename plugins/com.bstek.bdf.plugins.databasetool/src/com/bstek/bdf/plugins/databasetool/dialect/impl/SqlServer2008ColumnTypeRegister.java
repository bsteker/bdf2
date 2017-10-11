package com.bstek.bdf.plugins.databasetool.dialect.impl;

import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeRegister;

public class SqlServer2008ColumnTypeRegister implements ColumnTypeRegister {

	@Override
	public List<ColumnType> getAllColumnTypes() {
		List<ColumnType> columnTypelist = new ArrayList<ColumnType>();

		columnTypelist.add(new ColumnType(ColumnTypeConstants.BIT, false, false, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TINYINT, false, false, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.INT, false, false, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SMALLINT, false, false, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BIGINT, false, false, ColumnType.GROUP_NUMBER));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.FLOAT, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DECIMAL, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NUMERIC, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.MONEY, false, false, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SMALL_MONEY, false, false, ColumnType.GROUP_DECIMAL));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.CHAR, true, false, 1, 0, ColumnType.GROUP_CHARACTOR));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.UNIQUEIDENTIFIER, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NCHAR, true, false, 1, 0, ColumnType.GROUP_CHARACTOR));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARCHAR, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NVARCHAR, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SYSNAME, true, false, 50, 0, ColumnType.GROUP_STRING));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.TEXT, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NTEXT, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BINARY, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARBINARY, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.GEOGRAPHY, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.GEOMETRY, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.HIERARCHYID, false, false, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.IMAGE, false, false, ColumnType.GROUP_BINARY_LOB));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.REAL, false, false, ColumnType.GROUP_DECIMAL));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATE, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIME, false, false, ColumnType.GROUP_TIME));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATETIME, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIMESTAMP, false, false, ColumnType.GROUP_TIME));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATETIME2, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATETIMEOFFSET, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SMALLDATETIME, false, false, ColumnType.GROUP_DATE));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.XML, false, false, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SQL_VARIABLE, false, false, ColumnType.GROUP_STRING));

		return columnTypelist;
	}

	@Override
	public String getNumberGroupPreference() {
		return ColumnTypeConstants.INT;
	}

	@Override
	public String getDecimalGroupPreference() {
		return ColumnTypeConstants.NUMERIC;
	}

	@Override
	public String getCharactorGroupPreference() {
		return ColumnTypeConstants.CHAR;
	}

	@Override
	public String getStringGroupPreference() {
		return ColumnTypeConstants.VARCHAR;
	}

	@Override
	public String getDateGroupPreference() {
		return ColumnTypeConstants.DATE;
	}

	@Override
	public String getTimeGroupPreference() {
		return ColumnTypeConstants.TIMESTAMP;
	}

	@Override
	public String getBinaryLobGroupPreference() {
		return ColumnTypeConstants.VARBINARY;
	}

	@Override
	public String getCharactorLobGroupPreference() {
		return ColumnTypeConstants.TEXT;
	}

}
