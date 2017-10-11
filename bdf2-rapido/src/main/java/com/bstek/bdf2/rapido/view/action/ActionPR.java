/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.rapido.action.IAction;
import com.bstek.bdf2.rapido.bsh.VariableInfo;
import com.bstek.bdf2.rapido.bsh.VariableRegister;
import com.bstek.bdf2.rapido.domain.Action;
import com.bstek.bdf2.rapido.domain.ActionDef;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.PackageInfo;
import com.bstek.bdf2.rapido.domain.PackageType;
import com.bstek.bdf2.rapido.domain.Parameter;
import com.bstek.bdf2.rapido.manager.ActionDefManager;
import com.bstek.bdf2.rapido.manager.EntityManager;
import com.bstek.bdf2.rapido.manager.PackageManager;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.web.DoradoContext;

public class ActionPR implements ApplicationContextAware{
	private ActionDefManager actionDefManager;
	private PackageManager packageManager;
	private EntityManager entityManager;
	private Map<String,IAction> iactionMaps;
	private Collection<VariableInfo> variables;
	@DataProvider
	public Collection<ActionDef> loadActionDefs(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId",DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"packageId"));
		return actionDefManager.loadActionDefs(map);
	}
	
	@DataProvider
	public Collection<Parameter> loadActionDefParameters(String actionDefId){
		return actionDefManager.loadActionDefParameters(actionDefId);
	}
	
	@DataProvider
	public Collection<VariableInfo> loadBeanshellVariables(){
		VariableInfo var=new VariableInfo();
		var.setName("parameter");
		var.setDesc("Action在执行时附带的参数，一个Map类型的对象实例，包含所有前台传递的参数值");
		variables.add(var);
		return variables;
	}
	
	@DataProvider
	public Collection<Parameter> loadActionParameters(String actionId){
		return actionDefManager.getActionManager().loadActionParameters(actionId);
	}
	
	@DataProvider
	public Collection<Action> loadActions(String actionDefId){
		return actionDefManager.loadActions(actionDefId);
	}

	
	@DataResolver
	public void saveActionDefs(Collection<ActionDef> actions){
		for(ActionDef actionDef:actions){
			EntityState state=EntityUtils.getState(actionDef);
			if(state.equals(EntityState.NEW)){
				actionDefManager.insertActionDef(actionDef);
			}
			if(state.equals(EntityState.MODIFIED)){
				actionDefManager.updateActionDef(actionDef);
			}
			if(state.equals(EntityState.DELETED)){
				actionDefManager.deleteActionDef(actionDef.getId());
			}
			if(actionDef.getActions()!=null){
				for(Action a:actionDef.getActions()){
					EntityState st=EntityUtils.getState(a);
					if(st.equals(EntityState.NEW)){
						actionDefManager.insertAction(a, actionDef);
					}
					if(st.equals(EntityState.DELETED)){
						actionDefManager.deleteAction(a, actionDef);
					}
					if(a.getParameters()!=null){
						for(Parameter p:a.getParameters()){
							st=EntityUtils.getState(p);
							if(st.equals(EntityState.NEW)){
								actionDefManager.getActionManager().insertActionParameter(a, p);
							}
							if(st.equals(EntityState.DELETED)){
								actionDefManager.getActionManager().deleteActionParameter(a.getId(), p.getId());
							}
							if(st.equals(EntityState.MODIFIED)){
								actionDefManager.getActionManager().updateActionParameter(p);
							}
						}
					}
				}
			}
			if(actionDef.getParameters()!=null){
				for(Parameter p:actionDef.getParameters()){
					EntityState st=EntityUtils.getState(p);
					if(st.equals(EntityState.NEW)){
						actionDefManager.insertActionDefParameter(actionDef, p);
					}
					if(st.equals(EntityState.MODIFIED)){
						actionDefManager.updateActionDefParameter(p);
					}
					if(st.equals(EntityState.DELETED)){
						actionDefManager.deleteActionDefParameter(actionDef, p);
					}
				}
			}
		}
	}

	@DataProvider
	public Collection<Entity> loadEntitys(Map<String,Object> map) throws Exception{
		if(map==null)map=new HashMap<String,Object>();
		return entityManager.loadEntitys(map);
	}
	@DataProvider
	public Collection<PackageInfo> loadEntityPackages(Map<String,Object> map){
		if(map==null)map=new HashMap<String,Object>();
		return packageManager.loadPackage(PackageType.entity, (String)map.get("parentId"));
	}
	@DataProvider
	public Collection<Action> retriveAllActions(){
		Collection<Action> actions=new ArrayList<Action>();
		for(String beanId:iactionMaps.keySet()){
			Action a=new Action();
			a.setBeanId(beanId);
			a.setName(iactionMaps.get(beanId).getName());
			actions.add(a);
		}
		return actions;
	}
	@DataProvider
	public Collection<Parameter> retriveActionParameters(String actionBeanId){
		Collection<Parameter> actions=null;
		for(String beanId:iactionMaps.keySet()){
			if(beanId.equals(actionBeanId)){
				actions=iactionMaps.get(beanId).requiredParameters();
				break;
			}
		}
		return actions;
	}
	
	public PackageManager getPackageManager() {
		return packageManager;
	}

	public void setPackageManager(PackageManager packageManager) {
		this.packageManager = packageManager;
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
	public void setApplicationContext(ApplicationContext applicationContext)
		throws BeansException {
		iactionMaps=applicationContext.getBeansOfType(IAction.class);
		variables=new ArrayList<VariableInfo>();
		for(VariableRegister reg:applicationContext.getBeansOfType(VariableRegister.class).values()){
			variables.addAll(reg.register().values());
		}
	}
}
