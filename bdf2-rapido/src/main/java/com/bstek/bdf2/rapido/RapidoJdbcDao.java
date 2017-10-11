/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido;

import com.bstek.bdf2.core.orm.jdbc.JdbcDao;
import com.bstek.dorado.core.Configure;

public class RapidoJdbcDao extends JdbcDao {
	public static final String BDF_RAPIDO_DATASOURCE_NAME="bdf.rapido.dataSourceName";
	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString(RapidoJdbcDao.BDF_RAPIDO_DATASOURCE_NAME);
	}
}
