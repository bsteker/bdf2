package com.bstek.bdf2.importexcel;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.jdbc.JdbcDao;
import com.bstek.dorado.core.Configure;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public class ImportExcelJdbcDao extends JdbcDao {
	public static final String dataSourceRegisterName = "bdf2.import.dataSourceRegisterName";

	@Override
	protected String getModuleFixDataSourceName() {
		String name = Configure.getString(dataSourceRegisterName);
		if (StringUtils.isNotEmpty(name)) {
			return name;
		} else {
			return null;
		}
	}

}
