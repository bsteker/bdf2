package com.bstek.bdf2.core.orm.hibernate;

import java.util.List;

import org.hibernate.Session;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public abstract class GenericHibernateDao<T> extends HibernateDao {
	protected void save(final T t) throws Exception{
		this.doInHibernateSession(new ISessionCallback<T>(){
			@SuppressWarnings("unchecked")
			public T doInSession(Session session){
				return (T)session.save(t);
			}
		});
	}
	protected void update(final T t) throws Exception{
		this.doInHibernateSession(new ISessionCallback<T>(){
			public T doInSession(Session session){
				session.update(t);
				return null;
			}
		});
	}
	protected void delete(final T t) throws Exception{
		this.doInHibernateSession(new ISessionCallback<T>(){
			public T doInSession(Session session) {
				session.delete(t);
				return null;
			}
		});
	}
	@SuppressWarnings("unchecked")
	protected List<T> loadAll(final Class<T> clazz) throws Exception{
		return (List<T>)this.doInHibernateSession(new ISessionCallback<List<T>>(){
			public List<T> doInSession(Session session){
				return (List<T>)session.createQuery("from "+clazz.getName()).list();
			}
		});
	}
	
	protected T load(final Class<T> clazz,final java.io.Serializable id) throws Exception{
		return (T)this.doInHibernateSession(new ISessionCallback<T>(){
			public T doInSession(Session session){
				session.load(clazz, id);
				return null;
			}
		});
	}
	@SuppressWarnings("unchecked")
	protected T get(final Class<T> clazz,final java.io.Serializable id) throws Exception{
		return (T)this.doInHibernateSession(new ISessionCallback<T>(){
			public T doInSession(Session session){
				return (T)session.get(clazz, id);
			}
		});
	}
}
