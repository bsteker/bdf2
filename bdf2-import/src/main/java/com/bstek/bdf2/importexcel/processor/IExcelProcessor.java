package com.bstek.bdf2.importexcel.processor;

import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;

/**
 * 对要导入的Excel内容的处理类
 * 
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public interface IExcelProcessor {
	public String getName();

	/**
	 * @param excelDataWrapper
	 *            一个包装了Excel信息的集合
	 * @return 导入处理成功记录数
	 * @throws Exception
	 */
	public int execute(ExcelDataWrapper excelDataWrapper) throws Exception;
}
