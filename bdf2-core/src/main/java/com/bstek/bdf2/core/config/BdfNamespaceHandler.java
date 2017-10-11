package com.bstek.bdf2.core.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.bstek.bdf2.core.config.parser.AnnotationPackagesBeanParser;
import com.bstek.bdf2.core.config.parser.AnonymousUrlBeanParser;
import com.bstek.bdf2.core.config.parser.DataSourceRegisterBeanParser;
import com.bstek.bdf2.core.config.parser.OverrideInternalBeanParser;
import com.bstek.bdf2.core.config.parser.SessionFactoryBeanParser;

/**
 * @since 2013-1-20
 * @author Jacky.gao
 */
public class BdfNamespaceHandler extends NamespaceHandlerSupport {
	public void init() {
		registerBeanDefinitionParser("datasource-register",new DataSourceRegisterBeanParser());
		registerBeanDefinitionParser("session-factory",new SessionFactoryBeanParser());
		registerBeanDefinitionParser("anonymous-url",new AnonymousUrlBeanParser());
		registerBeanDefinitionParser("annotation-scan",new AnnotationPackagesBeanParser());
		
		registerBeanDefinitionParser("user-service",new OverrideInternalBeanParser());
		registerBeanDefinitionParser("dept-service",new OverrideInternalBeanParser());
		registerBeanDefinitionParser("position-service",new OverrideInternalBeanParser());
		registerBeanDefinitionParser("framework-service",new OverrideInternalBeanParser());
	}
}
