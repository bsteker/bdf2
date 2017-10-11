package com.bstek.bdf2.core.orm.hibernate;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2013-3-21
 */
public class MappingResources {
	private String dataSourceRegisterName;
	private List<String> resources;
	
	public String getDataSourceRegisterName() {
		return dataSourceRegisterName;
	}
	public void setDataSourceRegisterName(String dataSourceRegisterName) {
		this.dataSourceRegisterName = dataSourceRegisterName;
	}
	public List<String> getResources() {
		return resources;
	}
	public void setResources(List<String> resources) {
		this.resources = resources;
	}
}
