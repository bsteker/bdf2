package com.bstek.bdf2.core.security.component;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.bdf2.core.view.ViewManagerHelper;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.SubViewHolder;
import com.bstek.dorado.web.DoradoContext;

/**
 * 针对SubViewHolder下所有组件进行权限过滤
 * @since 2013-1-30
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class SubViewHolderFilter implements IComponentFilter,ApplicationContextAware {
	@Autowired
	@Qualifier(ViewManagerHelper.BEAN_ID)
	private ViewManagerHelper viewManagerHelper;
	private Collection<IComponentFilter> filters;
	public void filter(String url,Component component,UserAuthentication authentication) throws Exception{
		SubViewHolder viewHolder=(SubViewHolder)component;
		String viewName=viewHolder.getSubView();
		View view=viewManagerHelper.getViewConfig(DoradoContext.getCurrent(), viewName).getView();
		filterComponents(url,view.getChildren(),authentication);
	}
	
	private void filterComponents(String url,Collection<Component> components,UserAuthentication authentication) throws Exception{
		for(Component c:components){
			for(IComponentFilter filter:filters){
				if(filter.support(c)){
					filter.filter(url,c,authentication);					
				}
			}
		}
	}

	public void setViewManagerHelper(ViewManagerHelper viewManagerHelper) {
		this.viewManagerHelper = viewManagerHelper;
	}

	public boolean support(Component component){
		return component instanceof SubViewHolder;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.filters=applicationContext.getBeansOfType(IComponentFilter.class).values();
	}
}
