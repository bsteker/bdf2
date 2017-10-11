package com.bstek.bdf2.core.security.component;


import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.base.tab.AbstractTabControl;
import com.bstek.dorado.view.widget.base.tab.Tab;
import com.bstek.dorado.view.widget.base.tab.TabControl;

/**
 * 实现对TabControl控件及其子控件权限过滤
 * @since 2013-1-31
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class TabControlFilter implements IComponentFilter {
	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		AbstractTabControl tabControl=(AbstractTabControl)component;
		String id=tabControl.getId();
		boolean authority=true;
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,id);
		}
		if(!authority){
			tabControl.setIgnored(true);
			return;
		}
		for(Tab tab:tabControl.getTabs()){
			String caption=tab.getCaption();
			if(StringUtils.isNotEmpty(caption)){
				authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,caption);
			}
			if(!authority){
				tab.setIgnored(true);
				continue;
			}
			if(StringUtils.isNotEmpty(caption)){
				authority=SecurityUtils.checkComponent(authentication, AuthorityType.write, url,caption);
			}
			if(!authority){
				tab.setDisabled(true);
			}
		}
	}
	public boolean support(Component component){
		return component instanceof AbstractTabControl;
	}
}
