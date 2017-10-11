package com.bstek.bdf2.core.security.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Role;
import com.bstek.bdf2.core.model.RoleMember;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.orm.jdbc.JdbcDao;
import com.bstek.bdf2.core.security.attribute.AttributeType;
import com.bstek.bdf2.core.security.attribute.SecurityConfigAttribute;
import com.bstek.bdf2.core.service.IRoleService;
import com.bstek.bdf2.core.service.IUrlService;
import com.bstek.dorado.core.Configure;
/**
 * @since 2013-1-23
 * @author Jacky.gao
 */
public class UrlMetadataSource extends JdbcDao implements FilterInvocationSecurityMetadataSource,InitializingBean {
	public static final String BEAN_ID="bdf2.urlMetadataSource";
	private IRoleService roleService;
	private IUrlService urlService;
	private ApplicationCache applicationCache;
	private boolean useConservativeAuthorityStrategy;
	private Map<String,ConfigAttribute> anonymousUrlMetadata=new HashMap<String,ConfigAttribute>();
	private static final String URL_KEY_START="__urlsecure:";
	private AntPathMatcher matcher=new AntPathMatcher();
	
	@SuppressWarnings("unchecked")
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String url=null;
		if(object instanceof FilterInvocation){
			HttpServletRequest request=((FilterInvocation)object).getRequest();
			url=this.getRequestPath(request);			
		}else{
			url=(String)object;
		}
		Collection<ConfigAttribute> safeUrlAttributes=this.getAnonymousUrlAttributes(url);
		if(safeUrlAttributes!=null){
			return safeUrlAttributes;
		}
		IUser loginUser=ContextHolder.getLoginUser();
		if(loginUser==null){
			throw new NoneLoginException("Please login first");
		}
		String orgCompanyId=loginUser.getCompanyId();
		Assert.hasText(orgCompanyId, "current login user["+ContextHolder.getLoginUser().getUsername()+"] is not specified company ID");
		String[] companyIds=orgCompanyId.split(",");
		List<ConfigAttribute> attributes=null;
		for(String companyId:companyIds){
			attributes=loadUrlConfigAttributes(url,companyId);
			if(attributes!=null)break;
		}
		if(attributes==null){
			if(!useConservativeAuthorityStrategy || loginUser.isAdministrator()){
				return CollectionUtils.EMPTY_COLLECTION;				
			}else{
				throw new AccessDeniedException("Access denied");
			}
		}
		if(attributes.size()>0){
			return attributes;					
		}else{
			if(!useConservativeAuthorityStrategy || loginUser.isAdministrator()){
				return attributes;
			}else{
				throw new AccessDeniedException("Access denied");
			}
		}
		
	}
	
	private Collection<ConfigAttribute> getAnonymousUrlAttributes(String url){
		Collection<ConfigAttribute> attributes=null;
		for(String patternUrl:anonymousUrlMetadata.keySet()){
			if(matcher.match(patternUrl, url)){
				attributes=new ArrayList<ConfigAttribute>();
				attributes.add(anonymousUrlMetadata.get(patternUrl));
				break;
			}
		}
		return attributes;
	}

	@SuppressWarnings("unchecked")
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return CollectionUtils.EMPTY_COLLECTION;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
	@SuppressWarnings("unchecked")
	private List<ConfigAttribute> loadUrlConfigAttributes(String url,String companyId){
		String targetUrl=processUrl(url, companyId);
		Object attributeObj=applicationCache.getCacheObject(targetUrl);
		if(attributeObj==null){
			url=url.substring(1,url.length());
			targetUrl=processUrl(url, companyId);
			attributeObj=applicationCache.getCacheObject(targetUrl);
		}
		return (List<ConfigAttribute>)applicationCache.getCacheObject(targetUrl);
	}
	
	public void initUrlMetaData(){
		Map<String,List<ConfigAttribute>> mapAttribute=new HashMap<String,List<ConfigAttribute>>();
		for(Role role:roleService.loadAllRoles()){
			if(!role.isEnabled()){
				continue;
			}
			role.setRoleMembers(roleService.loadRoleMemberByRoleId(role.getId()));
			role.setUrls(urlService.loadUrlsByRoleId(role.getId()));
			String companyId=role.getCompanyId();
			for(Url url:role.getUrls()){
				String targetUrl=url.getUrl();
				if(StringUtils.isEmpty(targetUrl)){
					targetUrl=url.getName();
				}
				if(StringUtils.isEmpty(targetUrl)){
					continue;
				}
				targetUrl=processUrl(targetUrl,companyId);
				List<ConfigAttribute> attributes=mapAttribute.get(targetUrl);
				if(attributes==null){
					attributes=new ArrayList<ConfigAttribute>();
					mapAttribute.put(targetUrl, attributes);
				}
				this.buildConfigAttributes(role,attributes);
			}
		}
		applicationCache.removeCacheObjectsByKeyStartWith(URL_KEY_START);
		for(String key:mapAttribute.keySet()){
			applicationCache.putCacheObject(key, mapAttribute.get(key));
		}
	}
	
    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        if (request.getPathInfo() != null) {
            url += request.getPathInfo();
        }
	    boolean hasContextPath=Configure.getBoolean("bdf2.checkUrlWithContextPath");
        if(hasContextPath){
        	url = request.getContextPath()+url;
        }
        return url;
    }
	
	private void buildConfigAttributes(Role role,List<ConfigAttribute> list){
		for(RoleMember member:role.getRoleMembers()){
			SecurityConfigAttribute attribute=null;
			if(member.getUser()!=null){
				attribute=new SecurityConfigAttribute(AttributeType.user,member.isGranted(),role.getCompanyId());
				attribute.setMember(member.getUser());
			}
			if(member.getDept()!=null){
				attribute=new SecurityConfigAttribute(AttributeType.dept,member.isGranted(),role.getCompanyId());
				attribute.setMember(member.getDept());
			}
			if(member.getPosition()!=null){
				attribute=new SecurityConfigAttribute(AttributeType.position,member.isGranted(),role.getCompanyId());
				attribute.setMember(member.getPosition());
			}
			if(member.getGroup()!=null){
				attribute=new SecurityConfigAttribute(AttributeType.group,member.isGranted(),role.getCompanyId());
				attribute.setMember(member.getGroup());
			}
			list.add(attribute);
		}
	}
	
	private String processUrl(String targetUrl,String companyId){
		targetUrl=targetUrl.trim();
		return URL_KEY_START+companyId+"/"+targetUrl;
	}

	public void afterPropertiesSet() throws Exception {
		initUrlMetaData();
		Collection<AnonymousUrl> safeUrls=this.getApplicationContext().getBeansOfType(AnonymousUrl.class).values();
		buildSafeUrlConfigAttributes(safeUrls);
	}
	
	private void buildSafeUrlConfigAttributes(Collection<AnonymousUrl> safeUrls){
		for(AnonymousUrl url:safeUrls){
			String pattern=url.getUrlPattern();
			SecurityConfig attribute=new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY);
			anonymousUrlMetadata.put(pattern, attribute);
		}
	}
	
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	public void setUrlService(IUrlService urlService) {
		this.urlService = urlService;
	}
	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}

	public boolean isUseConservativeAuthorityStrategy() {
		return useConservativeAuthorityStrategy;
	}

	public void setUseConservativeAuthorityStrategy(
			boolean useConservativeAuthorityStrategy) {
		this.useConservativeAuthorityStrategy = useConservativeAuthorityStrategy;
	}
}