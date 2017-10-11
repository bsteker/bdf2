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
import java.util.Collection;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.LayoutProperty;

public class LayoutPropertyManager extends RapidoJdbcDao{
	public Collection<LayoutProperty> loadLayoutProperties(String componentId){
		String sql="select ID_,NAME_,VALUE_,DESC_,COMPONENT_ID_ from BDF_R_LAYOUT_PROPERTY where COMPONENT_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{componentId},new RowMapper<LayoutProperty>(){
			public LayoutProperty mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				LayoutProperty layout=new LayoutProperty();
				layout.setId(rs.getString(1));
				layout.setName(rs.getString(2));
				layout.setValue(rs.getString(3));
				layout.setDesc(rs.getString(4));
				layout.setComponentId(rs.getString(5));
				return layout;
			}
		});
	}
	
	public void insertLayoutProperty(LayoutProperty layout){
		layout.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_LAYOUT_PROPERTY(ID_,NAME_,VALUE_,DESC_,COMPONENT_ID_) values(?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{layout.getId(),layout.getName(),layout.getValue(),layout.getDesc(),layout.getComponentId()});
	}
	public void updateLayoutProperty(LayoutProperty layout){
		String sql="update BDF_R_LAYOUT_PROPERTY set NAME_=?,VALUE_=?,DESC_=?,COMPONENT_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{layout.getName(),layout.getValue(),layout.getDesc(),layout.getComponentId(),layout.getId()});
	}
	
	public void deleteLayoutProperty(String id){
		String sql="delete from BDF_R_LAYOUT_PROPERTY where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
}
