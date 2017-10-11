/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.Action;
import com.bstek.bdf2.rapido.domain.Parameter;

public class ActionManager extends RapidoJdbcDao{
	private ParameterManager parameterManager;
	public void insertAction(Action action){
		action.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_ACTION(ID_,NAME_,BEAN_ID_) values(?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{action.getId(),action.getName(),action.getBeanId()});
	}
	
	public void updateAction(Action action){
		String sql="update BDF_R_ACTION set NAME_=?,BEAN_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{action.getName(),action.getBeanId(),action.getId()});
	}
	public void deleteAction(String id){
		String sql="delete from BDF_R_ACTION where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public Collection<Parameter> loadActionParameters(String actionId){
		Collection<Parameter> list=new ArrayList<Parameter>();
		String sql="select PARAMETER_ID_ from BDF_R_ACTION_PARAMETER where ACTION_ID_=?";
		for(Map<String,Object> map:this.getJdbcTemplate().queryForList(sql,new Object[]{actionId})){
			String parameterId=(String)map.get("PARAMETER_ID_");
			list.add(parameterManager.loadParameter(parameterId));
		}
		return list;
	}
	
	public void insertActionParameter(Action action,Parameter parameter){
		this.parameterManager.insertParameter(parameter);
		String sql="insert into BDF_R_ACTION_PARAMETER(ID_,ACTION_ID_,PARAMETER_ID_) values(?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{UUID.randomUUID().toString(),action.getId(),parameter.getId()});
	}
	
	public void deleteActionParameter(String actionId,String parameterId){
		String sql="delete from BDF_R_ACTION_PARAMETER where ACTION_ID_=? and PARAMETER_ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{actionId,parameterId});
		this.parameterManager.deleteParameter(parameterId);
	}
	
	public void updateActionParameter(Parameter parameter){
		this.parameterManager.updateParameter(parameter);
	}
	
	public ParameterManager getParameterManager() {
		return parameterManager;
	}

	public void setParameterManager(ParameterManager parameterManager) {
		this.parameterManager = parameterManager;
	}

	public Action loadAction(String id){
		String sql="select ID_,NAME_,BEAN_ID_ from BDF_R_ACTION where ID_=?";
		return this.getJdbcTemplate().queryForObject(sql,new Object[]{id},new RowMapper<Action>(){
			public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
				Action a=new Action();
				a.setId(rs.getString(1));
				a.setName(rs.getString(2));
				a.setBeanId(rs.getString(3));
				return a;
			}
		});
	}
}
