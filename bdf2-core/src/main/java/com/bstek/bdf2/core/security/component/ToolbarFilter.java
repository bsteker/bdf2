package com.bstek.bdf2.core.security.component;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;

/**
 * @since 2013-1-31
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class ToolbarFilter implements IComponentFilter {
	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		ToolBar toolbar=(ToolBar)component;
		boolean authority=true;
		String id=toolbar.getId();
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,id);
		}
		if(!authority){
			toolbar.setIgnored(true);
			return;
		}
	}
	public boolean support(Component component){
		return component instanceof ToolBar;
	}
}
