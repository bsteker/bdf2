package com.bstek.bdf2.importexcel.parse;

import java.io.InputStream;

import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public interface IExcelParser {

	public ExcelDataWrapper parse(String excelModelId, int startRow, int endRow, InputStream in) throws Exception;

	public void put2Cache(ExcelDataWrapper excelDataWrapper);

	public boolean supportFileExtension(String fileExtension);

	public boolean supportBigData();
}
