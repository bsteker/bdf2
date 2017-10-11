package com.bstek.bdf2.importexcel.utils;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.DataSourceRegister;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public class ConnectionHelper {

	public Connection getConnection() throws Exception {
		return getConnection(null);
	}

	public Connection getConnection(String dataSourceName) throws Exception {
		Map<String, DataSourceRegister> dataSourceRegisters = DoradoContext.getAttachedWebApplicationContext().getBeansOfType(DataSourceRegister.class);
		for (DataSourceRegister dataSourceRegister : dataSourceRegisters.values()) {
			if (StringUtils.isNotEmpty(dataSourceName)) {
				if (dataSourceRegister.getName().equals(dataSourceName)) {
					return dataSourceRegister.getDataSource().getConnection();
				}
			} else {
				if (dataSourceRegister.isAsDefault()) {
					return dataSourceRegister.getDataSource().getConnection();
				}
			}

		}
		return null;
	}

}
