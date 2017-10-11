package com.bstek.bdf2.core.view.builder;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.view.ViewComponent;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
public abstract class AbstractControlBuilder implements IControlBuilder,ApplicationContextAware{
	protected Collection<IControlBuilder> builders;
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		builders=applicationContext.getBeansOfType(IControlBuilder.class).values();
	}
	protected ViewComponent generateViewComponent(String id,Class<?> clazz) {
		ViewComponent component;
		component=new ViewComponent();
		component.setId(id);			
		component.setName(clazz.getSimpleName());
		component.setIcon(">dorado/res/"+clazz.getName().replaceAll("\\.", "/")+".png");
		return component;
	}
}
