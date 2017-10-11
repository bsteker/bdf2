package com.bstek.bdf2.profile.listener;

import java.util.Map;

import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.profile.listener.handler.IComponentHandler;
import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;


/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public class GlobalComponentFilterListener extends AbstractFilterListener<View> {
	private ApplicationCache applicationCache;
	@SuppressWarnings("unchecked")
	@Override
	public void onInit(View view) throws Exception {
		if(getAssignTargetId()==null)return;
		Map<String,ComponentInfo> mapInCache=(Map<String,ComponentInfo>)applicationCache.getCacheObject(IComponentService.PROFILE_COMPONENTS_CACHE_KEY);
		this.handleComponents(view, mapInCache);
	}
	private void handleComponents(ViewElement viewElement,Map<String,ComponentInfo> mapInCache){
		if(viewElement.getInnerElements()==null || viewElement.getInnerElements().size()==0){
			return;
		}
		for(ViewElement component:viewElement.getInnerElements()){
			for(IComponentHandler handler:handlers){
				if(handler.support(component)){
					handler.handle(componentService, component, getAssignTargetId(),mapInCache);
				}
			}
			this.handleComponents(component, mapInCache);
		}
	}
	
	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}
}
