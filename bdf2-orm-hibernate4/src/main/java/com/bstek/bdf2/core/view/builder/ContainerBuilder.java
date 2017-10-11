package com.bstek.bdf2.core.view.builder;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.view.ViewComponent;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.base.Panel;
/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Component
public class ContainerBuilder implements IControlBuilder,ApplicationContextAware{
	private Collection<IControlBuilder> builders;
	public void build(Object control, ViewComponent parentViewComponent) {
		Container container=(Container)control;
		String id=container.getId();
		ViewComponent component=new ViewComponent();
		component.setId(id);
		component.setIcon(">dorado/res/"+Container.class.getName().replaceAll("\\.", "/")+".png");
		component.setName(container.getClass().getSimpleName());
		if(StringUtils.isEmpty(id)){
			component.setEnabled(false);
		}
		parentViewComponent.addChildren(component);
		for(com.bstek.dorado.view.widget.Component c:container.getChildren()){
			for(IControlBuilder builder:builders){
				if(builder.support(c)){
					builder.build(c, component);
					break;
				}
			}
		}
	}
	public boolean support(Object control) {
		return (control instanceof Container) && !(control instanceof Panel);
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		builders=applicationContext.getBeansOfType(IControlBuilder.class).values();
	}
}
