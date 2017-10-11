package com.bstek.bdf2.jasperreports.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jacky.gao
 * @since 2013-5-11
 */
public interface IExporter {
	public static final String REPORT_FILE_PARAMETER_KEY="reportFile";
	public static final String FILE_SOURCE_PARAMETER_KEY="fileSource";
	public static final String DATASOURCE_TYPE_PARAMETER_KEY="dataSourceType";
	public static final String DATASOURCE_PROVIDER_PARAMETER_KEY="dataSourceProvider";
	public static final String REPORT_PARAMETERS_PARAMETER_KEY="reportParameters";
	public static final String CACHE_PARAMETER_KEY="cache";
	public static final String EXPORT_FILE_TYPE_PARAMETER_KEY="exportFileType";
	boolean isDisabled();
	boolean support(String targetFile);
	void export(HttpServletRequest req,HttpServletResponse res) throws Exception;
}
