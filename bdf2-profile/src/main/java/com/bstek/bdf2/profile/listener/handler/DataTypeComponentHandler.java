package com.bstek.bdf2.profile.listener.handler;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.profile.model.ComponentInfo;
import com.bstek.bdf2.profile.service.IComponentService;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;

/**
 * @author Jacky.gao
 * @since 2013-2-27
 */
@Component
public class DataTypeComponentHandler extends AbstractComponentHandler implements ApplicationContextAware{
	private Collection<IComponentHandler> handlers;
	public void handle(IComponentService componentService,Object control,String assignTargetId,Map<String,ComponentInfo> mapInCache) {
		EntityDataType dataType=this.getControl(control);
		String controlId=dataType.getName();
		setControlPropertiesAndEvents(componentService, assignTargetId, dataType, controlId,"EntityDataType",mapInCache);
		for(String name:dataType.getPropertyDefs().keySet()){
			PropertyDef pd=dataType.getPropertyDef(name);
			for(IComponentHandler handler:handlers){
				if(handler.support(pd)){
					handler.handle(componentService,pd, assignTargetId,mapInCache);
				}
			}
		}
	}

	public boolean support(Object control) {
		return control instanceof EntityDataType;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		handlers=applicationContext.getBeansOfType(IComponentHandler.class).values();
	}
}
