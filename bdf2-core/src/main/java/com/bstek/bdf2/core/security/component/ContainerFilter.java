package com.bstek.bdf2.core.security.component;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.model.AuthorityType;
import com.bstek.bdf2.core.security.SecurityUtils;
import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.base.Button;
import com.bstek.dorado.view.widget.base.Panel;

/**
 * 容器类dorado控件权限过滤
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class ContainerFilter implements IComponentFilter,ApplicationContextAware {
	private Collection<IComponentFilter> filters;
	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		Container container=(Container)component;
		String id=container.getId();
		if(StringUtils.isNotEmpty(id)){
			boolean authority=SecurityUtils.checkComponent(authentication, AuthorityType.read, url,id);
			if(!authority){
				container.setIgnored(true);
				return;
			}
		}
		List<Component> children=container.getChildren();
		if(children!=null){
			for(Component com:children){
				for(IComponentFilter filter:filters){
					if(filter.support(com)){
						filter.filter(url, com, authentication);
						break;
					}
				}
			}
		}
		if(container instanceof Panel){
			Panel panel=(Panel)container;
			List<Button> buttons=panel.getButtons();
			if(buttons==null){
				return;
			}
			for(Button button:buttons){
				for(IComponentFilter filter:filters){
					if(filter.support(button)){
						filter.filter(url, button, authentication);
						break;
					}
				}
			}
		}
	}

	public boolean support(Component component) {
		return component instanceof Container;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		filters=applicationContext.getBeansOfType(IComponentFilter.class).values();
	}
}
