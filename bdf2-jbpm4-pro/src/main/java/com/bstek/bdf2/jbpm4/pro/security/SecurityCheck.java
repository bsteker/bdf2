package com.bstek.bdf2.jbpm4.pro.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.pro.model.ComponentSecurity;

/**
 * @since 2013-1-28
 * @author Jacky.gao
 */
public class SecurityCheck extends Jbpm4HibernateDao {

	/**
	 * 判断指定人对指定的URL下的组件有没有访问权限
	 * 
	 */
	public int checkComponent(String processDefinitionId, String taskName, Set<String> componentSignature) {

		String hql = "from "
				+ ComponentSecurity.class.getName()
				+ " c where c.pdId = :processDefinitionId and c.taskName = :taskName and c.tag in (:signatures) order by c.priority desc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", taskName);
		map.put("processDefinitionId", processDefinitionId);
		map.put("signatures", componentSignature);
		List<ComponentSecurity> result = this.query(hql, map);
		return result.size() > 0 ? result.get(0).getType() : -1;
	}

	public static void refreshComponentSecurityMetadata() {
	}
}
