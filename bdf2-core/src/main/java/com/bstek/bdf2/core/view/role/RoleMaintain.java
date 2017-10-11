package com.bstek.bdf2.core.view.role;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Role;
import com.bstek.bdf2.core.model.RoleMember;
import com.bstek.bdf2.core.model.RoleResource;
import com.bstek.bdf2.core.model.UrlComponent;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

@Component("bdf2.roleMaintain")
public class RoleMaintain extends CoreHibernateDao{
	@DataProvider
	public void loadRoles(Page<Role> page,Criteria criteria) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		String hql="from "+Role.class.getName()+" where type=:type and companyId=:companyId";
		ParseResult result=this.parseCriteria(criteria,true,"r");
		Map<String,Object> valueMap;
		if(result!=null){
			StringBuffer sb=result.getAssemblySql();
			valueMap=result.getValueMap();
			hql="from "+Role.class.getName()+" r where "+sb.toString()+" and r.type=:type and r.companyId=:companyId";
		}else{
			valueMap=new HashMap<String,Object>();
		}
		String countHql="select count(*) "+hql;
		hql+=" order by createDate desc";
		valueMap.put("type",Role.NORMAL_TYPE);
		valueMap.put("companyId",companyId);
		this.pagingQuery(page, hql,countHql,valueMap);
	}
	
	
	@DataResolver
	public void saveRoles(Collection<Role> roles) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		Session session=this.getSessionFactory().openSession();
		try{
			for(Role role:roles){
				EntityState state=EntityUtils.getState(role);
				if(state.equals(EntityState.NEW)){
					role.setId(UUID.randomUUID().toString());
					role.setCompanyId(companyId);
					role.setCreateDate(new Date());
					role.setType(Role.NORMAL_TYPE);
					session.save(role);
				}
				if(state.equals(EntityState.MODIFIED)){
					session.update(role);
				}
				if(state.equals(EntityState.DELETED)){
					session.createQuery("delete "+RoleResource.class.getName()+" where roleId=:roleId").setString("roleId",role.getId()).executeUpdate();
					session.createQuery("delete "+RoleMember.class.getName()+" where roleId=:roleId").setString("roleId",role.getId()).executeUpdate();
					session.createQuery("delete "+UrlComponent.class.getName()+" where roleId=:roleId").setString("roleId",role.getId()).executeUpdate();
					session.delete(role);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public void refreshUrlSecurityMetadata(){
		SecurityUtils.refreshUrlSecurityMetadata();
	}
	@Expose
	public void refreshAllSecurityMetadata(){
		SecurityUtils.refreshUrlSecurityMetadata();
		SecurityUtils.refreshComponentSecurityMetadata();
	}
	@Expose
	public void refreshComponentSecurityMetadata(){
		SecurityUtils.refreshComponentSecurityMetadata();
	}
}
