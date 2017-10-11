package com.bstek.bdf2.core.view.url;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.CoreHibernateDao;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.model.Url;
import com.bstek.bdf2.core.view.frame.main.MainFrame;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;

/**
 * @since 2013-2-3
 * @author Jacky.gao
 */
@Component("bdf2.urlMaintain")
public class UrlMaintain extends CoreHibernateDao {
	@Autowired
	@Qualifier(MainFrame.BEAN_ID)
	private MainFrame mainFrame;
	
	@Expose
	public void refreshUrlCache(){
		mainFrame.cacheNavigatorUrls();
	}
	
	@DataProvider
	public Collection<Url> loadUrls(String parentId) throws Exception{
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new NoneLoginException("Please login first");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		String hql="from "+Url.class.getName()+" u where u.companyId=:companyId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("companyId",companyId);
		if(StringUtils.isEmpty(parentId)){
			hql+=" and u.parentId is null order by u.order asc";
			return this.query(hql,map);			
		}else{
			map.put("parentId",parentId);
			hql+=" and u.parentId=:parentId order by u.order asc";
			return this.query(hql,map);			
		}
	}
	@DataResolver
	public void saveUrls(Collection<Url> urls) throws Exception{
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
			for(Url url:urls){
				EntityState state=EntityUtils.getState(url);
				if(state.equals(EntityState.NEW)){
					url.setId(UUID.randomUUID().toString());
					url.setCompanyId(companyId);
					session.save(url);
				}
				if(state.equals(EntityState.MODIFIED) || state.equals(EntityState.MOVED)){
					session.update(url);
				}
				if(url.getChildren()!=null){
					saveUrls(url.getChildren());
				}
				if(state.equals(EntityState.DELETED)){
					session.delete(url);
				}
			}
		}finally{
			session.flush();
			session.close();
		}
	}
	
	@Expose
	public int countChildren(String parentId) {
		String hql = "select count(*) from " + Url.class.getName() + " d where d.parentId = :parentId";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("parentId", parentId);
		return this.queryForInt(hql, parameterMap);
	}
}
