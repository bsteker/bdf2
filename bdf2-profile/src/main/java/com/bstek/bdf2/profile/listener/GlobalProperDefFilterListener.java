package com.bstek.bdf2.profile.listener;

import java.util.Map;

import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.profile.listener.handler.IComponentHandler;
import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.data.type.property.BasePropertyDef;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public class GlobalProperDefFilterListener extends AbstractFilterListener<BasePropertyDef>{
	private ApplicationCache applicationCache;
	@Override
	@SuppressWarnings("unchecked")
	public void onInit(BasePropertyDef propertyDef) throws Exception {
		if(getAssignTargetId()==null)return;
		Map<String,ComponentInfo> mapInCache=(Map<String,ComponentInfo>)applicationCache.getCacheObject(IComponentService.PROFILE_COMPONENTS_CACHE_KEY);
		for(IComponentHandler handler:handlers){
			if(handler.support(propertyDef)){
				handler.handle(componentService, propertyDef, getAssignTargetId(),mapInCache);
			}
		}
	}
	public ApplicationCache getApplicationCache() {
		return applicationCache;
	}
	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}
}
