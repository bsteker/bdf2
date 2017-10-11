package com.bstek.bdf2.export.extension;

import org.springframework.stereotype.Component;

import com.bstek.bdf2.export.excel.ExcelReportModelGenerater;

/**
 * @author matt.yao@bstek.com
 * @since 2.7
 */
@Component(ReportGenerater.BEAN_ID)
public class ReportGenerater extends ExcelReportModelGenerater {

	public static final String BEAN_ID = "bdf2.ReportGenerater";

}
