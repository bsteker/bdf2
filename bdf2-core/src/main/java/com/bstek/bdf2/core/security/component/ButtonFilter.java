package com.bstek.bdf2.core.security.component;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.HideMode;
import com.bstek.dorado.view.widget.base.AbstractButton;
import com.bstek.dorado.view.widget.base.Button;

/**
 * 用于针对dorado Button的权限过滤
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class ButtonFilter implements IComponentFilter{
	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		AbstractButton button=(AbstractButton)component;
		button.setHideMode(HideMode.display);
		boolean authority=true;
		String id=button.getId();
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication,AuthorityType.read,url,id);			
		}
		if(!authority){
			button.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication,AuthorityType.write,url,id);
		}
		if(!authority){
			button.setDisabled(true);
			return;
		}
		if(!(button instanceof Button)){
			return;
		}
		Button b=(Button)button;
		String caption=b.getCaption();
		if(StringUtils.isNotEmpty(caption)){
			authority=SecurityUtils.checkComponent(authentication,AuthorityType.read,url,caption);
		}
		if(!authority){
			button.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(caption)){
			authority=SecurityUtils.checkComponent(authentication,AuthorityType.write,url,caption);
		}
		if(!authority){
			button.setDisabled(true);
			return;
		}
	}
	public boolean support(Component component){
		return component instanceof AbstractButton;
	}
}
