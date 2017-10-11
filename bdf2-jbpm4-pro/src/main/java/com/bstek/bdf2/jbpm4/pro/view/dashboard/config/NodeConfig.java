package com.bstek.bdf2.jbpm4.pro.view.dashboard.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.pro.model.ComponentSecurity;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;

/**
 * @author Bing.Zhou
 * @since 2013-3-22
 */
@Component("bdf2.jbpm4.pro.nodeConfig")
public class NodeConfig extends Jbpm4HibernateDao {

	@DataProvider
	public Collection<ComponentSecurity> loadTaskConfig(String taskName,
			String processDefinitionId) {
		String hql = "from "
				+ ComponentSecurity.class.getName()
				+ " f where f.taskName = :taskName and pdId= :processDefinitionId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", taskName);
		map.put("processDefinitionId", processDefinitionId);
		return this.query(hql, map);
	}

	@DataResolver
	public void saveComponentSecurity(Collection<ComponentSecurity> FormSecurities) {
		Session session = getSession();
		for (ComponentSecurity formSecurity : FormSecurities) {
			EntityState state = EntityUtils.getState(formSecurity);
			if (EntityState.NEW.equals(state)) {
				formSecurity.setId(UUID.randomUUID().toString());
				session.save(formSecurity);
			} else if (EntityState.MODIFIED.equals(state)) {
				session.update(formSecurity);
			} else if (EntityState.DELETED.equals(state)) {
				session.delete(formSecurity);
			}
		}
	}
}
