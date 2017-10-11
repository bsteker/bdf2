/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.bsh;

import java.util.HashMap;
import java.util.Map;

import com.bstek.bdf2.core.orm.hibernate.HibernateDao;
import com.bstek.bdf2.rapido.RapidoAppHibernateDao;

/**
 * 提供将HibernateDaoSupportExt对象实例注入到BeanShell环境功能,<br>
 * 这样在bsh脚本中可以直接使用名为hibernateDaoSupport的变量来得到HibernateTemplate操作数据库
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class HibernateVariableRegister extends RapidoAppHibernateDao implements VariableRegister {

	public Map<String, VariableInfo> register() {
		Map<String, VariableInfo> map=new HashMap<String,VariableInfo>();
		
		VariableInfo hibernateDao=new VariableInfo();
		hibernateDao.setName("hibernateDao");
		hibernateDao.setDesc("BDF提供的HibernateDao对象实例");
		final HibernateDao dao=this;
		hibernateDao.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return dao;
			}
		});
		map.put("hibernateDao", hibernateDao);
		return map;
	}
}
