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

import com.bstek.bdf2.core.orm.jdbc.JdbcDao;
import com.bstek.bdf2.rapido.RapidoAppJdbcDao;

/**
 * 提供将JdbcDaoSupportExt对象注入到BeanShell环境功能,<br>
 * 这样在bsh脚本中可以直接使用名为jdbcDaoSupportExt的变量来得到JdbcTemplate操作数据库
 * @author jacky.gao@bstek.com
 * @since 1.0
 */
public class JdbcVariableRegister extends RapidoAppJdbcDao implements VariableRegister{
	public Map<String,VariableInfo> register(){
		Map<String, VariableInfo> map=new HashMap<String,VariableInfo>();
		VariableInfo jdbcDao=new VariableInfo();
		jdbcDao.setName("jdbcDao");
		jdbcDao.setDesc("BDF提供的JdbcDao对象实例");
		final JdbcDao dao=this;
		jdbcDao.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return dao;
			}
		});
		
		VariableInfo jdbcTemplate=new VariableInfo();
		jdbcTemplate.setName("jdbcTemplate");
		jdbcTemplate.setDesc("BDF提供的Spring的JdbcTemplate对象实例");
		jdbcTemplate.setVariableExecutor(new VariableExecutor(){
			public Object execute() {
				return dao.getJdbcTemplate();
			}
		});
		
		map.put("jdbcDao",jdbcDao);
		map.put("jdbcTemplate",jdbcTemplate);
		return map;
	}
}
