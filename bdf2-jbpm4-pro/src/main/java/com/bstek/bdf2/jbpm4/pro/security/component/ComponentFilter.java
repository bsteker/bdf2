package com.bstek.bdf2.jbpm4.pro.security.component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.jbpm4.pro.security.SecurityCheck;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.base.AbstractButton;
import com.bstek.dorado.view.widget.base.AbstractPanel;
import com.bstek.dorado.view.widget.base.Button;
import com.bstek.dorado.view.widget.form.AbstractEditor;
import com.bstek.dorado.view.widget.form.CheckBox;
import com.bstek.dorado.view.widget.form.FormConfig;

/**
 * 用于针对dorado Button的权限过滤
 * 
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class ComponentFilter implements ApplicationContextAware {
	public static int INVISIBLE = 0;
	public static int READONLY = 1;
	public static int WRITABLE = 2;

	private SecurityCheck securityCheck;

	public void filter(String processDefinitionId, String taskName, Component component, Collection<String> additional)
			throws Exception {

		Set<String> componentSignature = new HashSet<String>();
		String token;

		token = component.getId();
		if (StringUtils.isNotEmpty(token)) {
			componentSignature.add(token);
		}

		token = component.getTags();
		if (StringUtils.isNotEmpty(token)) {
			componentSignature.addAll(Arrays.asList(token.split(",")));
		}

		token = getCaption(component);
		if (StringUtils.isNotEmpty(token)) {
			componentSignature.add(token);
		}

		if (additional != null) {
			componentSignature.addAll(additional);
		}
		if (componentSignature.size() > 0) {
			int oper = securityCheck.checkComponent(processDefinitionId, taskName, componentSignature);
			if (oper >= 0) {
				setComponent(component, oper);
			}
		}

		IComponentFilter componentFilter = componentFilterMap.get(component.getClass().getName());
		if (componentFilter != null) {
			componentFilter.filter(processDefinitionId, taskName, component);
		}

	}

	public void setComponent(Component component, int oper) {
		if (oper == INVISIBLE) {
			component.setIgnored(true);
		} else {
			boolean read = oper == READONLY;
			if (component instanceof AbstractButton) {
				((AbstractButton) component).setDisabled(read);
			} else if (component instanceof AbstractEditor) {
				((AbstractEditor) component).setReadOnly(read);
			} else if (component instanceof FormConfig) {
				((FormConfig) component).setReadOnly(read);
			}
		}
	}

	public String getCaption(Object component) {
		if (component instanceof Button) {
			return ((Button) component).getCaption();
		} else if (component instanceof AbstractPanel) {
			return ((AbstractPanel) component).getCaption();
		} else if (component instanceof CheckBox) {
			return ((CheckBox) component).getCaption();
		}
		return null;
	}

	public void setSecurityCheck(SecurityCheck securityCheck) {
		this.securityCheck = securityCheck;
	}

	private Map<String, IComponentFilter> componentFilterMap;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<IComponentFilter> componentFilterList = applicationContext.getBeansOfType(IComponentFilter.class)
				.values();
		componentFilterMap = new HashMap<String, IComponentFilter>(componentFilterList.size());
		for (IComponentFilter componentFilter : componentFilterList) {
			componentFilterMap.put(componentFilter.getSupportType(), componentFilter);
		}
	}
}
