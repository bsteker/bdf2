package com.bstek.bdf2.jbpm4.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.service.IComponentControlService;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
public class ComponentControlServiceImpl extends Jbpm4HibernateDao implements IComponentControlService {

	public Collection<ComponentControl> getComponentControls(String processDefinitionId,String taskName) {
		String hql="from "+ComponentControl.class.getName()+" where taskName=:taskName and processDefinitionId=:processDefinitionId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("processDefinitionId", processDefinitionId);
		map.put("taskName", taskName);
		return this.query(hql, map);
	}

}
