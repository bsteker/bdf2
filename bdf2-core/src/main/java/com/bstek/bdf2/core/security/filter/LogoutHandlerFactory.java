package com.bstek.bdf2.core.security.filter;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class LogoutHandlerFactory implements FactoryBean<Collection<LogoutHandler>>,ApplicationContextAware {
	private Collection<LogoutHandler> logoutHandlers;
	public Collection<LogoutHandler> getObject() throws Exception {
		return logoutHandlers;
	}

	public Class<?> getObjectType() {
		return Collection.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		logoutHandlers=applicationContext.getBeansOfType(LogoutHandler.class).values();
	}
}
