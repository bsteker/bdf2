package com.bstek.bdf2.webservice.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class WSJaxb2Marshaller extends Jaxb2Marshaller implements ApplicationContextAware{

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Map<String,IWebservice> bindingsMap=applicationContext.getBeansOfType(IWebservice.class);
		if(applicationContext.getParent()!=null){
			bindingsMap.putAll(applicationContext.getParent().getBeansOfType(IWebservice.class));			
		}
		if(bindingsMap.size()>0){
			List<Class<?>> list=new ArrayList<Class<?>>();
			for(IWebservice binding:bindingsMap.values()){
				Class<?>[] c=binding.bindClasses();
				if(c!=null){
					for(Class<?> clazz:c){
						list.add(clazz);
					}
				}
			}
			if(list.size()>0){
				this.setClassesToBeBound(list.toArray(new Class<?>[list.size()]));							
			}else{
				this.setClassesToBeBound(new Class[]{String.class});
			}
		}else{
			this.setClassesToBeBound(new Class[]{String.class});			
		}
	}	
}
