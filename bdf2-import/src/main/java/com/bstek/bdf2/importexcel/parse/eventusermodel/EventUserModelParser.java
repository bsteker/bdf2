package com.bstek.bdf2.importexcel.parse.eventusermodel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.importexcel.manager.ExcelModelManager;
import com.bstek.bdf2.importexcel.model.CellWrapper;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.model.ExcelModel;
import com.bstek.bdf2.importexcel.model.ExcelModelDetail;
import com.bstek.bdf2.importexcel.model.GeneratePkStrategry;
import com.bstek.bdf2.importexcel.model.RowWrapper;
import com.bstek.bdf2.importexcel.parse.AbstractExcelParser;

public abstract class EventUserModelParser extends AbstractExcelParser implements IEventUserModelExecuter {

	public static final String BEAN_ID = "bdf2.HSSFProcess";

	public ExcelModelManager excelModelManager;
	private ExcelDataWrapper excelDataWrapper = new ExcelDataWrapper();
	private ExcelModel excelModel;
	private List<ExcelModelDetail> excelModelDetails;
	private int startRow = 0;
	private int endRow = 0;
	private String sheetName;

	private void parseExcelRow(int sheetIndex, String sheetName, int curRow, Map<Integer, Object> record) throws Exception {
		if (startRow != 0 && curRow + 1 < startRow) {
			return;
		}
		if (endRow != 0 && curRow + 1 > endRow) {
			return;
		}
		if (!StringUtils.isNotEmpty(excelModel.getExcelSheetName()) && sheetIndex != 0) {
			return;
		}
		if (StringUtils.isNotEmpty(excelModel.getExcelSheetName()) && !excelModel.getExcelSheetName().toLowerCase().equals(sheetName.toLowerCase())) {
			return;
		}
		RowWrapper rowWrapper = new RowWrapper();
		rowWrapper = new RowWrapper();
		rowWrapper.setRow(curRow + 1);
		List<CellWrapper> cellWrapperList = new ArrayList<CellWrapper>();
		CellWrapper cellWrapper;
		for (ExcelModelDetail excelModelDetail : excelModelDetails) {
			cellWrapper = new CellWrapper();
			String excelColumnTableName = excelModelDetail.getTableColumn();
			int excelColumn = excelModelDetail.getExcelColumn();
			if (excelColumn == 0) {
				continue;
			}
			cellWrapper.setColumn(excelColumn);
			cellWrapper.setColumnName(excelModelDetail.getTableColumn());
			cellWrapper.setName(excelModelDetail.getName());
			cellWrapper.setValidate(true);
			cellWrapper.setValue(record.get(excelColumn - 1));

			intercepterCellValue(cellWrapper, excelModelDetail.getInterceptor());
			String primarykey = excelModel.getPrimaryKey();
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
			cellWrapperList.add(cellWrapper);
		}
		rowWrapper.setCellWrappers(cellWrapperList);
		rowWrapper.setValidate(true);
		for (CellWrapper cw : cellWrapperList) {
			Boolean flag = cw.isValidate();
			if (!flag) {
				rowWrapper.setValidate(false);
				excelDataWrapper.setValidate(false);
				break;
			}
		}
		excelDataWrapper.getRowWrappers().add(rowWrapper);
	}

	public ExcelDataWrapper parse(String excelModelId, int startRow, int endRow, InputStream inputStream) throws Exception {
		this.excelModelManager = ContextHolder.getBean(ExcelModelManager.BEAN_ID);
		this.startRow = startRow;
		this.endRow = endRow;
		this.beforeParse(excelModelId);
		this.execute(inputStream, new ParseExcelRowMapper());
		return excelDataWrapper;
	}

	public ExcelDataWrapper beforeParse(String excelModelId) throws Exception {
		excelDataWrapper.setExcelModelId(excelModelId);
		excelDataWrapper.setValidate(true);
		excelModel = excelModelManager.findExcelModelById(excelModelId);
		excelModelDetails = excelModelManager.findExcelModelDetailByModelId(excelModelId);
		excelModel.setListExcelModelDetail(excelModelDetails);
		excelDataWrapper.setExcelModel(excelModel);
		this.setSheetName(excelModel.getExcelSheetName());
		if (!StringUtils.isNotEmpty(excelModel.getProcessor()) && StringUtils.isNotEmpty(excelModel.getDatasourceName())) {
			excelModel.setProcessor(this.getDefaultProcessor());
		} else {
			Assert.hasText(excelModel.getProcessor(), " the processor must not be empty ");
		}
		excelDataWrapper.setProcessor(excelModel.getProcessor());
		excelDataWrapper.setTableLabel(excelModel.getTableLabel());
		excelDataWrapper.setTableName(excelModel.getTableName());
		return excelDataWrapper;
	}

	private class ParseExcelRowMapper implements IParseExcelRowMapper {
		public void executeRowMapper(int sheetIndex, String sheetName, int curRow, Map<Integer, Object> record) throws Exception {
			parseExcelRow(sheetIndex, sheetName, curRow, record);
		}
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public boolean supportBigData() {
		return true;
	}
}
