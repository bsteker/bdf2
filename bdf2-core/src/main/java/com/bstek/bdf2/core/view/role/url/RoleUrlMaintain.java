package com.bstek.bdf2.core.view.role.url;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.RoleResource;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.service.IUrlService;
import com.bstek.bdf2.core.view.role.RoleMaintain;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;

/**
 * @author Jacky.gao
 * @since 2013-2-17
 */
@Component("bdf2.roleUrlMaintain")
public class RoleUrlMaintain extends RoleMaintain {
	@Autowired
	@Qualifier(IUrlService.BEAN_ID)
	private IUrlService urlService;
	@DataProvider
	public List<Url> loadUrls(String parentId,String roleId) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		String hql="from "+Url.class.getName()+" u where u.companyId=:companyId";
		Map<String,Object> parameterMap=new HashMap<String,Object>();
		parameterMap.put("companyId",companyId);
		if(StringUtils.isEmpty(parentId)){
			hql+=" and u.parentId is null order by u.order asc";
		}else{
			hql+=" and u.parentId=:parentId order by u.order asc";			
			parameterMap.put("parentId",parentId);
		}
		List<Url> urls=this.query(hql, parameterMap);
		List<Url> allUrls=urlService.loadUrlsByRoleId(roleId);
		for(Url url:urls){
			for(Url roleUrl:allUrls){
				if(url.getId().equals(roleUrl.getId())){
					url.setUse(true);
					break;
				}
			}
		}
		return urls;
	}
	
	@Expose
	public void saveRoleUrls(String roleId,Collection<String> ids) throws Exception{
		Session session=this.getSessionFactory().openSession();
		try{
			Query query=session.createQuery("delete "+RoleResource.class.getName()+" rr where rr.roleId=:roleId").setString("roleId",roleId);
			query.executeUpdate();
			for(String urlId:ids){
				RoleResource rr=new RoleResource();
				rr.setId(UUID.randomUUID().toString());
				rr.setRoleId(roleId);
				rr.setUrlId(urlId);
				session.save(rr);			
			}
		}finally{
			session.flush();
			session.close();
		}
	}
}
