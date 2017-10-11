package com.bstek.bdf2.core;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public abstract class CoreHibernateDao extends HibernateDao {
	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString("bdf2.coreDataSourceName");
	}
}
