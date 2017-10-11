package com.bstek.bdf2.jasperreports;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-5-11
 */
public class ReportHibernateDao extends HibernateDao {
	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString("bdf2.jasperreports.dataSourceName");
	}
}
