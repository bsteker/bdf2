package com.bstek.bdf2.export.excel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

@Component(value=IFillCellInterceptor.BEAN_ID)
public class DefaultFillCellInterceptor implements IFillCellInterceptor {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public void fillCellValue(Sheet sheet, Row row, Cell cell, Object value) {
		if (value != null) {
			if (value instanceof Date) {
				String result = sdf.format(value);
				if (result.endsWith("00:00:00")) {
					result = result.substring(0, 11);
				}
				cell.setCellValue(result);
			} else if (value instanceof Double) {
				cell.setCellValue((Double) value);
			} else if (value instanceof Integer) {
				cell.setCellValue((Integer) value);
			} else if (value instanceof Byte) {
				cell.setCellValue((Byte) value);
			} else if (value instanceof Short) {
				cell.setCellValue((Short) value);
			} else if (value instanceof Boolean) {
				cell.setCellValue((Boolean) value);
			} else if (value instanceof Long) {
				cell.setCellValue((Long) value);
			} else if (value instanceof Float) {
				cell.setCellValue((Float) value);
			} else if (value instanceof BigDecimal) {
				double doubleVal = ((BigDecimal) value).doubleValue();  
				cell.setCellValue(doubleVal);
			} else {
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(value.toString());
			}
		} else {
			cell.setCellValue("");
		}
	}

}
