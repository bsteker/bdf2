package com.bstek.bdf2.core.orm.hibernate;

import org.hibernate.Session;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public interface ISessionCallback<T> {
	T doInSession(Session session);
}
