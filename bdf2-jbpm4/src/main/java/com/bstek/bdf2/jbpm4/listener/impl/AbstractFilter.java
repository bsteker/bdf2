package com.bstek.bdf2.jbpm4.listener.impl;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.jbpm4.listener.IFilter;
import com.bstek.bdf2.jbpm4.model.ComponentControl;

/**
 * @author Jacky.gao
 * @since 2013-6-28
 */
public abstract class AbstractFilter implements IFilter,ApplicationContextAware {
	private ApplicationContext applicationContext;
	protected ComponentControl match(Collection<ComponentControl> componentControls,Object component,String id){
		ComponentControl target=null;
		for(ComponentControl control:componentControls){
			if(control.getComponentId().equals(id)){
				target=control;
				break;
			}
		}
		return target;
	}
	
	protected void filterChildren(Collection<?> controls,Collection<ComponentControl> componentControls){
		if(controls==null || controls.size()==0)return;
		for(Object c:controls){
			for(IFilter filter:this.getAllFilters()){
				if(filter.support(c)){
					filter.filter(c, componentControls);
				}
			}
		}
	}

	protected Collection<IFilter> getAllFilters(){
		return this.applicationContext.getBeansOfType(IFilter.class).values();
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}
}
