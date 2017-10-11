package com.bstek.bdf2.core.orm.jpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.AbstractDao;
import com.bstek.dorado.data.provider.Page;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public abstract class JpaDao extends AbstractDao implements ApplicationContextAware{
	private ApplicationContext applicationContext;
	private JpaEntityManagerRepository jpaEntityManagerRepository;
	public EntityManager getEntityManager(String dataSourceName){
		return jpaEntityManagerRepository.getEntityManager(dataSourceName);
	}
	public EntityManager getEntityManager(){
		String dsName=this.getModuleFixDataSourceName();
		if(StringUtils.isEmpty(dsName)){
			dsName=ContextHolder.getCurrentDataSourceName();
		}
		return jpaEntityManagerRepository.getEntityManager(dsName);
	}
	
	public JpaEntityManagerRepository getJpaEntityManagerRepository() {
		return jpaEntityManagerRepository;
	}
	public <T> T find(String jpql){
		return this.find(jpql, null,null);
	}
	
	public <T> T find(String jpql,String dataSourceName){
		return find(jpql,null,dataSourceName);
	}
	public <T> T find(String jpql,Map<String,Object> paramsMap){
		return find(jpql,paramsMap,null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T find(String jpql,Map<String,Object> paramsMap,String dataSourceName){
		EntityManager em=this.getEntityManager(dataSourceName);
		Query query=em.createQuery(jpql);
		if(paramsMap!=null){
			this.setParameter(query, paramsMap);				
		}
		return (T)query.getResultList();
	}
	
	public <T> T findSingleObject(String jpql){
		return this.findSingleObject(jpql, null,null);
	}
	
	public <T> T findSingleObject(String jpql,String dataSourceName){
		return findSingleObject(jpql,null,dataSourceName);
	}
	public <T> T findSingleObject(String jpql,Map<String,Object> paramsMap){
		return findSingleObject(jpql,paramsMap,null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T findSingleObject(String jpql,Map<String,Object> paramsMap,String dataSourceName){
		EntityManager em=this.getEntityManager(dataSourceName);
		Query query=em.createQuery(jpql);
		if(paramsMap!=null){
			this.setParameter(query, paramsMap);				
		}
		return (T)query.getSingleResult();
	}
	
	
	public int findObjectCount(String jpql){
		return findObjectCount(jpql,null,null);
	}
	
	public int findObjectCount(String jpql,String dataSourceName){
		return findObjectCount(jpql,null,dataSourceName);
	}
	public int findObjectCount(String jpql,Map<String,Object> paramsMap){
		return findObjectCount(jpql,paramsMap,null);
	}
	public int findObjectCount(String jpql,Map<String,Object> paramsMap,String dataSourceName){
		EntityManager em=this.getEntityManager(dataSourceName);
		Query query=em.createQuery(jpql);
		if(paramsMap!=null){
			this.setParameter(query, paramsMap);				
		}
		Object obj=query.getSingleResult();
		if(obj instanceof Integer){
			return (Integer)obj;
		}else if(obj instanceof Long){
			return ((Long)obj).intValue();
		}else{
			throw new IllegalArgumentException("The ql["+jpql+"] fetch result is "+obj+",can not convert to int value");
		}
	}
	public void pagingQuery(String jpql,String countJpql,Page<?> page){
		this.pagingQuery(jpql, countJpql, page, null, null);
	}
	public void pagingQuery(String jpql,String countJpql,Page<?> page,Map<String,Object> parameter){
		this.pagingQuery(jpql, countJpql, page, parameter, null);
	}
	
	@SuppressWarnings("unchecked")
	public void pagingQuery(String jpql,String countJpql,Page<?> page,Map<String,Object> parameter,String dataSourceName){
		EntityManager em=this.getEntityManager(dataSourceName);
		Query query=em.createQuery(jpql);
		if(parameter!=null){
			this.setParameter(query, parameter);				
		}
		query.setFirstResult((page.getPageNo()-1)*page.getPageSize());
		query.setMaxResults(page.getPageSize());
		page.setEntities(query.getResultList());
		Query countQuery=em.createQuery(countJpql);
		if(parameter!=null){
			this.setParameter(countQuery, parameter);				
		}
		int count=0;
		Object obj=countQuery.getSingleResult();
		if(obj instanceof Integer){
			count=(Integer)obj;
		}else if(obj instanceof Long){
			count=((Long)obj).intValue();
		}else{
			throw new IllegalArgumentException("The ql["+countJpql+"] fetch result is "+obj+",can not convert to int value");
		}
		page.setEntityCount(count);
	}
	
	private void setParameter(Query query,Map<String,Object> map){
		for(String key:map.keySet()){
			query.setParameter(key,map.get(key));
		}
	}
	protected ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}
	
	public final void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
		jpaEntityManagerRepository=applicationContext.getBeansOfType(JpaEntityManagerRepository.class).values().iterator().next();
	}
}
