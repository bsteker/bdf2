package com.bstek.bdf2.core.orm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2013-2-14
 */
public class ParseResult {
	private Map<String,Object> valueMap=new LinkedHashMap<String,Object>();
	private StringBuffer assemblySql=new StringBuffer();
	public Map<String, Object> getValueMap() {
		return valueMap;
	}
	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}
	public StringBuffer getAssemblySql() {
		return assemblySql;
	}
	public void setAssemblySql(StringBuffer assemblySql) {
		this.assemblySql = assemblySql;
	}
}
