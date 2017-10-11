package com.bstek.bdf2.jbpm4.pro.security.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.jbpm4.pro.security.SecurityCheck;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.base.tab.Tab;
import com.bstek.dorado.view.widget.base.tab.TabControl;

/**
 * 实现对TabControl控件及其子控件权限过滤
 * 
 */
@org.springframework.stereotype.Component
public class TabControlFilter implements IComponentFilter {
	private SecurityCheck securityCheck;

	public void filter(String processDefinitionId, String taskName, Component component) throws Exception {
		TabControl tabControl = (TabControl) component;

		Set<String> componentSignature = new HashSet<String>();
		for (Tab tab : tabControl.getTabs()) {
			String tags = tab.getTags();
			if (StringUtils.isNotEmpty(tags)) {
				componentSignature.addAll(Arrays.asList(tags.split(",")));
			}
			for (String token : new String[] { tab.getName(), tab.getName() }) {
				if (StringUtils.isNotEmpty(token)) {
					componentSignature.add(token);
				}
			}
			int authority = securityCheck.checkComponent(processDefinitionId, taskName, componentSignature);
			if (authority == ComponentFilter.INVISIBLE) {
				tab.setIgnored(true);
			} else if (authority > 0) {
				tab.setDisabled(authority == ComponentFilter.READONLY);
			}
		}
	}

	public String getSupportType() {
		return TabControl.class.getName();
	}

	public void setSecurityCheck(SecurityCheck securityCheck) {
		this.securityCheck = securityCheck;
	}
}
