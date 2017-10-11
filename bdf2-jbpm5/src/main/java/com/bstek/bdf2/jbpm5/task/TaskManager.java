package com.bstek.bdf2.jbpm5.task;

import javax.persistence.EntityManager;

import com.bstek.bdf2.jbpm5.BpmJpaDao;

/**
 * @author Jacky.gao
 * @since 2013-4-9
 */
public class TaskManager extends BpmJpaDao{
	
	public void persist(Object obj){
		EntityManager em=this.getEntityManager();
		try{
			em.persist(obj);
		}finally{
			em.flush();
			em.close();
		}
	}
}
