package com.bstek.bdf2.importexcel.parse.usermodel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.importexcel.manager.ExcelModelManager;
import com.bstek.bdf2.importexcel.model.CellWrapper;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.model.ExcelModel;
import com.bstek.bdf2.importexcel.model.ExcelModelDetail;
import com.bstek.bdf2.importexcel.model.GeneratePkStrategry;
import com.bstek.bdf2.importexcel.model.RowWrapper;
import com.bstek.bdf2.importexcel.parse.AbstractExcelParser;

/**
 * Excel解析处理
 * 
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Service(ExcelUserModelParser.BEAN_ID)
public class ExcelUserModelParser extends AbstractExcelParser {

	public static final String BEAN_ID = "bdf2.ExcelUserModelParser";
	public static final String EXCEL_DATA_CACHE_KEY = "excel_cache";
	public final Logger logger = Logger.getLogger(ExcelUserModelParser.class);
	public static final int MAX_EXCEL_ROW = 65536;
	@Autowired
	@Qualifier(ExcelModelManager.BEAN_ID)
	public ExcelModelManager excelModelManager;

	public ExcelDataWrapper parse(String excelModelId, int startRow, int endRow, InputStream in) throws Exception {
		ExcelDataWrapper excelDataWrapper = new ExcelDataWrapper();
		excelDataWrapper.setExcelModelId(excelModelId);
		excelDataWrapper.setValidate(true);
		ExcelModel excelModel = excelModelManager.findExcelModelById(excelModelId);
		List<ExcelModelDetail> excelModelDetails = excelModelManager.findExcelModelDetailByModelId(excelModelId);
		excelModel.setListExcelModelDetail(excelModelDetails);
		excelDataWrapper.setExcelModel(excelModel);
		if (!StringUtils.isNotEmpty(excelModel.getProcessor()) && StringUtils.isNotEmpty(excelModel.getDatasourceName())) {
			excelModel.setProcessor(this.getDefaultProcessor());
		} else {
			Assert.hasText(excelModel.getProcessor(), " the processor must not be empty ");
		}
		excelDataWrapper.setProcessor(excelModel.getProcessor());
		excelDataWrapper.setTableLabel(excelModel.getTableLabel());
		excelDataWrapper.setTableName(excelModel.getTableName());
		String primarykey = excelModel.getPrimaryKey();
		Workbook workbook = this.createWorkbook(in);
		Sheet sheet = null;
		if (!StringUtils.isNotEmpty(excelModel.getExcelSheetName())) {
			sheet = workbook.getSheetAt(0);
		} else {
			sheet = workbook.getSheet(excelModel.getExcelSheetName());
		}
		if (sheet == null) {
			throw new RuntimeException("上传的excel没有解析到合法的sheet值！");
		}
		if (startRow == 0 || startRow < sheet.getFirstRowNum() + 1) {
			startRow = sheet.getFirstRowNum() + 1;
		}
		if (endRow == 0 || endRow > sheet.getLastRowNum() + 1) {
			endRow = sheet.getLastRowNum() + 1;
		}
		if (endRow > MAX_EXCEL_ROW) {
			endRow = MAX_EXCEL_ROW;
		}
		Collection<RowWrapper> rowWrappers = new ArrayList<RowWrapper>();
		RowWrapper rowWrapper;
		for (int i = startRow - 1; i <= endRow - 1; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			rowWrapper = new RowWrapper();
			rowWrapper.setRow(row.getRowNum() + 1);
			List<CellWrapper> cellWrappers = new ArrayList<CellWrapper>();
			CellWrapper cellWrapper;
			for (ExcelModelDetail excelModelDetail : excelModelDetails) {
				cellWrapper = new CellWrapper();
				int excelColumn = excelModelDetail.getExcelColumn();
				if (excelColumn == 0) {
					throw new RuntimeException("上传的excel工作表" + sheet.getSheetName() + "第" + excelColumn + "列不存在！");
				}
				String excelColumnTableName = excelModelDetail.getTableColumn();
				Cell cell = row.getCell(excelColumn - 1);
				if (cell == null) {
					cellWrapper.setColumn(excelColumn);
				} else {
					cellWrapper.setColumn(cell.getColumnIndex() + 1);
				}
				cellWrapper.setColumnName(excelModelDetail.getTableColumn());
				cellWrapper.setName(excelModelDetail.getName());
				cellWrapper.setValidate(true);
				this.intercepterCellValue(cell, cellWrapper, excelModelDetail.getInterceptor());
				if (StringUtils.isNotEmpty(primarykey)) {
					if (primarykey.toLowerCase().equals(excelColumnTableName.toLowerCase()) && excelModel.getPrimaryKeyType().equals(GeneratePkStrategry.ASSIGNED.name())) {
						cellWrapper.setPrimaryKey(true);
						Object obj = cellWrapper.getValue();
						if (obj == null) {
							cellWrapper.setValue("<font color=\"red\">用户自定义主键不能为空!</font>");
							cellWrapper.setValidate(false);
						}
					}
				} else {
					cellWrapper.setPrimaryKey(false);
				}
				cellWrappers.add(cellWrapper);
			}
			rowWrapper.setCellWrappers(cellWrappers);
			rowWrapper.setValidate(true);
			for (CellWrapper cw : cellWrappers) {
				Boolean flag = cw.isValidate();
				if (!flag) {
					rowWrapper.setValidate(false);
					excelDataWrapper.setValidate(false);
					break;
				}
			}
			rowWrappers.add(rowWrapper);
		}
		excelDataWrapper.setRowWrappers(rowWrappers);
		return excelDataWrapper;
	}

	public boolean supportFileExtension(String fileExtension) {
		if (!StringUtils.isNotEmpty(fileExtension)) {
			return false;
		}
		if (fileExtension.equals("xls") || fileExtension.equals("xlsx")) {
			return true;
		}
		return false;
	}

	public boolean supportBigData() {
		return false;
	}

}
