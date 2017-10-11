package com.bstek.bdf2.core.view;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
public class OrmHibernateDao extends HibernateDao {

	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString("bdf2.ormDataSourceName");
	}
}
