package com.bstek.bdf2.export.interceptor;

import java.util.List;
import java.util.Map;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 * 
 */
public interface IDataInterceptor {

	public String getName();

	public String getDesc();

	public void interceptGridData(List<Map<String, Object>> list) throws Exception;

	public void interceptAutoFormData(List<Map<String, Object>> list) throws Exception;

}
