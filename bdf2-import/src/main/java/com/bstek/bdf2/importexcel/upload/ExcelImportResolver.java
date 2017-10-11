/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.bdf2.importexcel.upload;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.importexcel.context.ImportContext;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.parse.IExcelParser;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;

/**
 * 
 * @author william.jiang@bstek.com
 * @since 2.0.6
 * 
 */
@Service(ExcelImportResolver.BEAN_ID)
public class ExcelImportResolver {
	public static final String BEAN_ID = "bdf2.excelImportResolver";


	@FileResolver
	public void parseFile(UploadFile uploadFile, Map<String, Object> parameter) throws Exception {

			MultipartFile file = uploadFile.getMultipartFile();
			String name = file.getOriginalFilename();
			if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
				InputStream input = file.getInputStream();
				try {
					String bigData = (String)parameter.get("bigData");
					String startRow = (String)parameter.get("startRow");
					String endRow = (String)parameter.get("endRow");
					String excelModelId = (String)parameter.get("excelModelId");
					String fileExtension = null;
					if (name.endsWith(".xlsx")) {
						fileExtension = "xlsx";
					} else if (name.endsWith(".xls")) {
						fileExtension = "xls";
					}
					boolean supportBigData = false;
					if (StringUtils.isNotEmpty(bigData) && bigData.equals("true")) {
						supportBigData = true;
					}
					ImportContext.setParameters(parameter);
					IExcelParser excelParser = (IExcelParser) getExcelParser(fileExtension, supportBigData);
					ExcelDataWrapper excelDataWrapper = excelParser.parse(excelModelId, Integer.valueOf(startRow), Integer.valueOf(endRow), input);
					excelParser.put2Cache(excelDataWrapper);
				} finally {
					IOUtils.closeQuietly(input);
				}
			}
	}

	private IExcelParser getExcelParser(String fileExtension, boolean supportBigData) {
		Map<String, IExcelParser> map = ContextHolder.getApplicationContext().getBeansOfType(IExcelParser.class, true, true);
		for (IExcelParser parser : map.values()) {
			if (parser.supportFileExtension(fileExtension)) {
				if ((supportBigData && parser.supportBigData()) || !supportBigData && !parser.supportBigData()) {
					return parser;
				}
			}
		}
		return null;
	}
}
