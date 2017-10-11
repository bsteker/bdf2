package com.bstek.bdf2.core.cache;

import java.util.List;

public class RefreshCacheRegister {

	public static final String BEAN_NAME = "bdf2.refreshCacheRegister";

	public List<String> beanMethodNames;

	public List<String> getBeanMethodNames() {
		return beanMethodNames;
	}

	public void setBeanMethodNames(List<String> beanMethodNames) {
		this.beanMethodNames = beanMethodNames;
	}

}
