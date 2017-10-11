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

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.Action;
import com.bstek.bdf2.rapido.domain.ActionDef;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.MetaData;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.bdf2.rapido.domain.Validator;

public class PageManager extends RapidoJdbcDao{
	private ComponentManager componentManager;
	private LayoutPropertyManager layoutPropertyManager;
	private ActionDefManager actionDefManager;
	private ValidatorManager validatorManager;
	private String querySQL="select ID_,NAME_,LAYOUT_,DESC_,PACKAGE_ID_ from BDF_R_PAGE";
	public PageInfo loadPage(String id){
		return this.getJdbcTemplate().queryForObject(querySQL+" where ID_=?",new Object[]{id},new PageMapper());
	}
	
	public PageInfo loadPageForCreateXml(String pageId){
		PageInfo p=this.loadPage(pageId);
		p.setProperties(this.loadPageProperties(p.getId()));
		p.setLayoutProperties(layoutPropertyManager.loadLayoutProperties(p.getId()));
		if(p.getComponents()!=null){
			for(ComponentInfo c:p.getComponents()){
				if(c==null)continue;
				this.fillComponentProperties(c);
			}				
		}
		return p;
	}
	
	private void fillComponentProperties(ComponentInfo component){
		fillEntityFieldValidator(component);
		component.setChildren(componentManager.loadChildren(component.getId()));
		component.setComponentEvents(componentManager.loadComponentEvents(component.getId()));
		component.setComponentProperties(componentManager.loadComponentProperties(component.getId()));
		component.setLayoutConstraintProperties(componentManager.loadComponentLayoutConstraintProperties(component.getId()));
		component.setLayoutProperties(layoutPropertyManager.loadLayoutProperties(component.getId()));
		if(component.getChildren()!=null){
			for(ComponentInfo c:component.getChildren()){
				c.setReadOnly(component.isReadOnly());
				this.fillComponentProperties(c);
				fillEntityFieldValidator(c);
			}
		}
		if(component.getActionDef()!=null){
			ActionDef actionDef=component.getActionDef();
			actionDef.setActions(actionDefManager.loadActions(actionDef.getId()));
			actionDef.setParameters(actionDefManager.loadActionDefParameters(actionDef.getId()));
			if(actionDef.getActions()!=null){
				for(Action action:actionDef.getActions()){
					action.setParameters(actionDefManager.getActionManager().loadActionParameters(action.getId()));
				}
			}
		}
		if(component.getEntity()!=null && component.getEntity().getEntityFields()!=null){
			for(EntityField ef:component.getEntity().getEntityFields()){
				MetaData md=ef.getMetaData();
				if(md!=null){
					Collection<Validator> validators=validatorManager.loadValidators(md.getId());
					for(Validator v:validators){
						v.setProperties(validatorManager.loadValidatorProperties(v.getId()));
					}
					md.setValidators(validators);
				}
			}
		}
	}

	private void fillEntityFieldValidator(ComponentInfo c) {
		if(c.getEntity()!=null && c.getEntity().getEntityFields()!=null){
			for(EntityField ef:c.getEntity().getEntityFields()){
				ef.setValidators(validatorManager.loadValidators(ef.getId()));
				if(ef.getValidators()!=null){
					for(Validator validator:ef.getValidators()){
						validator.setProperties(validatorManager.loadValidatorProperties(validator.getId()));
					}
				}
			}
		}
	}
	
	public Collection<PageInfo> loadPageByPackageId(String packageId){
		return this.getJdbcTemplate().query(querySQL+" where PACKAGE_ID_=?",new Object[]{packageId},new PageMapper());
	}
	
