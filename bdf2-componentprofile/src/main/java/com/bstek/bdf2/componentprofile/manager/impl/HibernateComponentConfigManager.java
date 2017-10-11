/*
 * @author Bing
 * @since 2013-03-05
 */
package com.bstek.bdf2.componentprofile.manager.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf2.componentprofile.manager.IComponentConfigManager;
import com.bstek.bdf2.componentprofile.model.ComponentConfig;
import com.bstek.bdf2.componentprofile.model.ComponentConfigMember;
import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.variant.Record;

public class HibernateComponentConfigManager extends HibernateDao implements IComponentConfigManager {

	@SuppressWarnings("unchecked")
	public Collection<ComponentConfig> loadComponentConfigsByName(String profileKey) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComponentConfig.class);
		dc.add(Restrictions.eq("name", profileKey));
		dc.createAlias("componentConfigMembers", "m", CriteriaSpecification.LEFT_JOIN);
		dc.addOrder(Order.asc("m.order"));
		return (List<ComponentConfig>) this.query(dc);
	}

	@SuppressWarnings("unchecked")
	public ComponentConfig loadComponentConfig(String controlId, String name) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComponentConfig.class);
		dc.add(Restrictions.eq("name", name));
		dc.add(Restrictions.eq("controlId", controlId));
		dc.createAlias("componentConfigMembers", "m", CriteriaSpecification.LEFT_JOIN);
		dc.addOrder(Order.asc("m.order"));
		List<ComponentConfig> componentConfigs = (List<ComponentConfig>) this.query(dc);
		return componentConfigs.isEmpty() ? null : componentConfigs.get(0);
	}

	@SuppressWarnings("unchecked")
	public Collection<ComponentConfig> loadComponentConfigsByViewName(String viewName) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComponentConfig.class);
		dc.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		dc.add(Restrictions.like("controlId", viewName + ".%"));
		dc.createAlias("componentConfigMembers", "m", CriteriaSpecification.LEFT_JOIN);
		dc.addOrder(Order.asc("m.order"));
		return (List<ComponentConfig>) this.query(dc);
	}

	@SuppressWarnings("unchecked")
	public Collection<ComponentConfig> loadComponentConfigs() {
		DetachedCriteria dc = DetachedCriteria.forClass(ComponentConfig.class);
		dc.createAlias("componentConfigMembers", "m", CriteriaSpecification.LEFT_JOIN);
		dc.addOrder(Order.asc("m.order"));
		return (List<ComponentConfig>) this.query(dc);
	}

	@SuppressWarnings("unchecked")
	public void deleteComponentProfileByControlId(String controlId, String name) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ComponentConfig.class);
		detachedCriteria.add(Restrictions.eq("controlId", controlId));
		detachedCriteria.add(Restrictions.eq("name", name));
		Session session = this.getSessionFactory().openSession();
		try {
			org.hibernate.Criteria criteria = detachedCriteria.getExecutableCriteria(session);
			List<ComponentConfig> list = criteria.list();
			if (list.size() > 0) {
				String hql = "delete " + ComponentConfigMember.class.getName()
						+ " m where m.componentConfig.id = :configId";
				session.createQuery(hql).setString("configId", list.get(0).getId()).executeUpdate();
				session.delete(list.get(0));
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	@Transactional
	public void insertComponentConfigMembers(ComponentConfig componentConfig, Collection<Record> members) {
		Session session = this.getSessionFactory().openSession();
		try {
			session.save(componentConfig);
			for (Record record : members) {
				session.save(record2ConfigMember(componentConfig, record));
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	ComponentConfigMember record2ConfigMember(ComponentConfig componentConfig, Record record) {
		ComponentConfigMember componentConfigMember = new ComponentConfigMember();
		componentConfigMember.setId(UUID.randomUUID().toString());
		componentConfigMember.setControlType(record.getString("controlType"));
		componentConfigMember.setControlName(record.getString("controlName"));
		componentConfigMember.setOrder(record.getInt("order"));
		componentConfigMember.setParentControlName(record.getString("parentControl"));
		componentConfigMember.setCaption(record.getString("caption"));
		componentConfigMember.setWidth(record.getString("width"));
		componentConfigMember.setColSpan(record.getInt("colSpan"));
		componentConfigMember.setRowSpan(record.getInt("rowSpan"));
		componentConfigMember.setVisible(record.getBoolean("visible"));
		componentConfigMember.setComponentConfig(componentConfig);
		componentConfig.addMember(componentConfigMember);
		return componentConfigMember;
	}

	public Collection<ComponentConfigMember> findComponentConfigMemberByConfigId(String configId) {
		String hql = "from " + ComponentConfigMember.class.getName()
				+ " m where m.configId = :configId order by m.order";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("configId", configId);
		return this.query(hql, parameterMap);
	}

	@Override
	protected String getModuleFixDataSourceName() {
		return Configure.getString("bdf2.componentprofileSourceName");
	}
}
