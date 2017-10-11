package com.bstek.bdf2.profile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.profile.ProfileHibernateDao;
import com.bstek.bdf2.profile.model.ComponentEvent;
import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.model.ComponentProperty;
import com.bstek.bdf2.profile.model.ComponentSort;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public class DefaultComponentService extends ProfileHibernateDao implements IComponentService,InitializingBean{
	private ApplicationCache applicationCache;
	public ComponentInfo loadComponentFromCache(String viewId, String controlId,String assignTargetId,String type,Map<String,ComponentInfo> mapInCache) {
		String key=viewId+"|"+type+"|"+controlId+"|"+assignTargetId;
		if(mapInCache.containsKey(key)){
			return mapInCache.get(key);
		}
		return null;
	}
	
	public void initComponentToCache() {
		Map<String,ComponentInfo> map=new HashMap<String,ComponentInfo>();
		if (Configure.getBoolean(PROFILE_COMPONENTS_CACHE_PROPERTY_KEY, true)){
			String hql="from "+ComponentInfo.class.getName();
			List<ComponentInfo> components=this.query(hql);
			for(ComponentInfo component:components){
				component.setEvents(this.loadComponentEvents(component.getId()));
				component.setProperties(this.loadComponentProperties(component.getId()));
				component.setSorts(this.loadComponentSorts(component.getId()));
				String key=component.getUrl()+"|"+component.getType()+"|"+component.getControlId()+"|"+component.getAssignTargetId();
				map.put(key, component);
			}
		}
		applicationCache.putCacheObject(PROFILE_COMPONENTS_CACHE_KEY,map);
	}

	public List<ComponentProperty> loadComponentProperties(String componentId) {
		String hql="from "+ComponentProperty.class.getName()+" where componentId=:componentId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("componentId",componentId);
		return this.query(hql,map);
	}

	public List<ComponentEvent> loadComponentEvents(String componentId) {
		String hql="from "+ComponentEvent.class.getName()+" where componentId=:componentId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("componentId",componentId);
		return this.query(hql,map);
	}

	public List<ComponentSort> loadComponentSorts(String componentId) {
		String hql="from "+ComponentSort.class.getName()+" s where s.parentComponentId=:parentComponentId order by s.order asc";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("parentComponentId",componentId);
		return this.query(hql,map);
	}

	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}

	public void afterPropertiesSet() throws Exception {
		this.initComponentToCache();
	}
}
