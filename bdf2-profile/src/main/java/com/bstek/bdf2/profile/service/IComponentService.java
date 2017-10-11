package com.bstek.bdf2.profile.service;

import java.util.List;
import java.util.Map;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.model.ComponentEvent;
import com.bstek.bdf2.profile.model.ComponentProperty;
import com.bstek.bdf2.profile.model.ComponentSort;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public interface IComponentService {
	public static final String BEAN_ID="bdf2.profile.componentService";
	public final static String PROFILE_COMPONENTS_CACHE_PROPERTY_KEY="bdf2.initProfileCache";
	public final static String PROFILE_COMPONENTS_CACHE_KEY="profile_components_cache_key_";
	ComponentInfo loadComponentFromCache(String viewId,String controlId,String assignTargetId,String type,Map<String,ComponentInfo> mapInCache);
	List<ComponentProperty> loadComponentProperties(String componentId);
	List<ComponentEvent> loadComponentEvents(String componentId);
	List<ComponentSort> loadComponentSorts(String componentId);
	public void initComponentToCache();
}
