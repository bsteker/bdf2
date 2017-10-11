package com.bstek.bdf2.core.security.component;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.tree.AbstractTree;

/**
 * 用于实现对Tree以及DataTree是否显示权限进行控制
 * @since 2013-1-31
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class TreeFilter implements IComponentFilter {

	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		AbstractTree tree=(AbstractTree)component;
		boolean authority=true;
		String id=tree.getId();
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,id);
		}
		if(!authority){
			tree.setIgnored(true);
		}
	}
	public boolean support(Component component){
		return component instanceof AbstractTree;
	}
}
