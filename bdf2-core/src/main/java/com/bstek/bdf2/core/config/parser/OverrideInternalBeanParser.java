package com.bstek.bdf2.core.config.parser;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.bstek.bdf2.core.service.IDeptService;
import com.bstek.bdf2.core.service.IFrameworkService;
import com.bstek.bdf2.core.service.IPositionService;
import com.bstek.bdf2.core.service.IUserService;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public class OverrideInternalBeanParser implements BeanDefinitionParser {
	private Map<String,Class<?>> classMap=new HashMap<String,Class<?>>();
	private Map<String,String> beanIdMap=new HashMap<String,String>();
	public OverrideInternalBeanParser(){
		classMap.put("user-service", IUserService.class);
		classMap.put("dept-service", IDeptService.class);
		classMap.put("position-service", IPositionService.class);
		classMap.put("framework-service", IFrameworkService.class);
		
		beanIdMap.put("user-service", IUserService.BEAN_ID);
		beanIdMap.put("dept-service", IDeptService.BEAN_ID);
		beanIdMap.put("position-service", IPositionService.BEAN_ID);
		beanIdMap.put("framework-service", IFrameworkService.BEAN_ID);
	}
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String nodeName=parserContext.getDelegate().getLocalName(element);
		String refBeanId=element.getAttribute("ref");
		RuntimeBeanReference runtimeBean=new RuntimeBeanReference(refBeanId);
		RootBeanDefinition beanDefinition=new RootBeanDefinition(OverrideInternalBeanFactoryBean.class);
		beanDefinition.getPropertyValues().addPropertyValue("referenceBean", runtimeBean);
		beanDefinition.getPropertyValues().addPropertyValue("targetBeanClass", classMap.get(nodeName));
		beanDefinition.getPropertyValues().addPropertyValue("referenceBeanId", refBeanId);
		String id=beanIdMap.get(nodeName);
		parserContext.registerBeanComponent(new BeanComponentDefinition(beanDefinition,id));
		return beanDefinition;
	}
}
