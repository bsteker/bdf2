/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.core.orm;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;

/**
 * 可在运行时切换实际使用的数据源的数据源代理类.
 * @author jacky.gao@bstek.com
 * @since 2.0
 */
public class DataSourceFactory implements FactoryBean<DataSource>{
	private DataSourceRepository dataSourceRepository;
	public DataSource getObject() throws Exception {
		return dataSourceRepository.getRealDataSource();
	}

	public Class<DataSource> getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public DataSourceRepository getDataSourceRepository() {
		return dataSourceRepository;
	}

	public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
		this.dataSourceRepository = dataSourceRepository;
	}
}
