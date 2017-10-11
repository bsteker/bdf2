package com.bstek.bdf2.profile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.profile.ProfileHibernateDao;
import com.bstek.bdf2.profile.model.ComponentValidator;
import com.bstek.bdf2.profile.model.ValidatorDef;
import com.bstek.bdf2.profile.model.ValidatorEvent;
import com.bstek.bdf2.profile.model.ValidatorProperty;
import com.bstek.bdf2.profile.service.IValidatorService;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public class DefaultValidatorService extends ProfileHibernateDao implements IValidatorService,InitializingBean{
	public static final String CACHE_KEY="profile_cache_key_";
	private ApplicationCache applicationCache;
	@SuppressWarnings("unchecked")
	public List<ValidatorDef> loadValidators(String componentId) {
		Map<String,List<ValidatorDef>> map=(Map<String,List<ValidatorDef>>)applicationCache.getCacheObject(CACHE_KEY);
		return map.get(componentId);
	}
	
	@SuppressWarnings("unchecked")
	public void initValidatorsToCache() {
		Map<String,Object> map=new HashMap<String,Object>();
		String hql="from "+ComponentValidator.class.getName();
		List<ComponentValidator> componentValidators=this.query(hql,map);
		for(ComponentValidator cv:componentValidators){
			ValidatorDef validator=cv.getValidator();
			validator.setProperties(this.loadValidatorProperties(validator.getId()));
			validator.setEvents(this.loadValidatorEvents(validator.getId()));
			String componentId=cv.getComponentId();
			List<ValidatorDef> validators;
			if(map.containsKey(componentId)){
				validators=(List<ValidatorDef>)map.get(componentId);
			}else{
				validators=new ArrayList<ValidatorDef>();
				map.put(componentId, validators);
			}
			validators.add(validator);
		}
		applicationCache.putCacheObject(CACHE_KEY, map);
	}
	
	private List<ValidatorEvent> loadValidatorEvents(String validatorId){
		Map<String,Object> map=new HashMap<String,Object>();
		String hql="from "+ValidatorEvent.class.getName()+" where validatorId=:validatorId";
		map.put("validatorId", validatorId);
		return this.query(hql,map);
	}
	private List<ValidatorProperty> loadValidatorProperties(String validatorId){
		Map<String,Object> map=new HashMap<String,Object>();
		String hql="from "+ValidatorProperty.class.getName()+" where validatorId=:validatorId";
		map.put("validatorId", validatorId);
		return this.query(hql,map);
	}

	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}

	public void afterPropertiesSet() throws Exception {
		this.initValidatorsToCache();
	}
}
