package com.bstek.bdf2.profile;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public class ProfileHibernateDao extends HibernateDao {
	@Override
	protected String getModuleFixDataSourceName() {
		String dataSourceName=Configure.getString("bdf2.profile.dataSourceName");
		if(StringUtils.isNotEmpty(dataSourceName)){
			return dataSourceName;			
		}else{
			return null;
		}
	}

}
