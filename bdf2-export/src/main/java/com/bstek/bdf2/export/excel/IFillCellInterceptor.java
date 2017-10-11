package com.bstek.bdf2.export.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public interface IFillCellInterceptor {
	public static final String BEAN_ID = "bdf2.defaultExcelFillCellInterceptor";
	
	/**
	 * 数据写入Excel的Cell单元格时触发的事件
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param value
	 */
	public void fillCellValue(Sheet sheet, Row row, Cell cell, Object value);
}
