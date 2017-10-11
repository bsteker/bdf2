package com.bstek.bdf2.core;

import com.bstek.bdf2.core.orm.jdbc.JdbcDao;
import com.bstek.dorado.core.Configure;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public abstract class CoreJdbcDao extends JdbcDao {
	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString("bdf2.coreDataSourceName");
	}

	@Override
	protected String buildFieldName(String name) {
		if(name==null)return name;
		StringBuffer sb=new StringBuffer();
		for(char ch:name.toCharArray()){
			boolean upper=Character.isUpperCase(ch);
			if(upper){
				sb.append("_");
			}
			sb.append(Character.toUpperCase(ch));
		}
		sb.append("_");
		return sb.toString();
	}
}
