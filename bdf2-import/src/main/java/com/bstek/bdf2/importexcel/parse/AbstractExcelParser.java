package com.bstek.bdf2.importexcel.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.importexcel.exception.InterceptorException;
import com.bstek.bdf2.importexcel.interceptor.ICellDataInterceptor;
import com.bstek.bdf2.importexcel.model.CellWrapper;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.model.GeneratePkStrategry;
import com.bstek.bdf2.importexcel.processor.IExcelProcessor;
import com.bstek.bdf2.importexcel.processor.impl.DefaultExcelProcessor;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public abstract class AbstractExcelParser implements IExcelParser {

	public final Logger logger = Logger.getLogger(AbstractExcelParser.class);
	public static final String EXCEL_DATA_CACHE_KEY = "_excel_cache";

	public Workbook createWorkbook(InputStream in) throws IOException, InvalidFormatException {
		Assert.notNull(in, "参数 InputStream 不能为空！");
		if (!in.markSupported()) {
			in = new PushbackInputStream(in, 8);
		}
		if (POIFSFileSystem.hasPOIFSHeader(in)) {
			return new HSSFWorkbook(in);
		}
		if (POIXMLDocument.hasOOXMLHeader(in)) {
			return new XSSFWorkbook(OPCPackage.open(in));

		}
		throw new RuntimeException("上传的不是正确的Excel格式文件，不能创建工作薄");
	}

	public void intercepterCellValue(Cell cell, CellWrapper cellWrapper, String interceptor) throws Exception {
		if (cell == null) {
			cellWrapper.setValue(null);
		} else {
			int cellType = cell.getCellType();
			switch (cellType) {
			case Cell.CELL_TYPE_BLANK:
				cellWrapper.setValue(null);
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellWrapper.setValue(Boolean.valueOf(cell.getBooleanCellValue()));
				break;
			case Cell.CELL_TYPE_ERROR:
				cellWrapper.setValue(cell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellWrapper.setValue(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					cellWrapper.setValue(date);
				} else {
					cellWrapper.setValue(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellWrapper.setValue(cell.getStringCellValue());
				break;
			}
		}
		logger.debug("before interceptor column number[" + cellWrapper.getColumn() + "] cell value[" + cellWrapper.getValue() + "] cell intercepter[" + interceptor + "]");
		this.intercepterCellValue(cellWrapper, interceptor);
		logger.debug("after interceptor  cell value[" + cellWrapper.getValue() + "]");

	}

	public void intercepterCellValue(CellWrapper cellWrapper, String interceptor) throws Exception {
		if (StringUtils.isNotEmpty(interceptor)) {
			Object obj = null;
			try {
				obj = fireCellDataIntercepter(interceptor, cellWrapper.getValue());
			} catch (InterceptorException ex) {
				obj = ex.getMessage();
				cellWrapper.setValidate(false);
			}
			if (cellWrapper.isValidate()) {
				cellWrapper.setValue(obj);
			} else {
				cellWrapper.setValue("<font color=\"red\">" + obj + "</font>");
			}
		}
	}

	public Object fireCellDataIntercepter(String interceptor, Object cellValue) throws Exception {
		if (StringUtils.isNotEmpty(interceptor)) {
			if (ContextHolder.getApplicationContext().containsBean(interceptor)) {
				Object bean = ContextHolder.getApplicationContext().getBean(interceptor);
				if (bean instanceof ICellDataInterceptor) {
					ICellDataInterceptor cellDataInterceptor = (ICellDataInterceptor) bean;
					Object value = cellDataInterceptor.execute(cellValue);
					return value;
				} else {
					throw new RuntimeException("Spring Bean【" + interceptor + "】必须实现接口 com.bstek.bdf2.importexcel.intercepter.ICellDataIntercepter");
				}
			} else {
				throw new RuntimeException("Spring Bean【" + interceptor + "】不存在！");
			}
		}
		return cellValue;
	}

	public int fireProcessorInterceptor(String interceptor, Object value) throws Exception {
		if (StringUtils.isNotEmpty(interceptor)) {
			if (ContextHolder.getApplicationContext().containsBean(interceptor)) {
				Object bean = ContextHolder.getApplicationContext().getBean(interceptor);
				if (bean instanceof IExcelProcessor && value instanceof ExcelDataWrapper) {
					IExcelProcessor processor = (IExcelProcessor) bean;
					ExcelDataWrapper excelDataWrapper = (ExcelDataWrapper) value;
					return processor.execute(excelDataWrapper);
				} else {
					throw new RuntimeException("Spring Bean【" + interceptor + "】必须实现接口 com.bstek.bdf2.importexcel.processor.Processor");
				}
			} else {
				throw new RuntimeException("Spring Bean【" + interceptor + "】不存在！");
			}
		}
		return 0;
	}

	public int processParserdExcelData() throws Exception {
		int i = 0;
		ApplicationCache applicationCache = (ApplicationCache) ContextHolder.getBean(ApplicationCache.BEAN_ID);
		ExcelDataWrapper excelDataWrapper = (ExcelDataWrapper) applicationCache.getTemporaryCacheObject(getExcelCacheKey());
		if (excelDataWrapper != null) {
			String processor = excelDataWrapper.getProcessor();
			if (StringUtils.isNotEmpty(processor)) {
				if (excelDataWrapper.getRowWrappers().size() > 0) {
					if (excelDataWrapper.validate) {
						i = fireProcessorInterceptor(processor, excelDataWrapper);
						applicationCache.removeTemporaryCacheObject(getExcelCacheKey());
					} else {
						throw new RuntimeException("导入的Excel数据没有通过验证！");
					}
				} else {
					throw new RuntimeException("导入的Excel数据没有有效的记录！");
				}

			} else {
				throw new RuntimeException("解析的Excel数据没有对应的处理器！");
			}
		} else {
			throw new RuntimeException("缓存数据不存在！");
		}
		return i;
	}

	public String getExcelCacheKey() {
		return ContextHolder.getLoginUserName() + AbstractExcelParser.EXCEL_DATA_CACHE_KEY;
	}

	public void put2Cache(ExcelDataWrapper excelDataWrapper) {
		ApplicationCache cache = (ApplicationCache) ContextHolder.getBean(ApplicationCache.BEAN_ID);
		cache.putTemporaryCacheObject(getExcelCacheKey(), excelDataWrapper);
	}

	public ExcelDataWrapper getExcelDataWrapperCache() {
		ApplicationCache cache = (ApplicationCache) ContextHolder.getBean(ApplicationCache.BEAN_ID);
		return (ExcelDataWrapper) cache.getTemporaryCacheObject(getExcelCacheKey());
	}

	public void clearExcelDataWrapperCache() {
		ApplicationCache cache = (ApplicationCache) ContextHolder.getBean(ApplicationCache.BEAN_ID);
		cache.removeTemporaryCacheObject(getExcelCacheKey());
	}

	public Collection<String> getPrimaryTypes() {
		List<String> list = new ArrayList<String>();
		GeneratePkStrategry[] values = GeneratePkStrategry.values();
		for (GeneratePkStrategry g : values) {
			list.add(g.name());
		}
		return list;
	}

	public String getDefaultProcessor() {
		return DefaultExcelProcessor.BEAN_ID;
	}
}
