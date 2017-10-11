package com.bstek.bdf2.importexcel.parse.eventusermodel;

import java.util.Map;

public interface IParseExcelRowMapper {

	public void executeRowMapper(int sheetIndex, String sheetName, int curRow, Map<Integer,Object> record) throws Exception;

}
