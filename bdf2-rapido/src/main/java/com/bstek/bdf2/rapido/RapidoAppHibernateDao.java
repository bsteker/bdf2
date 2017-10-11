/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

public class RapidoAppHibernateDao extends HibernateDao {
	@Override
	protected String getModuleFixDataSourceName() {
		String rapidAppDSName=Configure.getString(RapidoAppJdbcDao.BDF_RAPIDO_APP_DATASOURCE_NAME);
		if(StringUtils.isEmpty(rapidAppDSName)){
			rapidAppDSName=super.getModuleFixDataSourceName();
		}
		return rapidAppDSName;
	}
}
