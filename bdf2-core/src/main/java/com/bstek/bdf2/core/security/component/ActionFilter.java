package com.bstek.bdf2.core.security.component;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.bdf2.core.security.component.IComponentFilter;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.action.Action;

/**
 * Action类型控件权限过滤
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@org.springframework.stereotype.Component
public class ActionFilter implements IComponentFilter {
	public void filter(String url, Component component, UserAuthentication authentication) throws Exception {
		Action action = (Action) component;
		boolean authority = true;
		String id = action.getId();
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(authentication, AuthorityType.read, url, id);
		}
		if (!authority) {
			action.setIgnored(true);
			return;
		}
		if (StringUtils.isNotEmpty(id)) {
			authority = SecurityUtils.checkComponent(authentication, AuthorityType.write, url, id);
		}
		if (!authority) {
			action.setDisabled(true);
			return;
		}
	}

	public boolean support(Component component) {
		return component instanceof Action;
	}
}
