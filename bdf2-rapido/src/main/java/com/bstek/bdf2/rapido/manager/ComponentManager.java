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
import org.springframework.util.StringUtils;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.ComponentEvent;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.LayoutConstraintProperty;
import com.bstek.bdf2.rapido.domain.LayoutProperty;

public class ComponentManager extends RapidoJdbcDao{
	private String querySQL="select ID_,NAME_,DESC_,CLASS_NAME_,ENTITY_ID_,PARENT_ID_,LAYOUT_,CONTAINER_,ORDER_,ACTION_DEF_ID_ from BDF_R_COMPONENT";
	private EntityManager entityManager;
	private ActionDefManager actionDefManager;
	private LayoutPropertyManager layoutPropertyManager;
	public ComponentInfo loadComponent(String id){
		Collection<ComponentInfo> components=this.getJdbcTemplate().query(querySQL+" where ID_=?", new Object[]{id},new ComponentMapper());
		if(components.size()>0){
			return components.iterator().next();
		}else{
			return null;			
		}
	}
	
	public Collection<ComponentInfo> loadComponents(Map<String,Object> map){
		StringBuffer sb=new StringBuffer(1024);
		List<Object> params=new ArrayList<Object>();
		sb.append(querySQL);
		sb.append(" where 1=1 ");
		if(map.containsKey("name")){
			sb.append(" and NAME_ like ?");
			params.add("%"+map.get("name")+"%");
		}
		if(map.containsKey("parentId")){
			sb.append(" and PARENT_ID_ = ?");
			params.add(map.get("parentId"));
		}else{
			sb.append(" and PARENT_ID_ is null");
		}
		if(map.containsKey("className")){
			sb.append(" and CLASS_NAME_ like ?");
			params.add("%"+map.get("className")+"%");
		}
		if(map.containsKey("packageId")){
			sb.append(" and PACKAGE_ID_	= ?");
			params.add(map.get("packageId"));
		}
		if(map.containsKey("ids")){
			sb.append(" and ID_ in (?)");
			params.add(map.get("ids"));
		}
		return this.getJdbcTemplate().query(sb.toString()+" order by ORDER_ asc",params.toArray(),new ComponentMapper());
	}
	
