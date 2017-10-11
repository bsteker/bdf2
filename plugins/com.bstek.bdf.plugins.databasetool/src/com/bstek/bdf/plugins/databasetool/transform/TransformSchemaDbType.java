package com.bstek.bdf.plugins.databasetool.transform;

import java.util.List;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeRegister;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.dialect.impl.MySqlDbDialect;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.utils.StringUtils;

public class TransformSchemaDbType {
	private DbDialect sourceDbDialect;
	private DbDialect destDbDialect;
	private Schema schema;
	private String sourceDbType;
	private String destDbType;

	private TransformSchemaDbType(Schema schema, String destDbType) {
		this.schema = schema;
		this.destDbType = destDbType;
	}

	public static TransformSchemaDbType getInstance(Schema schema, String destDbType) {
		return new TransformSchemaDbType(schema, destDbType);
	}

	public void transform() {
		sourceDbType = schema.getCurrentDbType();
		destDbDialect = DbDialectManager.getDbDialect(destDbType);
		sourceDbDialect = DbDialectManager.getDbDialect(sourceDbType);
		schema.setCurrentDbType(destDbType);
		for (Table table : schema.getTables()) {
			for (Column column : table.getColumns()) {
				String type = column.getType();
				if (sourceDbDialect instanceof MySqlDbDialect) {
					if (column.getType().toLowerCase().equals("enum") || column.getType().toLowerCase().equals("set")
							|| column.getType().toLowerCase().equals("year")) {
						column.setLength("255");
					}
				}
				String newType = transformType(sourceDbDialect, type, destDbDialect);
				if (StringUtils.hasText(newType)) {
					column.setType(newType);
					column.fireModelModifyEvent();
				}

			}
		}
	}

	private String transformType(DbDialect sourceDbDialect, String sourceType, DbDialect destDbDialect) {
		List<ColumnType> columnTypes = sourceDbDialect.getColumnTypes();
		int group = -1;
		String type = null;
		for (ColumnType columnType : columnTypes) {
			if (columnType.getType().toLowerCase().equals(sourceType.toLowerCase())) {
				group = columnType.getGroup();
				break;
			}
		}
		if (group != -1) {
			ColumnTypeRegister columnTypeRegister = destDbDialect.getColumnTypeRegister();
			switch (group) {
			case ColumnType.GROUP_NUMBER:
				type = columnTypeRegister.getNumberGroupPreference();
				break;
			case ColumnType.GROUP_DECIMAL:
				type = columnTypeRegister.getDecimalGroupPreference();
				break;
			case ColumnType.GROUP_CHARACTOR:
				type = columnTypeRegister.getCharactorGroupPreference();
				break;
			case ColumnType.GROUP_STRING:
				type = columnTypeRegister.getStringGroupPreference();
				break;
			case ColumnType.GROUP_BINARY_LOB:
				type = columnTypeRegister.getBinaryLobGroupPreference();
				break;
			case ColumnType.GROUP_CHARACTOR_LOB:
				type = columnTypeRegister.getCharactorLobGroupPreference();
				break;
			case ColumnType.GROUP_DATE:
				type = columnTypeRegister.getDateGroupPreference();
				break;
			case ColumnType.GROUP_TIME:
				type = columnTypeRegister.getTimeGroupPreference();
				break;
			default:
				type = columnTypeRegister.getStringGroupPreference();
			}
		}
		return type;
	}
}
