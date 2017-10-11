/*
 * @author Bing
 * @since 2013-03-05
 */
package com.bstek.bdf2.componentprofile.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.componentprofile.manager.IComponentConfigManager;
import com.bstek.bdf2.componentprofile.model.ComponentConfig;
import com.bstek.bdf2.componentprofile.model.EmptyConfig;
import com.bstek.bdf2.core.cache.ApplicationCache;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.data.variant.Record;

public class ComponentProfileService implements ApplicationContextAware {
	public static final String prefix = "profile:";

	private ApplicationCache applicationCache;
	private IComponentConfigManager componentConfigManager;
	private IDataService dataService;

	public String getProfileKey() {
		return dataService.getProfileKey();
	}

	private String getCacheKey(String profileKey) {
		return prefix + profileKey;
	}

	@SuppressWarnings("unchecked")
	public Map<String, ComponentConfig> loadComponentConfigsCache(String profileKey) {
		return (Map<String, ComponentConfig>) applicationCache.getCacheObject(getCacheKey(profileKey));
	}

	public ComponentConfig loadComponentConfig(String profileKey, String controlId) {
		String sessionKey = profileKey + "@" + controlId;
		HttpSession httpSession = ContextHolder.getRequest().getSession();
		ComponentConfig componentConfig = (ComponentConfig) httpSession.getAttribute(sessionKey);
		if (componentConfig == null) {
			componentConfig = componentConfigManager.loadComponentConfig(controlId, profileKey);
			if (componentConfig == null) {
				componentConfig = new EmptyConfig();
			}
			httpSession.setAttribute(sessionKey, componentConfig);
		}
		return componentConfig instanceof EmptyConfig ? null : componentConfig;
	}

	public ApplicationCache getApplicationCache() {
		return applicationCache;
	}

	public void setApplicationCache(ApplicationCache applicationCache) {
		this.applicationCache = applicationCache;
	}

	public void doResetComponentProfile(String controlId) {
		String profileKey = getProfileKey();
		componentConfigManager.deleteComponentProfileByControlId(controlId, profileKey);
		saveCache(profileKey, controlId, null);
	}

	public void saveComponentProfile(String controlId, String profileKey, String cols, Collection<Record> members) {

		if (StringUtils.isEmpty(profileKey)) {
			profileKey = getProfileKey();
		}
		componentConfigManager.deleteComponentProfileByControlId(controlId, profileKey);

		ComponentConfig componentConfig = new ComponentConfig();
		componentConfig.setId(UUID.randomUUID().toString());
		componentConfig.setName(profileKey);
		componentConfig.setCols(cols);
		componentConfig.setControlId(controlId);

		if (members != null && members.size() > 0) {
			componentConfigManager.insertComponentConfigMembers(componentConfig, members);
		}
		saveCache(profileKey, controlId, componentConfig);
	}

	@SuppressWarnings("unchecked")
	private void saveCache(String profileKey, String controlId, ComponentConfig componentConfig) {
		String viewName = controlId.substring(0, controlId.lastIndexOf("."));
		String sessionKey = prefix + profileKey + "@" + viewName;
		HttpSession httpSession = ContextHolder.getRequest().getSession();
		Map<String, ComponentConfig> cfgMap = (Map<String, ComponentConfig>) httpSession.getAttribute(sessionKey);
		if (cfgMap == null) {
			cfgMap = new HashMap<String, ComponentConfig>();
		}
		if (componentConfig != null) {
			cfgMap.put(componentConfig.getControlId(), componentConfig);
		} else {
			cfgMap.remove(controlId);
		}
		httpSession.setAttribute(sessionKey, cfgMap);
	}

	public IComponentConfigManager getComponentConfigManager() {
		return componentConfigManager;
	}

	public void setComponentConfigManager(IComponentConfigManager componentConfigManager) {
		this.componentConfigManager = componentConfigManager;
	}

	public void cacheAllComponentConfigs() {
		Collection<ComponentConfig> configs = componentConfigManager.loadComponentConfigs();
		saveComponentConfigsToCache(configs);
	}

	public void cacheComponentConfigs(String profileKey) {
		Collection<ComponentConfig> configs = componentConfigManager.loadComponentConfigsByName(profileKey);
		saveComponentConfigsToCache(configs);
	}

	private void saveComponentConfigsToCache(Collection<ComponentConfig> configs) {
		Map<String, Map<String, ComponentConfig>> componentprofileCache = new HashMap<String, Map<String, ComponentConfig>>();
		for (ComponentConfig componentConfig : configs) {
			String name = componentConfig.getName();
			Map<String, ComponentConfig> profileCache = componentprofileCache.get(name);
			if (profileCache == null) {
				profileCache = new HashMap<String, ComponentConfig>();
				componentprofileCache.put(name, profileCache);
			}
			profileCache.put(componentConfig.getControlId(), componentConfig);
		}

		for (Entry<String, Map<String, ComponentConfig>> entry : componentprofileCache.entrySet()) {
			applicationCache.putCacheObject(getCacheKey(entry.getKey()), entry.getValue());
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<IDataService> dataServices = applicationContext.getBeansOfType(IDataService.class).values();
		if (dataServices.size() == 0) {
			throw new RuntimeException(
					"IDataService interface should be implemented which used by bdf2-componentprofile");
		}

		dataService = dataServices.iterator().next();
	}

	@SuppressWarnings("unchecked")
	public Map<String, ComponentConfig> loadComponentConfigByView(String profileKey, String viewName) {
		String sessionKey = prefix + profileKey + "@" + viewName;
		HttpSession httpSession = ContextHolder.getRequest().getSession();
		Map<String, ComponentConfig> cfgMap = (Map<String, ComponentConfig>) httpSession.getAttribute(sessionKey);
		if (cfgMap == null) {
			cfgMap = new HashMap<String, ComponentConfig>();
			Collection<ComponentConfig> componentConfigs = componentConfigManager
					.loadComponentConfigsByViewName(viewName);

			for (ComponentConfig componentConfig : componentConfigs) {
				cfgMap.put(componentConfig.getControlId(), componentConfig);
			}
			httpSession.setAttribute(sessionKey, cfgMap);
		}
		return cfgMap;
	}
}
