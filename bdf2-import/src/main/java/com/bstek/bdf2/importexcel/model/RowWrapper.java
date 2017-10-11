package com.bstek.bdf2.importexcel.model;

import java.util.Collection;

public class RowWrapper implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 当前行在Excel中行号
	 */
	private int row;
	/**
	 * 当前行所有列对应的单元格值对象
	 */
	private Collection<CellWrapper> cellWrappers;
	/**
	 * 是否通过验证
	 */
	private boolean validate;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public Collection<CellWrapper> getCellWrappers() {
		return cellWrappers;
	}

	public void setCellWrappers(Collection<CellWrapper> cellWrappers) {
		this.cellWrappers = cellWrappers;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}
}
