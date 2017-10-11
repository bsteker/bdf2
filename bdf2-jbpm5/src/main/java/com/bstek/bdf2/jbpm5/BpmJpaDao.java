package com.bstek.bdf2.jbpm5;

import com.bstek.bdf2.core.orm.jpa.JpaDao;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-15
 */
public class BpmJpaDao extends JpaDao {

	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString("bdf2.bpmDataSourceName");
	}
}
