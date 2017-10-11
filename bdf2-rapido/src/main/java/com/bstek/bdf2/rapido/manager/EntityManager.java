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
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.Parameter;

/**
 * Entity对象维护
 * @author jacky.gao@bstek.com
 * @since 2012-6-4
 */
public class EntityManager extends RapidoJdbcDao {
	private String querySQL="select ID_,NAME_,TABLE_NAME_,DESC_,QUERY_SQL_,PAGE_SIZE_,PARENT_ID_,RECURSIVE_ from BDF_R_ENTITY";
	private ParameterManager parameterManager;
	private EntityFieldManager entityFieldManager;
	public Entity loadEntity(String id){
		List<Entity> list=this.getJdbcTemplate().query(querySQL+" where ID_=?", new Object[]{id},new EntityMapper());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public Collection<Entity> loadEntitys(Map<String,Object> map){
		List<Object> params=new ArrayList<Object>();
		String sql=" where 1=1 ";
		if(map.get("parentId")!=null){
			params.add(map.get("parentId"));
			sql+=" and PARENT_ID_=?";
		}
		if(map.get("packageId")!=null){
			params.add(map.get("packageId"));
			sql+=" and PACKAGE_ID_=?";
		}
		return this.getJdbcTemplate().query(querySQL+sql,params.toArray(),new EntityMapper());
	}
	
	public void insertEntity(Entity entity){
		String sql="insert into BDF_R_ENTITY(ID_,NAME_,TABLE_NAME_,DESC_,QUERY_SQL_,PAGE_SIZE_,PACKAGE_ID_,PARENT_ID_,RECURSIVE_) values(?,?,?,?,?,?,?,?,?)";
		entity.setId(UUID.randomUUID().toString());
		this.getJdbcTemplate().update(sql,new Object[]{entity.getId(),entity.getName(),entity.getTableName(),entity.getDesc(),entity.getQuerySql(),entity.getPageSize(),entity.getPackageId(),entity.getParentId(),entity.isRecursive()?"1":"0"});
	}
	
	public void updateEntity(Entity entity){
		String sql="update BDF_R_ENTITY set NAME_=?,TABLE_NAME_=?,DESC_=?,QUERY_SQL_=?,PAGE_SIZE_=?,RECURSIVE_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{entity.getName(),entity.getTableName(),entity.getDesc(),entity.getQuerySql(),entity.getPageSize(),entity.isRecursive()?"1":"0",entity.getId()});
	}
	
	public void deleteEntity(String id){
		Collection<Parameter> parameters=this.getEntityParameters(id);
		if(parameters!=null){
			for(Parameter p:parameters){
				this.deleteEntityParameter(id, p.getId());
				this.parameterManager.deleteParameter(p.getId());
			}			
		}
		this.entityFieldManager.deleteEntityFieldsByEntityId(id);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("parentId", id);
		for(Entity e:this.loadEntitys(map)){
			deleteEntity(e.getId());
		}
		String sql="delete from BDF_R_ENTITY where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public void deleteEntityByPackageId(String packageId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId", packageId);
		for(Entity e:this.loadEntitys(map)){
			deleteEntity(e.getId());
		}
	}
	
	public void insertEntityParameter(String entityId,String parameterId){
		String sql="insert into BDF_R_ENTITY_PARAMETER(ID_,ENTITY_ID_,PARAMETER_ID_) values(?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{UUID.randomUUID().toString(),entityId,parameterId});
	}

	public void deleteEntityParameter(String entityId,String parameterId){
		String sql="delete from BDF_R_ENTITY_PARAMETER where ENTITY_ID_=? and PARAMETER_ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{entityId,parameterId});
	}
	
	private Collection<Parameter> getEntityParameters(String entityId){
		String sql="select PARAMETER_ID_ from BDF_R_ENTITY_PARAMETER where ENTITY_ID_=?";
		List<Map<String,Object>> list=this.getJdbcTemplate().queryForList(sql,new Object[]{entityId});
		if(list.size()>0){
			Collection<Parameter> parameters=new ArrayList<Parameter>();
			for(Map<String,Object> map:list){
				String pid=(String)map.get("PARAMETER_ID_");
				parameters.add(parameterManager.loadParameter(pid));
			}
			return parameters;
		}else{
			return null;
		}
	}
	
	public ParameterManager getParameterManager() {
		return parameterManager;
	}

	public void setParameterManager(ParameterManager parameterManager) {
		this.parameterManager = parameterManager;
	}

	public EntityFieldManager getEntityFieldManager() {
		return entityFieldManager;
	}

	public void setEntityFieldManager(EntityFieldManager entityFieldManager) {
		this.entityFieldManager = entityFieldManager;
	}

	class EntityMapper implements RowMapper<Entity>{
		public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Entity e=new Entity();
			e.setId(rs.getString(1));
			e.setName(rs.getString(2));
			e.setTableName(rs.getString(3));
			e.setDesc(rs.getString(4));
			e.setQuerySql(rs.getString(5));
			e.setParameters(getEntityParameters(e.getId()));
			Collection<EntityField> fields=entityFieldManager.loadFields(e.getId());
			e.setEntityFields(fields);
			e.setPageSize(rs.getInt(6));
			e.setParentId(rs.getString(7));
			e.setRecursive(rs.getString(8)!=null && rs.getString(8).equals("1") ? true :false);
			return e;
		}
	}
}
