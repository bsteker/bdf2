/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.core.orm.transaction;

import java.util.Map;

import org.hibernate.Session;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.bstek.bdf2.core.context.ContextHolder;

/**
 * 扩展Spring的JDBC事务管理器，使其可以在多个数据源中动态找到合适的DataSource
 * @author jacky.gao@bstek.com
 * @since 2.0
 */
public class JdbcTransactionManager extends DataSourceTransactionManager{
	private static final long serialVersionUID = -2117944168347645059L;
	@Override
	protected void prepareForCommit(DefaultTransactionStatus status) {
		super.prepareForCommit(status);
		clean();
	}
	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		clean();
		super.doRollback(status);
	}
	private void clean(){
		Map<String,Session> sessionMap=ContextHolder.getHibernateSessionMap();
		if(sessionMap==null){
			return;
		}
		for(Session session:sessionMap.values()){
			if(session!=null && session.isOpen()){
				session.flush();
				session.close();
				session=null;
			}
		}
		sessionMap.clear();		
	}
}
