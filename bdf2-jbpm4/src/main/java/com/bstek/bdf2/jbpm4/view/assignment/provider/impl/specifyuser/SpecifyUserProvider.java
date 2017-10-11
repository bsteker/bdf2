package com.bstek.bdf2.jbpm4.view.assignment.provider.impl.specifyuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.InternalAssignment;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-3-23
 */
@Component("bdf2.jbpm4.specifyUserProvider")
public class SpecifyUserProvider extends Jbpm4HibernateDao implements InitializingBean{
	private Map<String,ISpecifyUserProvider> providerMap;
	@DataProvider
	public void loadUsers(Page<Map<String,Object>> page,Criteria criteria){
		if(providerMap.size()==0){
			throw new RuntimeException("You must implemation ["+ISpecifyUserProvider.class.getName()+"] and then define in the spring context when use specify user assignment type!");
		}
		ISpecifyUserProvider provider=providerMap.values().iterator().next();
		Page<IUser> p=new Page<IUser>(page.getPageSize(),page.getPageNo());
		provider.loadUsers(p, criteria);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(IUser user:p.getEntities()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("username",user.getUsername());
			map.put("cname",user.getCname());
			list.add(map);
		}
		page.setEntities(list);
		page.setEntityCount(p.getEntityCount());
	}
	
	@DataResolver
	public void saveUser(Map<String,Object> map,String assignmentDefId){
		String username=(String)map.get("username");
		String cname=(String)map.get("cname");
		InternalAssignment internal=new InternalAssignment();
		internal.setAssignmentDefId(assignmentDefId);
		internal.setName(cname);
		internal.setValue(username);
		internal.setId(UUID.randomUUID().toString());
		Session session=this.getSessionFactory().openSession();
		try{
			session.save(internal);
		}finally{
			session.flush();
			session.close();
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		providerMap=this.getApplicationContext().getBeansOfType(ISpecifyUserProvider.class);
	}
}
