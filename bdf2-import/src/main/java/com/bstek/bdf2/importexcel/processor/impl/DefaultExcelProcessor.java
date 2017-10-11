package com.bstek.bdf2.importexcel.processor.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.importexcel.ImportExcelJdbcDao;
import com.bstek.bdf2.importexcel.manager.ExcelModelManager;
import com.bstek.bdf2.importexcel.model.CellWrapper;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.model.ExcelModel;
import com.bstek.bdf2.importexcel.model.ExcelModelDetail;
import com.bstek.bdf2.importexcel.model.GeneratePkStrategry;
import com.bstek.bdf2.importexcel.model.RowWrapper;
import com.bstek.bdf2.importexcel.processor.IExcelProcessor;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Service(DefaultExcelProcessor.BEAN_ID)
public class DefaultExcelProcessor extends ImportExcelJdbcDao implements IExcelProcessor {
	public static final String BEAN_ID = "bdf2.DefaultProcessor";
	public final Log logger = LogFactory.getLog(DefaultExcelProcessor.class);

	@Autowired
	@Qualifier(ExcelModelManager.BEAN_ID)
	public ExcelModelManager excelModelManager;

	public String getName() {
		return "系统默认单表解析入库处理类";
	}

	@Transactional
	public int execute(ExcelDataWrapper excelDataWrapper) throws Exception {
		if (!excelDataWrapper.isValidate()) {
			throw new RuntimeException("当前数据没有通过验证,不能解析入库！");
		}
		ExcelModel excelModel = excelDataWrapper.getExcelModel();
		ExcelModelDetail excelModelDetail = null;
		if (StringUtils.isNotEmpty(excelModel.getPrimaryKey())) {
			excelModelDetail = excelModelManager.findExcelModelDetailByModelIdAndPrimaryKey(excelModel.getId(), excelModel.getPrimaryKey());
		}

		SupportSequenceDb type = this.validateDb(excelModel.getDatasourceName());
		if (type != null) {
			excelModel.setDbType(type.name());
		}
		Collection<RowWrapper> rowWrappers = excelDataWrapper.getRowWrappers();
		String tableName = excelDataWrapper.getTableName();
		int count = 0;
		for (RowWrapper rowWrapper : rowWrappers) {
			Collection<CellWrapper> cellWrappers = rowWrapper.getCellWrappers();
			// 系统定义主键策略
			if (excelModelDetail == null && cellWrappers.size() > 0) {
				if (StringUtils.isNotEmpty(excelModel.getPrimaryKey())) {
					if (excelModel.getPrimaryKeyType().equals(GeneratePkStrategry.VMID.name())) {
						CellWrapper cellWrapper = new CellWrapper();
						cellWrapper.setPrimaryKey(true);
						cellWrapper.setColumnName(excelModel.getPrimaryKey());
						cellWrapper.setValue(UUID.randomUUID().toString());
						cellWrappers.add(cellWrapper);
					} else if (excelModel.getPrimaryKeyType().equals(GeneratePkStrategry.UUID.name())) {
						CellWrapper cellWrapper = new CellWrapper();
						cellWrapper.setPrimaryKey(true);
						cellWrapper.setColumnName(excelModel.getPrimaryKey());
						cellWrapper.setValue(UUID.randomUUID().toString());
						cellWrappers.add(cellWrapper);
					}
				}
			}
			List<String> columnNameList = new ArrayList<String>();
			List<Object> columnValueList = new ArrayList<Object>();
			for (CellWrapper cellWrapper : cellWrappers) {
				String columnName = cellWrapper.getColumnName();
				if (StringUtils.isNotEmpty(columnName)) {
					Object columnValue = cellWrapper.getValue();
					columnNameList.add(columnName);
					columnValueList.add(columnValue);
					if (cellWrapper.isPrimaryKey() && excelModel.getPrimaryKeyType().equals(GeneratePkStrategry.ASSIGNED.name())) {
						String sql = "select count(1) from " + tableName + " where " + columnName + "=?";
						int sum = this.getJdbcTemplate(excelModel.getDatasourceName()).queryForObject(sql, new Object[] { columnValue }, Integer.class);
						if (sum > 0) {
							throw new RuntimeException("数据库表[" + tableName + "]的字段[" + columnName + "]为主键，键值[" + columnValue + "]重复！");
						}
					}
				}

			}
			int n = this.insertRowWrapper2Table(excelModel, columnNameList, columnValueList);
			if (n == 1) {
				count++;
			}
		}
		logger.info("解析excel入库成功，导入[" + count + "]条数据！");
		return count;

	}

	private int insertRowWrapper2Table(ExcelModel excelModel, List<String> columnNameList, List<Object> columnValueList) throws Exception {
		String tableName = excelModel.getTableName();
		StringBuffer sb = new StringBuffer("insert into ");
		sb.append(tableName + "( ");
		StringBuffer sbValues = new StringBuffer(" values(");
		int j = 1;
		if (StringUtils.isNotEmpty(excelModel.getPrimaryKey())) {
			if (excelModel.getPrimaryKeyType().equals(GeneratePkStrategry.SEQUENCE.name())) {
				if (excelModel.getDbType().equals(SupportSequenceDb.oracle.name())) {
					sb.append(excelModel.getPrimaryKey());
					sbValues.append(this.getOracleNextval(excelModel.getSequenceName()));
				} else if (excelModel.getDbType().equals(SupportSequenceDb.db2.name())) {
					sb.append(excelModel.getPrimaryKey());
					sbValues.append(this.getDB2Nextval(excelModel.getSequenceName()));
				}
				if (columnNameList.size() > 0 && columnValueList.size() > 0) {
					sb.append(",");
					sbValues.append(",");
				}
			}
		}
		for (String s : columnNameList) {
			if (columnNameList.size() == j) {
				sb.append(s);
				sbValues.append("?");
			} else {
				sb.append(s + ",");
				sbValues.append("?,");
			}
			j++;

		}
		sb.append(" )");
		sbValues.append(" )");
		String sql = sb.append(sbValues).toString();
		return this.getJdbcTemplate(excelModel.getDatasourceName()).update(sql, columnValueList.toArray());
	}

	private SupportSequenceDb validateDb(String datasourceName) {
		return this.getJdbcTemplate(datasourceName).execute(new ConnectionCallback<SupportSequenceDb>() {
			public SupportSequenceDb doInConnection(Connection con) throws SQLException, DataAccessException {
				DatabaseMetaData databaseMetaData = con.getMetaData();
				String databaseProductName = databaseMetaData.getDatabaseProductName();
				if (org.apache.commons.lang.StringUtils.containsIgnoreCase(databaseProductName, SupportSequenceDb.oracle.name())) {
					return SupportSequenceDb.oracle;
				} else if (org.apache.commons.lang.StringUtils.containsIgnoreCase(databaseProductName, SupportSequenceDb.db2.name())) {
					return SupportSequenceDb.db2;
				}
				return null;
			}
		});
	}

	public String getOracleNextval(String sequenceName) {
		return sequenceName + ".nextval";
	}

	public String getDB2Nextval(String sequenceName) {
		return "NEXTVAL FOR " + sequenceName;
	}

	public ExcelModelManager getExcelModelManager() {
		return excelModelManager;
	}

	public void setExcelModelManager(ExcelModelManager excelModelManager) {
		this.excelModelManager = excelModelManager;
	}

	private enum SupportSequenceDb {
		oracle, db2
	}

}
