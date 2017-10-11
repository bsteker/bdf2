package com.bstek.bdf2.authoritydelegation;


import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;

/**
 * @author lucas.yue@bstek.com
 *
 * 2013-4-23 下午12:20:29
 */
public class AuthoritydelegationHibernateDao extends HibernateDao {
	@Override
	protected String getModuleFixDataSourceName() {
		String name = Configure
				.getString("bdf2.authoritydelegationDataSourceName");
		if (StringUtils.isNotEmpty(name)) {
			return name;
		}
		return null;
	}
}
