package com.bstek.bdf.plugins.databasetool.dialect.impl;

import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeRegister;

public class DB2ColumnTypeRegister implements ColumnTypeRegister {

	@Override
	public List<ColumnType> getAllColumnTypes() {
		List<ColumnType> columnTypelist = new ArrayList<ColumnType>();

		columnTypelist.add(new ColumnType(ColumnTypeConstants.SMALLINT, false, false, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.INTEGER, false, false, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BIGINT, false, false, ColumnType.GROUP_NUMBER));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.DOUBLE, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DECIMAL, true, true, 5, 2, ColumnType.GROUP_DECIMAL));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.CHARACTER, true, false, 1, 0, ColumnType.GROUP_CHARACTOR));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.GRAPHIC, true, false, 10, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARCHAR, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARGRAPHIC, true, false, 50, 0, ColumnType.GROUP_STRING));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.BLOB, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.CLOB, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DBCLOB, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONG_VARCHAR, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONG_VARGRAPHIC, false, false, ColumnType.GROUP_CHARACTOR_LOB));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.REAL, false, false, ColumnType.GROUP_NUMBER));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATE, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIME, false, false, ColumnType.GROUP_TIME));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIMESTAMP, false, false, ColumnType.GROUP_TIME));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.XML, false, false, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SYSPROC_DB2SQLSTATE, false, false, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SYSIBMADM_FILE_TYPE, false, false, ColumnType.GROUP_STRING));

		return columnTypelist;
	}

	@Override
	public String getNumberGroupPreference() {
		return ColumnTypeConstants.INTEGER;
	}

	@Override
	public String getDecimalGroupPreference() {
		return ColumnTypeConstants.DECIMAL;
	}

	@Override
	public String getCharactorGroupPreference() {
		return ColumnTypeConstants.CHARACTER;
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
		return ColumnTypeConstants.BLOB;
	}

	@Override
	public String getCharactorLobGroupPreference() {
		return ColumnTypeConstants.CLOB;
	}

}
