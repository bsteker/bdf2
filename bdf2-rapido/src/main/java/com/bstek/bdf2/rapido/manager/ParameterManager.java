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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.Parameter;
import com.bstek.bdf2.rapido.domain.ParameterType;

/**
 * 维护Parameter数据
 * @author jacky.gao@bstek.com
 * @since 2012-6-5
 */
public class ParameterManager extends RapidoJdbcDao{
	private String querySQL="select ID_,NAME_,DESC_,VALUE_,TYPE_ from BDF_R_PARAMETER";
	public Parameter loadParameter(String id){
		return this.getJdbcTemplate().queryForObject(querySQL+" where ID_=?",new Object[]{id},new ParameterMapper());
	}

	public Collection<Parameter> loadParameters(Map<String,Object> map){
		StringBuffer sb=new StringBuffer(1024);
		List<Object> param=new ArrayList<Object>();
		sb.append(querySQL);
		sb.append(" where 1=1");
		if(map.containsKey("name")){
			sb.append(" and NAME_ like ?");
			param.add("%"+map.get("name")+"%");
		}
		if(map.containsKey("type")){
			sb.append(" and TYPE_ like ?");
			param.add("%"+map.get("type")+"%");
		}
		if(map.containsKey("packageId")){
			sb.append(" and PACKAGE_ID_=?");
			param.add(map.get("packageId"));
		}
		if(map.containsKey("ids")){
			sb.append(" and ID_ in ("+map.get("ids")+")");
		}
		return this.getJdbcTemplate().query(sb.toString(),param.toArray(),new ParameterMapper());
	}
	
	public String insertParameter(Parameter p){
		String id=UUID.randomUUID().toString();
		p.setId(id);
		String sql="insert into BDF_R_PARAMETER(ID_,NAME_,DESC_,VALUE_,TYPE_,PACKAGE_ID_) values(?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{id,p.getName(),p.getDesc(),p.getValue(),p.getType(),p.getPackageId()});
		return id;
	}
	
	public void updateParameter(Parameter p){
		String sql="update BDF_R_PARAMETER set NAME_=?,VALUE_=?,DESC_=?,TYPE_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{p.getName(),p.getValue(),p.getDesc(),p.getType(),p.getId()});
	}
	
	public void deleteParameter(String id){
		String sql="delete from BDF_R_PARAMETER where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	class ParameterMapper implements RowMapper<Parameter> {
		public Parameter mapRow(ResultSet rs, int rowNum) throws SQLException {
			Parameter p=new Parameter();
			p.setId(rs.getString(1));
			p.setName(rs.getString(2));
			p.setDesc(rs.getString(3));
			p.setValue(rs.getString(4));
			if(rs.getString(5)!=null){
				p.setType(ParameterType.valueOf(rs.getString(5)));				
			}
			return p;
		}
	}
}
