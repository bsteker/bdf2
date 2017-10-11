/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.core.orm;

import javax.sql.DataSource;

/**
 * 数据源注册器.
 * 此类用于向系统中的数据源仓库注册一个数据源.
 * @author jacky.gao@bstek.com
 * @since 2.0
 */
public class DataSourceRegister {
	private String name = null;
	private DataSource dataSource = null;
	private boolean asDefault = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public boolean isAsDefault() {
		return asDefault;
	}
	public void setAsDefault(boolean asDefault) {
		this.asDefault = asDefault;
	}
}
