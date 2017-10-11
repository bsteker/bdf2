package com.bstek.bdf2.jbpm4;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-22
 */
public class Jbpm4HibernateDao extends HibernateDao {
	public static final String BEAN_ID="bdf2.jbpm4HibernateDao";
	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString("bdf2.jbpm4.dataSourceName");
	}
}
