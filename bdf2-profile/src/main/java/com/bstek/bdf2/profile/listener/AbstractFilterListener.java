package com.bstek.bdf2.profile.listener;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.profile.listener.handler.IComponentHandler;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.bdf2.profile.service.IProfileDataService;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
public abstract class AbstractFilterListener<T> extends GenericObjectListener<T> implements ApplicationContextAware{
	protected IComponentService componentService;
	private IProfileDataService dataService;
	protected Collection<IComponentHandler> handlers;
	@Override
	public boolean beforeInit(T t) throws Exception {
		return true;
	}

	protected String getAssignTargetId(){
		return this.dataService.getAssignTargetId(DoradoContext.getCurrent().getRequest());
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		handlers=applicationContext.getBeansOfType(IComponentHandler.class).values();
		Collection<IProfileDataService> dataServices=applicationContext.getBeansOfType(IProfileDataService.class).values();
		if(dataServices.size()==0){
			throw new RuntimeException("You need implementation ["+IProfileDataService.class.getName()+"] interface when use bdf2-profile module!");
		}
		dataService=dataServices.iterator().next();
	}
	public void setComponentService(IComponentService componentService) {
		this.componentService = componentService;
	}
}
