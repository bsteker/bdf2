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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.Mapping;
import com.bstek.bdf2.rapido.domain.MappingSource;

/**
 * 实现对处理映射数据的维护
 * @author jacky.gao@bstek.com
 * @since 2012-6-4
 */
public class MappingManager extends RapidoJdbcDao{
	private String querySQL="select ID_,NAME_,SOURCE_,VALUE_FIELD_,KEY_FIELD_,QUERY_SQL_,CUSTOM_KEY_VALUE_ from BDF_R_MAPPING";
	public Mapping loadMapping(String id){
		List<Mapping> list=this.getJdbcTemplate().query(querySQL+" where ID_=?", new Object[]{id},new MetadataMappingMapper());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public Collection<Mapping> loadMappings(Map<String,Object> map){
		String sql="";
		List<Object> params=new ArrayList<Object>();
		if(map.containsKey("packageId")){
			sql+=" and PACKAGE_ID_=?";
			params.add(map.get("packageId"));
		}
		return this.getJdbcTemplate().query(querySQL+" where 1=1 "+sql, params.toArray(),new MetadataMappingMapper());
	}
	public void deleteMappingByPackageId(String packageId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId", packageId);
		for(Mapping m:this.loadMappings(map)){
			this.deleteMapping(m.getId());
		}
	}
	
	public void deleteMapping(String id){
		String sql="delete from BDF_R_MAPPING where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public void insertMapping(Mapping mapping){
		mapping.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_MAPPING(ID_,NAME_,SOURCE_,VALUE_FIELD_,KEY_FIELD_,QUERY_SQL_,CUSTOM_KEY_VALUE_,PACKAGE_ID_) values(?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{mapping.getId(),mapping.getName(),mapping.getSource().toString(),mapping.getValueField(),mapping.getKeyField(),mapping.getQuerySql(),mapping.getCustomKeyValue(),mapping.getPackageId()});
	}
	public void updateMapping(Mapping mapping){
		String sql="update BDF_R_MAPPING set NAME_=?,SOURCE_=?,VALUE_FIELD_=?,KEY_FIELD_=?,QUERY_SQL_=?,CUSTOM_KEY_VALUE_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{mapping.getName(),mapping.getSource().toString(),mapping.getValueField(),mapping.getKeyField(),mapping.getQuerySql(),mapping.getCustomKeyValue(),mapping.getId()});
	}
	
	class MetadataMappingMapper implements RowMapper<Mapping>{
		public Mapping mapRow(ResultSet rs, int rowNum) throws SQLException {
			Mapping m=new Mapping();
			m.setId(rs.getString(1));
			m.setName(rs.getString(2));
			m.setSource(rs.getString(3)!=null?MappingSource.valueOf(rs.getString(3)):null);
			m.setValueField(rs.getString(4));
			m.setKeyField(rs.getString(5));
			m.setQuerySql(rs.getString(6));
			m.setCustomKeyValue(rs.getString(7));
			return m;
		}
	}
}
