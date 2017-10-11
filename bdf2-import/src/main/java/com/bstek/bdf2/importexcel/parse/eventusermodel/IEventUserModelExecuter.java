package com.bstek.bdf2.importexcel.parse.eventusermodel;

import java.io.InputStream;

public interface IEventUserModelExecuter {
	
	public void execute(InputStream inputStream, IParseExcelRowMapper parseExcelRowMapper) throws Exception;
}