	public Collection<ComponentProperty> loadComponentProperties(String componentId){
		String sql="select ID_,NAME_,VALUE_,COMPONENT_ID_ from BDF_R_COMPONENT_PROPERTY where COMPONENT_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{componentId},new RowMapper<ComponentProperty>(){
			public ComponentProperty mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				ComponentProperty cp=new ComponentProperty();
				cp.setId(rs.getString(1));
				cp.setName(rs.getString(2));
				cp.setValue(rs.getString(3));
				cp.setComponentId(rs.getString(4));
				return cp;
			}
		});
	}
	
	public Collection<ComponentEvent> loadComponentEvents(String componentId){
		String sql="select ID_,NAME_,DESC_,SCRIPT_,COMPONENT_ID_ from BDF_R_COMPONENT_EVENT where COMPONENT_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{componentId},new RowMapper<ComponentEvent>(){
			public ComponentEvent mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				ComponentEvent e=new ComponentEvent();
				e.setId(rs.getString(1));
				e.setName(rs.getString(2));
				e.setDesc(rs.getString(3));
				e.setScript(rs.getString(4));
				e.setComponentId(rs.getString(5));
				return e;
			}
		});
	}
	
	public void deleteComponentsByParentId(String parentId){
		for(ComponentInfo component:this.loadChildren(parentId)){
			this.deleteComponent(component.getId());
		}
	}
	public void deleteComponentsByPackageId(String packageId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId", packageId);
		for(ComponentInfo component:this.loadComponents(map)){
			this.deleteComponent(component.getId());
		}
	}
	
	public void insertComponentEvent(ComponentEvent event){
		event.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_COMPONENT_EVENT(ID_,NAME_,DESC_,COMPONENT_ID_,SCRIPT_) values(?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{event.getId(),event.getName(),event.getDesc(),event.getComponentId(),event.getScript()});
	}
	
	public void updateComponentEvent(ComponentEvent event){
		String sql="update BDF_R_COMPONENT_EVENT set NAME_=?,DESC_=?,SCRIPT_=?,COMPONENT_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{event.getName(),event.getDesc(),event.getScript(),event.getComponentId(),event.getId()});		
	}
	
	public void deleteComponentEvent(String id){
		String sql="delete from BDF_R_COMPONENT_EVENT where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public void insertComponentProperty(ComponentProperty p){
		p.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_COMPONENT_PROPERTY(ID_,NAME_,VALUE_,COMPONENT_ID_) values(?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{p.getId(),p.getName(),p.getValue(),p.getComponentId()});
	}
	
	public void updateComponentProperty(ComponentProperty p){
		String sql="update BDF_R_COMPONENT_PROPERTY set NAME_=?,VALUE_=?,COMPONENT_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{p.getName(),p.getValue(),p.getComponentId(),p.getId()});		
	}
	
	public void deleteComponentProperty(String id){
		String sql="delete from BDF_R_COMPONENT_PROPERTY where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});				
	}
	
	
	public Collection<ComponentInfo> loadChildren(String parentId){
		return this.getJdbcTemplate().query(querySQL+" where PARENT_ID_=? order by ORDER_ asc",new Object[]{parentId},new ComponentMapper());			
	}
	
	public void insertComponent(ComponentInfo component){
		String sql="insert into BDF_R_COMPONENT(ID_,NAME_,DESC_,CLASS_NAME_,ENTITY_ID_,LAYOUT_,PARENT_ID_,CONTAINER_,PACKAGE_ID_,ORDER_,ACTION_DEF_ID_) values(?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{component.getId(),component.getName(),component.getDesc(),component.getClassName(),component.getEntity()!=null?component.getEntity().getId():component.getEntityId(),component.getLayout(),component.getParentId(),component.isContainer()?"1":"0",component.getPackageId(),component.getOrder(),component.getActionDef()!=null?component.getActionDef().getId():null});
	}

	public void updateComponent(ComponentInfo component){
		String sql="update BDF_R_COMPONENT set NAME_=?,DESC_=?,CLASS_NAME_=?,ENTITY_ID_=?,LAYOUT_=?,PARENT_ID_=?,CONTAINER_=?,ORDER_=?,ACTION_DEF_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{component.getName(),component.getDesc(),component.getClassName(),component.getEntity()!=null?component.getEntity().getId():component.getEntityId(),component.getLayout(),component.getParentId(),component.isContainer()?"1":"0",component.getOrder(),component.getActionDef()!=null?component.getActionDef().getId():null,component.getId()});
	}
	
	public void deleteComponent(String id){
		for(ComponentInfo c:this.loadChildren(id)){
			deleteComponent(c.getId());
		}
		for(ComponentProperty cp:this.loadComponentProperties(id)){
			this.deleteComponentProperty(cp.getId());
		}
		for(ComponentEvent ce:this.loadComponentEvents(id)){
			this.deleteComponentEvent(ce.getId());
		}
		for(LayoutConstraintProperty lcp:this.loadComponentLayoutConstraintProperties(id)){
			this.deleteComponentLayoutConstraintProperty(lcp.getId());			
		}
		for(LayoutProperty lp:this.layoutPropertyManager.loadLayoutProperties(id)){
			this.layoutPropertyManager.deleteLayoutProperty(lp.getId());
		}
		deletePageComponents(id);
		String sql="delete from BDF_R_COMPONENT where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	private void deletePageComponents(String componentId){
		String sql="delete from BDF_R_PAGE_COMPONENT where COMPONENT_ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{componentId});
	}
	
	public Collection<LayoutConstraintProperty> loadComponentLayoutConstraintProperties(String componentId){
		String sql="select ID_,NAME_,VALUE_,DESC_,COMPONENT_ID_ from BDF_R_LAYOUT_CONSTRAINT_PROP where COMPONENT_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{componentId},new RowMapper<LayoutConstraintProperty>(){
			public LayoutConstraintProperty mapRow(ResultSet rs, int rowNum)
			throws SQLException {
				LayoutConstraintProperty layout=new LayoutConstraintProperty();
				layout.setId(rs.getString(1));
				layout.setName(rs.getString(2));
				layout.setValue(rs.getString(3));
				layout.setDesc(rs.getString(4));
				layout.setComponentId(rs.getString(5));
				return layout;
			}
		});
	}
	
	public void insertComponentLayoutConstraintProperty(LayoutConstraintProperty layout){
		layout.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_LAYOUT_CONSTRAINT_PROP(ID_,NAME_,VALUE_,DESC_,COMPONENT_ID_) values(?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{layout.getId(),layout.getName(),layout.getValue(),layout.getDesc(),layout.getComponentId()});
	}
	public void updateComponentLayoutConstraintProperty(LayoutConstraintProperty layout){
		String sql="update BDF_R_LAYOUT_CONSTRAINT_PROP set NAME_=?,VALUE_=?,DESC_=?,COMPONENT_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{layout.getName(),layout.getValue(),layout.getDesc(),layout.getComponentId(),layout.getId()});
	}
	
	public void deleteComponentLayoutConstraintProperty(String id){
		String sql="delete from BDF_R_LAYOUT_CONSTRAINT_PROP where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ActionDefManager getActionDefManager() {
		return actionDefManager;
	}

	public void setActionDefManager(ActionDefManager actionDefManager) {
		this.actionDefManager = actionDefManager;
	}

	public LayoutPropertyManager getLayoutPropertyManager() {
		return layoutPropertyManager;
	}

	public void setLayoutPropertyManager(LayoutPropertyManager layoutPropertyManager) {
		this.layoutPropertyManager = layoutPropertyManager;
	}

	class ComponentMapper implements RowMapper<ComponentInfo>{
		public ComponentInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ComponentInfo c=new ComponentInfo();
			c.setId(rs.getString(1));
			c.setName(rs.getString(2));
			c.setDesc(rs.getString(3));
			c.setClassName(rs.getString(4));
			String entityId=rs.getString(5);
			if(StringUtils.hasText(entityId)){
				c.setEntity(entityManager.loadEntity(entityId));
			}
			c.setEntityId(entityId);
			c.setParentId(rs.getString(6));
			c.setLayout(rs.getString(7));
			c.setContainer((rs.getString(8)==null || rs.getString(8).equals("0")?false:true));
			c.setOrder(rs.getInt(9));
			c.setActionDefId(rs.getString(10));
			if(c.getActionDefId()!=null){
				c.setActionDef(actionDefManager.loadActionDef(c.getActionDefId()));
			}
			return c;
		}
	}
}
