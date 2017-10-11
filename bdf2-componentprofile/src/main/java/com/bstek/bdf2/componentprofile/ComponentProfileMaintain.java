package com.bstek.bdf2.componentprofile;

import java.util.Collection;
import java.util.Map;

import com.bstek.bdf2.componentprofile.service.ComponentProfileService;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.variant.Record;

public class ComponentProfileMaintain {
	private ComponentProfileService componentProfileService;

	@Expose
	public void doResetComponentProfile(String controlId) {
		componentProfileService.doResetComponentProfile(controlId);
	}

	@SuppressWarnings("unchecked")
	@Expose
	public void saveComponentProfile(Map<String, Object> parameter) {
		if (parameter != null) {
			if (ContextHolder.getLoginUser() != null) {
				String controlId = (String) parameter.get("controlId");
				String name = (String) parameter.get("name");
				String cols = parameter.get("cols").toString();
				Collection<Record> members = (Collection<Record>) parameter.get("members");
				componentProfileService.saveComponentProfile(controlId, name, cols, members);
			}

		}
	}

	@Expose
	public void syncSingleCache(String cacheKey) {
		if (ContextHolder.getLoginUser() == null) {
			componentProfileService.cacheComponentConfigs(cacheKey);
		}
	}

	public void refreshCache() {
		componentProfileService.cacheAllComponentConfigs();
	}

	public ComponentProfileService getComponentProfileService() {
		return componentProfileService;
	}

	public void setComponentProfileService(ComponentProfileService componentProfileService) {
		this.componentProfileService = componentProfileService;
	}
}
