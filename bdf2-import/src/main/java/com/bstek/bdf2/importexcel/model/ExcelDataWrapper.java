package com.bstek.bdf2.importexcel.model;

import java.util.ArrayList;
import java.util.Collection;

public class ExcelDataWrapper implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public String excelModelId;
	/**
	 * excel对应的模型信息
	 */
	public ExcelModel excelModel;

	/**
	 * 解析的excel数据集合
	 */
	public Collection<RowWrapper> rowWrappers = new ArrayList<RowWrapper>();
	/**
	 * 数据是否通过验证
	 */
	public boolean validate;
	/**
	 * 采用的数据处理器
	 */
	public String processor;

	private String tableName;

	private String tableLabel;

	public ExcelModel getExcelModel() {
		return excelModel;
	}

	public Collection<RowWrapper> getRowWrappers() {
		return rowWrappers;
	}

	public void setExcelModel(ExcelModel excelModel) {
		this.excelModel = excelModel;
	}

	public void setRowWrappers(Collection<RowWrapper> rowWrappers) {
		this.rowWrappers = rowWrappers;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getExcelModelId() {
		return excelModelId;
	}

	public void setExcelModelId(String excelModelId) {
		this.excelModelId = excelModelId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableLabel() {
		return tableLabel;
	}

	public void setTableLabel(String tableLabel) {
		this.tableLabel = tableLabel;
	}
}
