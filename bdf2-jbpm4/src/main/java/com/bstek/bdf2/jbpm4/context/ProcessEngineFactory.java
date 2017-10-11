package com.bstek.bdf2.jbpm4.context;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.jbpm.api.ProcessEngine;
import org.jbpm.pvm.internal.cfg.ConfigurationImpl;
import org.jbpm.pvm.internal.processengine.SpringHelper;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.core.orm.hibernate.HibernateSessionFactoryRepository;
import com.bstek.dorado.core.Configure;

/**
 * @author Jacky.gao
 * @since 2013-3-27
 */
public class ProcessEngineFactory extends SpringHelper implements InitializingBean{
	protected SessionFactory sessionFactory;
	@Override
	public ProcessEngine createProcessEngine() {
		processEngine = new ConfigurationImpl()
				.springInitiated(applicationContext).setResource(jbpmCfg)
				.setHibernateSessionFactory(sessionFactory)
				.buildProcessEngine();
		return processEngine;
	}

	public void afterPropertiesSet() throws Exception {
		Map<String,HibernateSessionFactoryRepository> respositoryMap=this.applicationContext.getBeansOfType(HibernateSessionFactoryRepository.class);
		HibernateSessionFactoryRepository respository=respositoryMap.values().iterator().next();
		String dataSourceName=Configure.getString("bdf2.jbpm4.dataSourceName");
		if(StringUtils.isNotEmpty(dataSourceName)){
			if(respository.getSessionFactoryMap().containsKey(dataSourceName)){
				sessionFactory=respository.getSessionFactoryMap().get(dataSourceName);
			}else{
				sessionFactory=respository.getDefaultSessionFactory();				
			}
		}else{
			sessionFactory=respository.getDefaultSessionFactory();
		}
		
	}
}
