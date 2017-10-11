package com.bstek.bdf2.core.orm.hibernate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public class HibernateSessionFactoryRepository implements ApplicationContextAware{
	private Map<String,SessionFactory> sessionFactoryMap=new HashMap<String,SessionFactory>();
	private SessionFactory defaultSessionFactory;
	private String defaultSessionFactoryName;
	private Collection<HibernateSessionFactory> sessionFactoryBeanList;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		sessionFactoryBeanList=applicationContext.getBeansOfType(HibernateSessionFactory.class).values();
		for(HibernateSessionFactory bean:sessionFactoryBeanList){
			SessionFactory sessionFactory=bean.getObject();
			sessionFactoryMap.put(bean.getDataSourceName(),sessionFactory);
			if(bean.isAsDefault()){
				defaultSessionFactory=sessionFactory;
				defaultSessionFactoryName=bean.getDataSourceName();
			}
		}
	}

	public String getDefaultSessionFactoryName() {
		return defaultSessionFactoryName;
	}

	public Map<String, SessionFactory> getSessionFactoryMap() {
		return sessionFactoryMap;
	}

	public SessionFactory getDefaultSessionFactory() {
		return defaultSessionFactory;
	}
}
