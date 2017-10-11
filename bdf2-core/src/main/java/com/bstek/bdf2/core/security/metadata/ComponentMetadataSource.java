package com.bstek.bdf2.core.security.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.model.Role;
import com.bstek.bdf2.core.model.RoleMember;
import com.bstek.bdf2.core.model.UrlComponent;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.attribute.AttributeType;
import com.bstek.bdf2.core.security.attribute.SecurityConfigAttribute;
import com.bstek.bdf2.core.service.IRoleService;
import com.bstek.bdf2.core.service.IUrlService;

/**
 * @since 2013-1-28
 * @author Jacky.gao
 */
public class ComponentMetadataSource implements InitializingBean {
	private static final String COMPONENT_KEY_START="__componentsecure:";
	public static final String BEAN_ID="bdf2.componentMetadataSource";
	private ApplicationCache applicationCache;
	private IRoleService roleService;
	private IUrlService urlService;
	public Collection<ConfigAttribute> getAttributes(String url,String componentId)
			throws IllegalArgumentException {
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			return null;
		}
		String orgCompanyId=user.getCompanyId();
		Map<String,Collection<ConfigAttribute>> attributeMaps=null;
		String[] companyIds=orgCompanyId.split(",");
		for(String companyId:companyIds){
			String urlId=getUrlId(companyId, url);
			attributeMaps=getMetadata(urlId);
			if(attributeMaps==null){
				url=url.substring(1);
				urlId=getUrlId(companyId, url);
				attributeMaps=getMetadata(urlId);
			}
			if(attributeMaps!=null)break;
		}
		if(attributeMaps==null){
			return null;
		}
		Collection<ConfigAttribute> attributes=attributeMaps.get(componentId);
		if(attributes==null){
			return new ArrayList<ConfigAttribute>();
		}	
		return attributes;
	}

	@SuppressWarnings("unchecked")
	public Map<String,Collection<ConfigAttribute>> getMetadata(String urlId){
		Map<String,Collection<ConfigAttribute>> map=SecurityUtils.getComponentsMetadata();
		if(map!=null){
			return map;
		}
		return (Map<String,Collection<ConfigAttribute>>)applicationCache.getCacheObject(urlId);
	}
	
	public void initComponentMetadata(){
		Map<String,Map<String,Collection<ConfigAttribute>>> cacheMap=new HashMap<String,Map<String,Collection<ConfigAttribute>>>();
		for(Role role:roleService.loadAllRoles()){
			if(!role.isEnabled()){
				continue;
			}
			role.setRoleMembers(roleService.loadRoleMemberByRoleId(role.getId()));
			role.setUrlComponents(urlService.loadComponentUrlsByRoleId(role.getId()));
			String companyId=role.getCompanyId();
			for(UrlComponent uc:role.getUrlComponents()){
				Collection<ConfigAttribute> configAttributes=this.buildConfigAttributes(role.getRoleMembers(),uc,role.getCompanyId());
				String targetUrl=uc.getUrl().getUrl();
				targetUrl=((targetUrl==null)?"":targetUrl);
				String urlId=getUrlId(companyId, targetUrl);
				Map<String,Collection<ConfigAttribute>> urlMap=null;
				if(cacheMap.containsKey(urlId)){
					urlMap=cacheMap.get(urlId);
				}else{
					urlMap=new HashMap<String,Collection<ConfigAttribute>>();
					cacheMap.put(urlId, urlMap);
				}
				String componentId=uc.getComponent().getComponentId()+"|"+uc.getAuthorityType().toString();
				Collection<ConfigAttribute> attributes=null;
				if(urlMap.containsKey(componentId)){
					attributes=urlMap.get(componentId);
				}
				if(attributes==null){
					attributes=configAttributes;
					urlMap.put(componentId, attributes);
				}else{
					attributes.addAll(configAttributes);						
				}
			}
		}
		applicationCache.removeCacheObjectsByKeyStartWith(COMPONENT_KEY_START);
		for(String key:cacheMap.keySet()){
			applicationCache.putCacheObject(key, cacheMap.get(key));			
		}
	}
	
	public static String getUrlId(String companyId,String targetUrl){
		String urlId=COMPONENT_KEY_START+companyId+"|"+targetUrl;
		return urlId;
	}
	
	private Collection<ConfigAttribute> buildConfigAttributes(List<RoleMember> members,UrlComponent uc,String companyId){
		Collection<ConfigAttribute> attributes=new ArrayList<ConfigAttribute>();
		for(RoleMember member:members){
			AttributeType type=null;
			Object obj=null;
			if(member.getUser()!=null){
				type=AttributeType.user;
				obj=member.getUser();
			}
			if(member.getDept()!=null){
				type=AttributeType.dept;
				obj=member.getDept();
			}
			if(member.getPosition()!=null){
				type=AttributeType.position;
				obj=member.getPosition();
			}
			if(member.getGroup()!=null){
				type=AttributeType.group;
				obj=member.getGroup();
			}
			if(type!=null){
				SecurityConfigAttribute att=new SecurityConfigAttribute(type,member.isGranted(),companyId);
				att.setMember(obj);
				attributes.add(att);				
			}
		}
		return attributes;
	}

	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}
	
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public void setUrlService(IUrlService urlService) {
		this.urlService = urlService;
	}

	public void afterPropertiesSet() throws Exception {
		this.initComponentMetadata();
	}
}
