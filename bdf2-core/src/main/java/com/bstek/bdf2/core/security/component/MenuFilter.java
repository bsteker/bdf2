package com.bstek.bdf2.core.security.component;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.base.menu.BaseMenuItem;
import com.bstek.dorado.view.widget.base.menu.Menu;
import com.bstek.dorado.view.widget.base.menu.MenuItem;

/**
 * 菜单权限过滤
 * @since 2013-1-31
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class MenuFilter  implements IComponentFilter{
	
	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		Menu menu = (Menu) component;
		String id=menu.getId();
		boolean authority=true;
		if(StringUtils.isNotEmpty(id)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,id);
		}
		if(!authority){
			menu.setIgnored(true);
			return;
		}
		
		for (BaseMenuItem menuItem : menu.getItems()) {
			this.filterMenuItem(url,menuItem,authentication);
		}
	}

	private void filterMenuItem(String url,BaseMenuItem menuItem,UserAuthentication authentication)
			throws Exception {
		if(!(menuItem instanceof MenuItem)){
			return;
		}
		MenuItem item = (MenuItem) menuItem;
		boolean authority=true;
		String caption=item.getCaption();
		if(StringUtils.isNotEmpty(caption)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read,url,caption);
		}
		if(!authority){
			item.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(caption)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.write,url,caption);
		}
		if(!authority){
			item.setDisabled(true);
			return;
		}
		
		String name=item.getName();
		if(StringUtils.isNotEmpty(name)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.read,url,name);
		}
		if(!authority){
			item.setIgnored(true);
			return;
		}
		if(StringUtils.isNotEmpty(name)){
			authority=SecurityUtils.checkComponent(authentication, AuthorityType.write,url,name);
		}
		if(!authority){
			item.setDisabled(true);
			return;
		}
		
		if(item.getItems() != null){
			for(BaseMenuItem mi : item.getItems()){
				this.filterMenuItem(url,mi,authentication);
			}
		}
	}
	
	public boolean support(Component component){
		return component instanceof Menu;
	}
}
