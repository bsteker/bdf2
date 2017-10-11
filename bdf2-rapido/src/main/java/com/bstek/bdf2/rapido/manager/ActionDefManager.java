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
import com.bstek.bdf2.rapido.domain.Action;
import com.bstek.bdf2.rapido.domain.ActionDef;
import com.bstek.bdf2.rapido.domain.Parameter;

public class ActionDefManager extends RapidoJdbcDao{
	private ParameterManager parameterManager;
	private EntityManager entityManager;
	private ActionManager actionManager;
	private String querySql="select ID_,NAME_,DESC_,TYPE_,SCRIPT_,ENTITY_ID_,ASYNC_,CONFIRM_MESSAGE_,SUCCESS_MESSAGE_,BEFORE_EXECUTE_SCRIPT_,ON_SUCCESS_SCRIPT_ from BDF_R_ACTION_DEF";
	public Collection<ActionDef> loadActionDefs(Map<String,Object> map){
		String sql="";
		List<Object> params=new ArrayList<Object>();
		if(map.containsKey("packageId")){
			params.add(map.get("packageId"));
			sql+=" and PACKAGE_ID_=?";
		}
		return this.getJdbcTemplate().query(querySql+" where 1=1 "+sql,params.toArray(),new ActionDefMapper());
	}
	
	public ActionDef loadActionDef(String id){
		List<ActionDef> actions=this.getJdbcTemplate().query(querySql+" where ID_=? ",new Object[]{id},new ActionDefMapper());
		if(actions.size()>0){
			return actions.get(0);
		}else{
			return null;
		}
	}
	
	public void insertActionDef(ActionDef action){
		String sql="insert into BDF_R_ACTION_DEF(ID_,NAME_,DESC_,SCRIPT_,ENTITY_ID_,ASYNC_,CONFIRM_MESSAGE_,SUCCESS_MESSAGE_,BEFORE_EXECUTE_SCRIPT_,ON_SUCCESS_SCRIPT_,PACKAGE_ID_) values(?,?,?,?,?,?,?,?,?,?,?)";
		action.setId(UUID.randomUUID().toString());
		this.getJdbcTemplate().update(sql,new Object[]{action.getId(),action.getName(),action.getDesc(),action.getScript(),action.getEntity()!=null?action.getEntity().getId():action.getEntityId(),action.isAsync()?"1":0,action.getConfirmMessage(),action.getSuccessMessage(),action.getBeforeExecuteScript(),action.getOnExecuteScript(),action.getPackageId()});
	}
	
	public void updateActionDef(ActionDef action){
		String sql="update BDF_R_ACTION_DEF set NAME_=?,DESC_=?,SCRIPT_=?,ENTITY_ID_=?,ASYNC_=?,CONFIRM_MESSAGE_=?,SUCCESS_MESSAGE_=?,BEFORE_EXECUTE_SCRIPT_=?,ON_SUCCESS_SCRIPT_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{action.getName(),action.getDesc(),action.getScript(),action.getEntity()!=null?action.getEntity().getId():action.getEntityId(),action.isAsync()?"1":0,action.getConfirmMessage(),action.getSuccessMessage(),action.getBeforeExecuteScript(),action.getOnExecuteScript(),action.getId()});
	}
	public void deleteActionDefByPackageId(String packageId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId", packageId);
		for(ActionDef def:this.loadActionDefs(map)){
			this.deleteActionDef(def.getId());
		}
	}
	
	public void deleteActionDef(String id){
		ActionDef def=new ActionDef();
		def.setId(id);
		deleteActionDefParameter(def,null);
		deleteAction(null,def);
		String sql="delete from BDF_R_ACTION_DEF where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public Collection<Parameter> loadActionDefParameters(String actionId){
		Collection<Parameter> params=new ArrayList<Parameter>();
		String sql="select PARAMETER_ID_ from BDF_R_ACTION_DEF_PARAMETER where ACTION_DEF_ID_=?";
		for(Map<String,Object> m:this.getJdbcTemplate().queryForList(sql,new Object[]{actionId})){
			params.add(this.parameterManager.loadParameter((String)m.get("PARAMETER_ID_")));
		}
		return params;
	}
	