	public Collection<ComponentProperty> loadPageProperties(String pageId){
		String sql="select ID_,NAME_,VALUE_,COMPONENT_ID_ from BDF_R_COMPONENT_PROPERTY where COMPONENT_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{pageId},new RowMapper<ComponentProperty>(){
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
	
	public void insertPageProperty(ComponentProperty p){
		p.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_COMPONENT_PROPERTY(ID_,NAME_,VALUE_,COMPONENT_ID_) values(?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{p.getId(),p.getName(),p.getValue(),p.getComponentId()});
	}
	
	public void updatePageProperty(ComponentProperty p){
		String sql="update BDF_R_COMPONENT_PROPERTY set NAME_=?,VALUE_=?,COMPONENT_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{p.getName(),p.getValue(),p.getComponentId(),p.getId()});		
	}
	
	public void deletePageProperty(String id){
		String sql="delete from BDF_R_COMPONENT_PROPERTY where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});				
	}	
	
	
	public void insertPage(PageInfo page){
		String sql="insert into BDF_R_PAGE(ID_,NAME_,LAYOUT_,DESC_,PACKAGE_ID_) values(?,?,?,?,?)";
		page.setId(UUID.randomUUID().toString());
		this.getJdbcTemplate().update(sql,new Object[]{page.getId(),page.getName(),page.getLayout(),page.getDesc(),page.getPackageId()});
	}
	
	public void insertPageComponent(String pageId,ComponentInfo component){
		String sql="insert into BDF_R_PAGE_COMPONENT(ID_,PAGE_ID_,COMPONENT_ID_,ORDER_) values(?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{UUID.randomUUID().toString(),pageId,component.getId(),component.getOrder()});
	}
	public void updatePageComponent(String pageId,ComponentInfo component){
		String sql="update BDF_R_PAGE_COMPONENT set ORDER_=?,READ_ONLY_=? where PAGE_ID_=? and COMPONENT_ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{component.getOrder(),component.isReadOnly()?"Y":"N",pageId,component.getId()});
	}
	
	public void deletePageComponents(String pageId,String componentId){
		if(pageId!=null && componentId!=null){
			String sql="delete from BDF_R_PAGE_COMPONENT where PAGE_ID_=? and COMPONENT_ID_=?";
			this.getJdbcTemplate().update(sql,new Object[]{pageId,componentId});			
		}
		if(pageId!=null && componentId==null){
			String sql="delete from BDF_R_PAGE_COMPONENT where PAGE_ID_=?";
			this.getJdbcTemplate().update(sql,new Object[]{pageId});			
		}
		if(pageId==null && componentId!=null){
			String sql="delete from BDF_R_PAGE_COMPONENT where COMPONENT_ID_=?";
			this.getJdbcTemplate().update(sql,new Object[]{componentId});			
		}
	}
	
	public void updatePage(PageInfo page){
		String sql="update BDF_R_PAGE set NAME_=?,LAYOUT_=?,DESC_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{page.getName(),page.getLayout(),page.getDesc(),page.getId()});
	}
	
	public void deletePageByPackageId(String packageId){
		for(PageInfo page:this.loadPageByPackageId(packageId)){
			this.deletePage(page.getId());
		}
		String sql="delete from BDF_R_PAGE where PACKAGE_ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{packageId});
	}
	public void deletePage(String id){
		this.deletePageComponents(id, null);
		String sql="delete from BDF_R_PAGE where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	private Collection<ComponentInfo> loadComponents(String pageId){
		String sql="select COMPONENT_ID_,READ_ONLY_ from BDF_R_PAGE_COMPONENT where PAGE_ID_ = ? order by ORDER_ asc";
		List<Map<String,Object>> list=this.getJdbcTemplate().queryForList(sql,new Object[]{pageId});
		if(list.size()>0){
			Collection<ComponentInfo> components=new ArrayList<ComponentInfo>();
			for(Map<String,Object> map:list){
				String componentId=(String)map.get("COMPONENT_ID_");
				String readOnlyStr=(String)map.get("READ_ONLY_");
				boolean readOnly=(StringUtils.isEmpty(readOnlyStr) || readOnlyStr.equals("N"))?false:true;
				ComponentInfo component=componentManager.loadComponent(componentId);
				component.setReadOnly(readOnly);
				if(component!=null){
					components.add(component);					
				}
			}
			return components;
		}else{
			return null;			
		}
	}

	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	public LayoutPropertyManager getLayoutPropertyManager() {
		return layoutPropertyManager;
	}

	public void setLayoutPropertyManager(LayoutPropertyManager layoutPropertyManager) {
		this.layoutPropertyManager = layoutPropertyManager;
	}
	
	public ValidatorManager getValidatorManager() {
		return validatorManager;
	}

	public void setValidatorManager(ValidatorManager validatorManager) {
		this.validatorManager = validatorManager;
	}

	public ActionDefManager getActionDefManager() {
		return actionDefManager;
	}

	public void setActionDefManager(ActionDefManager actionDefManager) {
		this.actionDefManager = actionDefManager;
	}

	public String getQuerySQL() {
		return querySQL;
	}

	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}

	class PageMapper implements RowMapper<PageInfo>{
		public PageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			PageInfo p=new PageInfo();
			p.setId(rs.getString(1));
			p.setName(rs.getString(2));
			p.setLayout(rs.getString(3));
			p.setDesc(rs.getString(4));
			p.setPackageId(rs.getString(5));
			p.setComponents(loadComponents(p.getId()));
			return p;
		}
	}
}
