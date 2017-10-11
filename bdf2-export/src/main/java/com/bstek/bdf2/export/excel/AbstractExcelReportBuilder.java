package com.bstek.bdf2.export.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.bstek.bdf2.export.model.ReportGrid;
import com.bstek.bdf2.export.model.ReportGridHeader;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public abstract class AbstractExcelReportBuilder {
	public static final String DefaultSheetName = "Sheet1";

	public Workbook createWorkBook2003() {
		return new HSSFWorkbook();
	}

	public Workbook createWorkBook2007(int rowAccessWindowSize) {
		return new SXSSFWorkbook(rowAccessWindowSize);
	}

	public void writeFile(Workbook workbook, String fileLocation) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(new File(fileLocation));
		try {
			workbook.write(out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public void writeOutputStream(Workbook workbook, OutputStream out) throws IOException {
		workbook.write(out);
	}

	public Sheet createSheet(Workbook workbook, String sheetName) {
		if (StringUtils.isNotEmpty(sheetName)) {
			return workbook.createSheet(sheetName);
		} else {
			return workbook.createSheet(DefaultSheetName);
		}
	}

	public void calculateMaxHeaderLevel(ReportGrid gridModel, List<ReportGridHeader> gridHeaders) {
		int maxLevel = gridModel.getMaxHeaderLevel();
		for (ReportGridHeader header : gridHeaders) {
			if (header.getLevel() > maxLevel) {
				maxLevel = header.getLevel();
				gridModel.setMaxHeaderLevel(maxLevel);
			}
			if (header.getHeaders().size() > 0) {
				this.calculateMaxHeaderLevel(gridModel, header.getHeaders());
			}
		}
	}

	public void calculateGridHeadersByLevel(List<ReportGridHeader> columnHeaderModels, int level, List<ReportGridHeader> result) {
		for (ReportGridHeader reportGridHeaderModel : columnHeaderModels) {
			if (reportGridHeaderModel.getLevel() == level) {
				result.add(reportGridHeaderModel);
			} else if (reportGridHeaderModel.getHeaders().size() > 0) {
				calculateGridHeadersByLevel(reportGridHeaderModel.getHeaders(), level, result);
			}
		}
	}

	public void calculateBottomColumnHeader(List<ReportGridHeader> gridHeader, List<ReportGridHeader> result) {
		for (ReportGridHeader header : gridHeader) {
			if (header.getHeaders().size() == 0) {
				result.add(header);
			} else {
				this.calculateBottomColumnHeader(header.getHeaders(), result);
			}
		}
	}

	public int calculateGridHeaderColspan(ReportGridHeader headerModel) {
		if (headerModel.getHeaders().size() == 0) {
			return 1;
		} else {
			List<ReportGridHeader> result = new ArrayList<ReportGridHeader>();
			calculateBottomColumnHeader(headerModel.getHeaders(), result);
			return result.size();
		}
	}

}