	public void updateActionDefParameter(Parameter p){
		parameterManager.updateParameter(p);
	}
	
	public void insertActionDefParameter(ActionDef a,Parameter p){
		parameterManager.insertParameter(p);
		String sql="insert into BDF_R_ACTION_DEF_PARAMETER(ID_,ACTION_DEF_ID_,PARAMETER_ID_) values(?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{UUID.randomUUID().toString(),a.getId(),p.getId()});
	}
	
	public void deleteActionDefParameter(ActionDef a,Parameter p){
		if(p!=null){
			String sql="delete from BDF_R_ACTION_DEF_PARAMETER where ACTION_DEF_ID_=? and PARAMETER_ID_=?";
			this.getJdbcTemplate().update(sql,new Object[]{a.getId(),p.getId()});
			parameterManager.deleteParameter(p.getId());			
		}else{
			for(Parameter param:this.loadActionDefParameters(a.getId())){
				parameterManager.deleteParameter(param.getId());
			}
			String sql="delete from BDF_R_ACTION_DEF_PARAMETER where ACTION_DEF_ID_=?";
			this.getJdbcTemplate().update(sql,new Object[]{a.getId()});
		}
	}
	
	public Collection<Action> loadActions(String actionDefId){
		String sql="select ACTION_ID_ from BDF_R_ACTION_DEF_RELATION where ACTION_DEF_ID_=?";
		Collection<Action> result=new ArrayList<Action>();
		for(Map<String,Object> map:this.getJdbcTemplate().queryForList(sql,new Object[]{actionDefId})){
			String actionId=(String)map.get("ACTION_ID_");
			result.add(actionManager.loadAction(actionId));
		}
		return result;
	}
	
	public void insertAction(Action action,ActionDef def){
		actionManager.insertAction(action);
		String sql="insert into BDF_R_ACTION_DEF_RELATION(ID_,ACTION_ID_,ACTION_DEF_ID_) values(?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{UUID.randomUUID().toString(),action.getId(),def.getId()});
	}
	
	public void deleteAction(Action action,ActionDef def){
		if(action!=null){
			String sql="delete from BDF_R_ACTION_DEF_RELATION where ACTION_ID_=? and ACTION_DEF_ID_=?";
			this.getJdbcTemplate().update(sql,new Object[]{action.getId(),def.getId()});
			actionManager.deleteAction(action.getId());			
		}else{
			for(Action a:this.loadActions(def.getId())){
				actionManager.deleteAction(a.getId());
			}
			String sql="delete from BDF_R_ACTION_DEF_RELATION where ACTION_DEF_ID_=?";
			this.getJdbcTemplate().update(sql,new Object[]{def.getId()});
		}
	}
	
	public ActionManager getActionManager() {
		return actionManager;
	}

	public void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}

	public ParameterManager getParameterManager() {
		return parameterManager;
	}

	public void setParameterManager(ParameterManager parameterManager) {
		this.parameterManager = parameterManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	class ActionDefMapper implements RowMapper<ActionDef>{
		public ActionDef mapRow(ResultSet rs, int rowNum) throws SQLException {
			ActionDef action=new ActionDef();
			action.setId(rs.getString(1));
			action.setName(rs.getString(2));
			action.setDesc(rs.getString(3));
			//action.setType(ActionType.valueOf(rs.getString(4)));
			action.setScript(rs.getString(5));
			if(rs.getString(6)!=null){
				action.setEntityId(rs.getString(6));
				action.setEntity(entityManager.loadEntity(rs.getString(6)));				
			}
			action.setAsync((rs.getString(7)==null || rs.getString(7).equals("1"))?true:false);
			action.setConfirmMessage(rs.getString(8));
			action.setSuccessMessage(rs.getString(9));
			action.setBeforeExecuteScript(rs.getString(10));
			action.setOnExecuteScript(rs.getString(11));
			return action;
		}
	}
}
