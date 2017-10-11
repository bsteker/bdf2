package com.bstek.bdf2.job;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-9
 */
public class JobHibernateDao extends HibernateDao{

	@Override
	protected String getModuleFixDataSourceName() {
		String name=Configure.getString("bdf2.jobDataSourceName");
		if(StringUtils.isNotEmpty(name)){
			return name;
		}
		return null;
	}
}
