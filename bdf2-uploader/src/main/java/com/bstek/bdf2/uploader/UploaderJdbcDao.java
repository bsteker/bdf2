package com.bstek.bdf2.uploader;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.jdbc.JdbcDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-5-20
 */
public class UploaderJdbcDao extends JdbcDao {
	@Override
	protected String getModuleFixDataSourceName() {
		if(StringUtils.isNotEmpty(Configure.getString("bdf2.upload.dataSourceName"))){
			return Configure.getString("bdf2.upload.dataSourceName");
		}else{
			return null;			
		}
	}
}
