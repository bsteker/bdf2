package com.bstek.bdf2.rapido;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-4-11
 */
public class RapidoHibernateDao extends HibernateDao {
	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString(RapidoJdbcDao.BDF_RAPIDO_DATASOURCE_NAME);
	}
}
