package com.bstek.bdf.plugins.databasetool.dialect.impl;

import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeRegister;

public class MySqlColumnTypeRegister implements ColumnTypeRegister {

	@Override
	public List<ColumnType> getAllColumnTypes() {
		List<ColumnType> columnTypelist = new ArrayList<ColumnType>();

		columnTypelist.add(new ColumnType(ColumnTypeConstants.BIT, true, false, 1, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BOOL, false, false, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TINYINT, true, false, 2, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TINYINT_UNSIGNED, true, false, 2, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SMALLINT, true, false, 6, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SMALLINT_UNSIGNED, true, false, 6, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.INT, true, false, 8, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.INT_UNSIGNED, true, false, 8, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.INTEGER, true, false, 8, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.INTEGER_UNSIGNED, true, false, 8, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.MEDIUMINT, true, false, 10, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.MEDIUMINT_UNSIGNED, true, false, 10, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BIGINT, true, false, 10, 0, ColumnType.GROUP_NUMBER));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BIGINT_UNSIGNED, true, false, 10, 0, ColumnType.GROUP_NUMBER));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.FLOAT, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DOUBLE, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DOUBLE_PRECISION, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DECIMAL, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.NUMERIC, true, true, 5, 2, ColumnType.GROUP_DECIMAL));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.REAL, true, true, 5, 2, ColumnType.GROUP_DECIMAL));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.CHAR, true, false, 1, 0, ColumnType.GROUP_CHARACTOR));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARCHAR, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.ENUM, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.SET, true, false, 50, 0, ColumnType.GROUP_STRING));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONG_VARCHAR, true, false, 50, 0, ColumnType.GROUP_STRING));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.BLOB, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TINYBLOB, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.MEDIUMBLOB, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONGBLOB, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONG_VARBINARY, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.BINARY, false, false, ColumnType.GROUP_BINARY_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.VARBINARY, false, false, ColumnType.GROUP_BINARY_LOB));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.TINYTEXT, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TEXT, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.MEDIUMTEXT, false, false, ColumnType.GROUP_CHARACTOR_LOB));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.LONGTEXT, false, false, ColumnType.GROUP_CHARACTOR_LOB));

		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATE, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIME, false, false, ColumnType.GROUP_TIME));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.YEAR, true, false, 4, 0, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.DATETIME, false, false, ColumnType.GROUP_DATE));
		columnTypelist.add(new ColumnType(ColumnTypeConstants.TIMESTAMP, false, false, ColumnType.GROUP_TIME));

		return columnTypelist;
	}

	@Override
	public String getNumberGroupPreference() {
		return ColumnTypeConstants.INT;
	}

	@Override
	public String getDecimalGroupPreference() {
		return ColumnTypeConstants.DECIMAL;
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
		return ColumnTypeConstants.BLOB;
	}

	@Override
	public String getCharactorLobGroupPreference() {
		return ColumnTypeConstants.TEXT;
	}

}
