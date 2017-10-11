package com.bstek.bdf.plugins.databasetool.dialect.impl;

import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeRegister;

public class OracleColumnTypeRegister implements ColumnTypeRegister {

	@Override
	public List<ColumnType> getAllColumnTypes() {
		List<ColumnType> columnTypelist = new ArrayList<ColumnType>();

		columnTypelist.add(new ColumnType(ColumnTypeConstants.INTEGER, true, false, ColumnType.GROUP_NUMBER));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.NUMBER, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DOUBLE, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.FLOAT, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BINARY_DOUBLE, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BINARY_FLOAT, true, true, 5, 2, ColumnType.GROUP_DECIMAL));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.CHAR, true, false, 1, 0, ColumnType.GROUP_CHARACTOR));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NCHAR, true, false, 1, 0, ColumnType.GROUP_CHARACTOR));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARCHAR, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NVARCHAR, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARCHAR2, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NVARCHAR2, true, false, 50, 0, ColumnType.GROUP_STRING));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.BLOB, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.CLOB, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NCLOB, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONG, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONG_RAW, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.RAW, false, false, ColumnType.GROUP_CHARACTOR_LOB));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATE, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIMESTAMP, false, false, ColumnType.GROUP_TIME));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIMESTAMP_WITH_LOCAL_TIME_ZONE, false, false, ColumnType.GROUP_TIME));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIMESTAMP_WITH_TIME_ZONE, false, false, ColumnType.GROUP_TIME));

		return columnTypelist;
	}

	@Override
	public String getNumberGroupPreference() {
		return ColumnTypeConstants.INTEGER;
	}

	@Override
	public String getDecimalGroupPreference() {
		return ColumnTypeConstants.NUMBER;
	}

	@Override
	public String getCharactorGroupPreference() {
		return ColumnTypeConstants.CHAR;
	}

	@Override
	public String getStringGroupPreference() {
		return ColumnTypeConstants.VARCHAR2;
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
		return ColumnTypeConstants.BLOB;
	}

	@Override
	public String getCharactorLobGroupPreference() {
		return ColumnTypeConstants.CLOB;
	}

}
