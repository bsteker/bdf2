package com.bstek.bdf2.uploader;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-5-3
 */
public class UploaderHibernateDao extends HibernateDao {

	@Override
	protected String getModuleFixDataSourceName() {
		if(StringUtils.isNotEmpty(Configure.getString("bdf2.upload.dataSourceName"))){
			return Configure.getString("bdf2.upload.dataSourceName");
		}else{
			return null;			
		}
	}

}
