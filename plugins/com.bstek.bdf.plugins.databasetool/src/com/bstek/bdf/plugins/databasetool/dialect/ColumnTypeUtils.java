/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.dialect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnTypeUtils {

	private static Map<String, String> SQL_JAVA_MAPPING = new HashMap<String, String>();
	private static List<String> INTEGER_COLUMN_TYPE = new ArrayList<String>();

	public static String getJavaType(String type) {
		String value = SQL_JAVA_MAPPING.get(type);
		return value == null ? "String" : value;
	}

	public static List<String> getIntegerColumnType() {
		return INTEGER_COLUMN_TYPE;
	}

	static {
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.TINYINT);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.TINYINT_UNSIGNED);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.SMALLINT);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.SMALLINT_UNSIGNED);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.INTEGER);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.INTEGER_UNSIGNED);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.INT);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.INT_UNSIGNED);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.MEDIUMINT);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.MEDIUMINT_UNSIGNED);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.BIGINT);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.BIGINT_UNSIGNED);
		INTEGER_COLUMN_TYPE.add(ColumnTypeConstants.LONG);
	}

	static {
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BOOL, "boolean");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BIT, "boolean");

		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TINYINT, "byte");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TINYINT_UNSIGNED, "byte");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.SMALLINT, "short");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.SMALLINT_UNSIGNED, "short");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.MEDIUMINT, "short");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.MEDIUMINT_UNSIGNED, "short");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.INTEGER, "int");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.INTEGER_UNSIGNED, "int");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.INT, "int");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.INT_UNSIGNED, "int");

		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BIGINT, "long");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BIGINT_UNSIGNED, "long");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.LONG, "long");

		SQL_JAVA_MAPPING.put(ColumnTypeConstants.FLOAT, "float");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BINARY_FLOAT, "float");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DOUBLE, "double");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BINARY_DOUBLE, "double");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DOUBLE_PRECISION, "double");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DECIMAL, "BigDecimal");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.NUMBER, "BigDecimal");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.NUMERIC, "BigDecimal");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.MONEY, "BigDecimal");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.SMALL_MONEY, "BigDecimal");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.REAL, "BigDecimal");

		SQL_JAVA_MAPPING.put(ColumnTypeConstants.CHAR, "String");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.ROWID, "String");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.NCHAR, "String");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.VARCHAR, "String");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.NVARCHAR, "String");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.VARCHAR2, "String");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.NVARCHAR2, "String");

		SQL_JAVA_MAPPING.put(ColumnTypeConstants.CLOB, "Clob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.NCLOB, "Clob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DBCLOB, "Clob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TINYTEXT, "Clob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TEXT, "Clob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.MEDIUMTEXT, "Clob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.LONGTEXT, "Clob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.NTEXT, "Clob");

		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TINYBLOB, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BLOB, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.MEDIUMBLOB, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.LONGBLOB, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.BINARY, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.VARBINARY, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.LONG_VARBINARY, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.IMAGE, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.RAW, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.LONG_RAW, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.GEOGRAPHY, "Blob");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.GEOMETRY, "Blob");

		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DATE, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TIME, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.YEAR, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DATETIME, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DATETIME2, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DATETIMEOFFSET, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.SMALLDATETIME, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.DATETIME_YEAR_TO_SECOND, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TIMESTAMP, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TIMESTAMP_WITH_LOCAL_TIME_ZONE, "Date");
		SQL_JAVA_MAPPING.put(ColumnTypeConstants.TIMESTAMP_WITH_TIME_ZONE, "Date");
	}

}
