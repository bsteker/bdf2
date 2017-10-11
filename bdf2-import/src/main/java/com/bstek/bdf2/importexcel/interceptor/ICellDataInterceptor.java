package com.bstek.bdf2.importexcel.interceptor;

/**
 * Excel单元格拦截处理类
 * 
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public interface ICellDataInterceptor {

	/**
	 * 名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @param cellValue
	 * @return
	 * @throws Exception
	 */
	public Object execute(Object cellValue) throws Exception;
}
