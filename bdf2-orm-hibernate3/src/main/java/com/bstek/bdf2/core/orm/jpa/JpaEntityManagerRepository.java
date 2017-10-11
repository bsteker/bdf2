package com.bstek.bdf2.core.orm.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.context.ContextHolder;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public class JpaEntityManagerRepository implements ApplicationContextAware{
	private Map<String,EntityManager> entityManagerMap=new HashMap<String,EntityManager>();
	private EntityManager defaultEntityManager;
	private Map<String,EntityManagerFactory> entityManagerFactoryMap=new HashMap<String,EntityManagerFactory>();
	public EntityManager getEntityManager(String dataSourceName){
		if(dataSourceName==null){
			return getEntityManager();
		}else{
			if(entityManagerMap.containsKey(dataSourceName)){
				return entityManagerMap.get(dataSourceName);
			}else{
				return getEntityManager();
			}
		}
	}
	
	public EntityManager getEntityManager(){
		String dataSourceName=ContextHolder.getCurrentDataSourceName();
		if(dataSourceName==null){
			return defaultEntityManager;
		}else{
			if(entityManagerMap.containsKey(dataSourceName)){
				return entityManagerMap.get(dataSourceName);
			}else{
				return defaultEntityManager;
			}
		}
	}
	
	public Map<String, EntityManager> getEntityManagerMap() {
		return entityManagerMap;
	}

	public EntityManager getDefaultEntityManager() {
		return defaultEntityManager;
	}

	public Map<String, EntityManagerFactory> getEntityManagerFactoryMap() {
		return entityManagerFactoryMap;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		for(JpaEntityManagerFactory factory:applicationContext.getBeansOfType(JpaEntityManagerFactory.class).values()){
			entityManagerFactoryMap.put(factory.getDataSourceName(),factory.getObject());
			EntityManager entityManager=factory.getObject().createEntityManager();
			entityManagerMap.put(factory.getDataSourceName(),entityManager);
			if(factory.isAsDefault()){
				defaultEntityManager=entityManager;
			}
		}
	}
}
