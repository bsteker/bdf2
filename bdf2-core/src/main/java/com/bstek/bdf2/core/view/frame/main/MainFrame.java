package com.bstek.bdf2.core.view.frame.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.AccessDeniedException;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.annotation.DataProvider;

/**
 * @since 2013-2-1
 * @author Jacky.gao
 */
public class MainFrame extends CoreHibernateDao implements InitializingBean{
	public static final String BEAN_ID="bdf2.mainFrame";
	public static final String URL_FOR_NAVI_CACHE_KEY="url_for_navi_cache_key_";
	
	private ApplicationCache applicationCache;
	
	@DataProvider
	@SuppressWarnings("unchecked")
	public Collection<Url> loadMeunUrls(String parentId) {
		IUser user = ContextHolder.getLoginUser();
		if (user == null) {
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		List<Url> cacheUrls=(List<Url>)this.applicationCache.getCacheObject(URL_FOR_NAVI_CACHE_KEY);
		Collection<Url> urls = getCacheUrls(cacheUrls,companyId,parentId);
		UserAuthentication authentication = new UserAuthentication(user);
		Collection<Url> result = new ArrayList<Url>();
		authorityCheck(urls,authentication,result);
		return result;
	}
	
	private void authorityCheck(Collection<Url> urls,UserAuthentication authentication,Collection<Url> result){
		for (Url url : urls) {
			String targetUrl = url.getUrl();
			List<Url> children=url.getChildren();
			int childrenCount = 0;
			if(children!=null){
				childrenCount=children.size();
			}
			if (childrenCount==0 && StringUtils.isEmpty(targetUrl)) {
				continue;
			}
			if(StringUtils.isEmpty(targetUrl)){
				targetUrl = url.getName();				
			}
			try {
				SecurityUtils.checkUrl(authentication, targetUrl);
				Url newUrl=buildNewUrl(url);
				result.add(newUrl);
				if(children!=null){
					List<Url> childrenUrls=new ArrayList<Url>();
					newUrl.setChildren(childrenUrls);
					authorityCheck(children,authentication,childrenUrls);				
				}
			} catch (AccessDeniedException ex) {}
		}
	}
	
	@DataProvider
	public Collection<Url> loadContainChildMeunUrls(String parentId) {
		Collection<Url> result = this.loadMeunUrls(parentId);
		this.loadContainChildMeunUrls(result, parentId);
		return result;
	}

	private void loadContainChildMeunUrls(Collection<Url> result, String parentId) {
		for (Url url : result) {
			List<Url> childList = new ArrayList<Url>();
			childList.addAll(this.loadMeunUrls(url.getId()));
			url.setChildren(childList);
			this.loadContainChildMeunUrls(childList, url.getId());
		}
	}

	public ApplicationCache getApplicationCache() {
		return applicationCache;
	}

	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}

	private List<Url> getCacheUrls(List<Url> urls,String companyId,String parentId){
		List<Url> resultUrls=new ArrayList<Url>();
		this.buildCacheUrls(urls,resultUrls,companyId, parentId);
		return resultUrls;
	}	
	
	private void buildCacheUrls(List<Url> urls,List<Url> resultUrls,String companyId,String parentId){
		for(Url url:urls){
			if(StringUtils.isEmpty(parentId)){
				if(StringUtils.isEmpty(url.getParentId()) && url.getCompanyId()!=null && url.getCompanyId().equals(companyId)){
					resultUrls.add(url);
				}
			}else{
				if(StringUtils.isNotEmpty(url.getParentId()) && url.getParentId().equals(parentId)){
					resultUrls.add(url);
				}
			}
			if(url.getChildren()!=null){
				this.buildCacheUrls(url.getChildren(), resultUrls, companyId, parentId);
			}
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		cacheNavigatorUrls();
	}
	
	public void cacheNavigatorUrls(){
		Session session=this.getSessionFactory().openSession();
		try{
			List<Url> urls = this.loadUrls(null,session);
			this.applicationCache.putCacheObject(URL_FOR_NAVI_CACHE_KEY, urls);			
		}finally{
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Url> loadUrls(String parentId,Session session){
		String hql="from "+Url.class.getName()+" u where u.forNavigation=:forNavigation";
		List<Url> urls = null;
		if (StringUtils.isNotEmpty(parentId)) {
			hql += " and u.parentId = :parentId order by u.order asc";
			urls = session.createQuery(hql).setBoolean("forNavigation", true).setString("parentId", parentId).list();
		} else {
			hql += " and u.parentId is null order by u.order asc";
			urls = session.createQuery(hql).setBoolean("forNavigation", true).list();
		}
		for(Url url:urls){
			url.setChildren(this.loadUrls(url.getId(),session));
		}
		return urls;
	}
	
	private Url buildNewUrl(Url oldUrl){
		Url url=new Url();
		url.setId(oldUrl.getId());
		url.setName(oldUrl.getName());
		url.setDesc(oldUrl.getDesc());
		url.setUrl(oldUrl.getUrl());
		url.setIcon(oldUrl.getIcon());
		url.setParentId(oldUrl.getParentId());
		url.setCompanyId(oldUrl.getCompanyId());
		return url;
	}
}

class UrlRowMapper implements RowMapper<Url> {
	public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
		Url url = new Url();
		url.setId(rs.getString("ID_"));
		url.setName(rs.getString("NAME_"));
		url.setDesc(rs.getString("DESC_"));
		url.setUrl(rs.getString("URL_"));
		url.setIcon(rs.getString("ICON_"));
		url.setParentId(rs.getString("PARENT_ID_"));
		url.setCompanyId(rs.getString("COMPANY_ID_"));
		return url;
	}
}
