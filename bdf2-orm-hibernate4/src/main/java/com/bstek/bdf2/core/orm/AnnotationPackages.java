package com.bstek.bdf2.core.orm;

import java.util.List;

/**
 * @since 2013-1-16
 * @author Jacky.gao
 */
public class AnnotationPackages {
	private List<String> scanPackages;
	private String dataSourceRegisterName;
	public List<String> getScanPackages() {
		return scanPackages;
	}

	public void setScanPackages(List<String> scanPackages) {
		this.scanPackages = scanPackages;
	}

	public String getDataSourceRegisterName() {
		return dataSourceRegisterName;
	}

	public void setDataSourceRegisterName(String dataSourceRegisterName) {
		this.dataSourceRegisterName = dataSourceRegisterName;
	}
}
