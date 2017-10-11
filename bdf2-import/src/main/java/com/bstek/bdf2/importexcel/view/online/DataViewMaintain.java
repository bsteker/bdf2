package com.bstek.bdf2.importexcel.view.online;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.bstek.bdf2.importexcel.manager.ExcelModelManager;
import com.bstek.bdf2.importexcel.model.CellWrapper;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.model.ExcelModelDetail;
import com.bstek.bdf2.importexcel.model.RowWrapper;
import com.bstek.bdf2.importexcel.parse.usermodel.ExcelUserModelParser;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.view.widget.grid.DataColumn;
import com.bstek.dorado.view.widget.grid.DataGrid;
import com.bstek.dorado.view.widget.grid.RowNumColumn;

/**
 * 缓存数据预览
 * 
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Controller("bdf2.DataViewMaintain")
public class DataViewMaintain {
	private static final String COLUMN_WIDTH = "150";

	@Autowired
	@Qualifier(ExcelModelManager.BEAN_ID)
	public ExcelModelManager excelModelManager;
	
	@Autowired
	@Qualifier(ExcelUserModelParser.BEAN_ID)
	public ExcelUserModelParser excelParser;

	@DataProvider
	public List<Map<String, Object>> loadQueryData() throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ExcelDataWrapper data = excelParser.getExcelDataWrapperCache();
		if (data == null) {
			return list;
		}
		Collection<RowWrapper> rows = data.getRowWrappers();
		for (RowWrapper row : rows) {
			Collection<CellWrapper> cells = row.getCellWrappers();
			Map<String, Object> map = new HashMap<String, Object>();
			for (CellWrapper cell : cells) {
				map.put(cell.getName(), cell.getValue());
			}
			list.add(map);
		}
		return list;
	}

	public void onInit(DefaultEntityDataType dataTypeData) throws Exception {
		ExcelDataWrapper data = excelParser.getExcelDataWrapperCache();
		if (data == null) {
			return;
		}
		BasePropertyDef pd;
		for (ExcelModelDetail detail : data.getExcelModel().getListExcelModelDetail()) {
			pd = new BasePropertyDef();
			pd.setName(detail.getName());
			dataTypeData.addPropertyDef(pd);
		}
	}

	public void onInit(DataGrid dataGridData) throws Exception {
		ExcelDataWrapper data = excelParser.getExcelDataWrapperCache();
		if (data == null) {
			return;
		}
		List<ExcelModelDetail> details = data.getExcelModel().getListExcelModelDetail();
		DataColumn column;
		if(!details.isEmpty()){
			RowNumColumn rowNumColumn=new RowNumColumn();
			rowNumColumn.setWidth("30");
			dataGridData.addColumn(rowNumColumn);
			for (ExcelModelDetail detail : details) {
				column = new DataColumn();
				column.setName(detail.getName());
				column.setCaption(detail.getName() + "(第" + detail.getExcelColumn() + "列)");
				column.setWidth(COLUMN_WIDTH);
				dataGridData.addColumn(column);
			}
		}
	}
}
