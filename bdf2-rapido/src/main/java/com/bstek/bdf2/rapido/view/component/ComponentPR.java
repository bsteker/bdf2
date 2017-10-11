/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.rapido.common.RuleSetHelper;
import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.bdf2.rapido.component.property.PropertySupport;
import com.bstek.bdf2.rapido.domain.ActionDef;
import com.bstek.bdf2.rapido.domain.ComponentEvent;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.LayoutConstraintProperty;
import com.bstek.bdf2.rapido.domain.LayoutProperty;
import com.bstek.bdf2.rapido.domain.PackageInfo;
import com.bstek.bdf2.rapido.domain.PackageType;
import com.bstek.bdf2.rapido.manager.ActionDefManager;
import com.bstek.bdf2.rapido.manager.ComponentManager;
import com.bstek.bdf2.rapido.manager.EntityManager;
import com.bstek.bdf2.rapido.manager.LayoutPropertyManager;
import com.bstek.bdf2.rapido.manager.PackageManager;
import com.bstek.bdf2.rapido.manager.PageManager;
import com.bstek.bdf2.rapido.view.component.def.PropertyDef;
import com.bstek.bdf2.rapido.view.component.def.SupportDef;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.web.DoradoContext;

public class ComponentPR implements ApplicationContextAware{
	private ComponentManager componentManager;
	private EntityManager entityManager;
	private ActionDefManager actionDefManager;
	private PackageManager packageManager;
	private Collection<ISupport> supports;
	private RuleSetHelper ruleSetHelper;
	private PageManager pageManager;
	private LayoutPropertyManager layoutPropertyManager;
	@DataProvider
	public Collection<ComponentInfo> loadComponents(String parentId) throws Exception{
		String packageId=(String)DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"packageId");
		Map<String,Object> map=new HashMap<String,Object>();
		if(parentId==null){
			map.put("packageId", packageId);			
		}else{
			map.put("parentId", parentId);						
		}
		Collection<ComponentInfo> components=componentManager.loadComponents(map);
		buildComponentSupport(components);
		return components;
	}
	
	@Expose
	public void saveComponentOperate(Map<String,Object> map){
		String componentId=(String)map.get("componentId");
		String packageId=(String)map.get("packageId");
		ComponentInfo component=componentManager.loadComponent(componentId);
		component.setPackageId(packageId);
		this.retriveChildComponent(component);
		component.setParentId(null);
		this.fillComponents(component, packageId);
		this.insertComponents(component);
	}
	
	public void insertComponents(ComponentInfo component){
		componentManager.insertComponent(component);
		if(component.getComponentEvents()!=null){
			for(ComponentEvent ce:component.getComponentEvents()){
				componentManager.insertComponentEvent(ce);
			}
		}
		if(component.getComponentProperties()!=null){
			for(ComponentProperty cp:component.getComponentProperties()){
				componentManager.insertComponentProperty(cp);
			}
		}
		if(component.getLayoutConstraintProperties()!=null){
			for(LayoutConstraintProperty cp:component.getLayoutConstraintProperties()){
				componentManager.insertComponentLayoutConstraintProperty(cp);
			}
		}
		if(component.getLayoutProperties()!=null){
			for(LayoutProperty lp:component.getLayoutProperties()){
				layoutPropertyManager.insertLayoutProperty(lp);
			}
		}
		if(component.getChildren()!=null){
			for(ComponentInfo com:component.getChildren()){
				this.insertComponents(com);
			}
		}
	}
	
	
	private void fillComponents(ComponentInfo component,String packageId){
		component.setId(UUID.randomUUID().toString());
		component.setPackageId(packageId);
		for(ComponentProperty cp:component.getComponentProperties()){
			cp.setId(UUID.randomUUID().toString());
			cp.setComponentId(component.getId());
		}			
		for(ComponentEvent ce:component.getComponentEvents()){
			ce.setId(UUID.randomUUID().toString());
			ce.setComponentId(component.getId());
		}
		for(LayoutConstraintProperty cp:component.getLayoutConstraintProperties()){
			cp.setId(UUID.randomUUID().toString());
			cp.setComponentId(component.getId());
		}
		for(LayoutProperty lp:component.getLayoutProperties()){
			lp.setId(UUID.randomUUID().toString());
			lp.setComponentId(component.getId());
		}
		if(component.getChildren()!=null){
			for(ComponentInfo com:component.getChildren()){
				com.setParentId(component.getId());
				this.fillComponents(com, packageId);
			}
		}
	}
	
	
	private void retriveChildComponent(ComponentInfo component){
		component.setComponentEvents(componentManager.loadComponentEvents(component.getId()));
		component.setComponentProperties(componentManager.loadComponentProperties(component.getId()));
		component.setLayoutConstraintProperties(componentManager.loadComponentLayoutConstraintProperties(component.getId()));
		component.setLayoutProperties(layoutPropertyManager.loadLayoutProperties(component.getId()));
		Collection<ComponentInfo> children=componentManager.loadChildren(component.getId());
		if(children!=null){
			for(ComponentInfo com:children){
				this.retriveChildComponent(com);
			}
			component.setChildren(children);
		}
	}
	
	@Expose
	public Collection<ComponentInfo> autoCreateChildren(Map<String,Object> map){
		String className=(String)map.get("className");
		String entityId=(String)map.get("entityId");
		String parentId=(String)map.get("parentId");
		componentManager.deleteComponentsByParentId(parentId);
		Entity entity=entityManager.loadEntity(entityId);
		ISupport support=this.retriveComponentSupport(className);
		Collection<ComponentInfo> components=support.createChildrenByEntity(entity);
		if(components==null)return null;
		for(ComponentInfo component:components){
			component.setParentId(parentId);
			component.setId(UUID.randomUUID().toString());
			this.fillComponentSupport(component);
		}
		return components;
	}
	
	@Expose
	public Collection<ComponentProperty> autoCreateComponentProperties(Map<String,Object> map){
		String className=(String)map.get("className");
		String componentId=(String)map.get("componentId");
		ISupport support=this.retriveComponentSupport(className);
		Collection<ComponentProperty> properties=support.createComponentPropertysByComponentId(componentId);
		if(properties==null)return null;
		for(ComponentProperty cp:properties){
			cp.setComponentId(componentId);
			cp.setId(UUID.randomUUID().toString());
		}
		return properties;
	}
	
	private void fillComponentSupport(ComponentInfo component){
		ISupport support=this.retriveComponentSupport(component.getClassName());
		component.setSupport(this.copySupportProperty(support));
		if(component.getChildren()!=null){
			for(ComponentInfo c:component.getChildren()){
				this.fillComponentSupport(c);
			}
		}
	}
	
	@DataProvider
	public Collection<ClientEvent> loadEvents(String componentClassName) throws Exception{
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule(componentClassName.substring(componentClassName.lastIndexOf(".")+1));
		if(rule==null){
			return null;			
		}else{
			Collection<ClientEvent> events=new ArrayList<ClientEvent>();
			for(ClientEvent e:rule.getClientEvents().values()){
				events.add(e);
			}
			return events;
		}
	}
	
	@DataProvider
	public Collection<PropertyDef> loadLayoutProperties(String layoutName) throws Exception{
		if(layoutName==null)return null;
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule((layoutName.substring(0,1).toUpperCase()+layoutName.substring(1))+"Layout");
		if(rule==null){
			return null;
		}else{
			Collection<PropertyDef> props=new ArrayList<PropertyDef>();
			for(Property prop:rule.getProperties().values()){
				if(prop.isVisible()){
					PropertyDef p=new PropertyDef();
					p.setEditor(prop.getEditor());
					p.setName(prop.getName());
					p.setType(prop.getType());
					p.setEnumValues(this.buildPropertyEnumValues(prop).toString());
					props.add(p);					
				}
			}
			return props;
		}
	}
	@DataProvider
	public Collection<PropertyDef> loadLayoutConstraintProperties(String layoutName) throws Exception{
		if(layoutName==null)return null;
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule((layoutName.substring(0,1).toUpperCase()+layoutName.substring(1))+"LayoutConstraint");
		if(rule==null){
			return null;
		}else{
			Collection<PropertyDef> props=new ArrayList<PropertyDef>();
			for(Property prop:rule.getProperties().values()){
				if(prop.isVisible()){
					PropertyDef p=new PropertyDef();
					p.setEditor(prop.getEditor());
					p.setName(prop.getName());
					p.setType(prop.getType());
					p.setEnumValues(this.buildPropertyEnumValues(prop).toString());
					props.add(p);					
				}
			}
			return props;
		}
	}
	
	@DataProvider
	public Collection<PropertyDef> loadProperties(String componentClassName) throws Exception{
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=null;
		if(componentClassName.equals("View")){
			rule=ruleSet.getRule(componentClassName);			
		}else{
			rule=ruleSet.getRule(componentClassName.substring(componentClassName.lastIndexOf(".")+1));			
		}
		if(rule==null){
			return null;			
		}else{
			Collection<PropertyDef> coll=new ArrayList<PropertyDef>();
			for(Property p:rule.getProperties().values()){
				StringBuffer sb = buildPropertyEnumValues(p);
				ISupport support=this.retriveComponentSupport(componentClassName);
				boolean add=true;
				if(!p.isVisible()){
					add=false;
				}else if(support!=null && support.getPropertySupports()!=null){
					for(PropertySupport propertySupport:support.getPropertySupports()){
						if(!propertySupport.isShow() && propertySupport.getPropertyName().equals(p.getName())){
							add=false;												
							break;
						}
					}
				}
				if(add){
					PropertyDef pd=new PropertyDef();
					pd.setName(p.getName());
					pd.setEditor(p.getEditor());
					pd.setType(p.getType());
					pd.setEnumValues(sb.toString());
					coll.add(pd);										
				}
			}
			return coll;
		}
	}
	private StringBuffer buildPropertyEnumValues(Property p) {
		StringBuffer sb=new StringBuffer();
		String[] enumValues=p.getEnumValues();
		if(enumValues!=null){
			int i=0;
			for(String value:enumValues){
				if(i==0){
					sb.append(value);
				}else{
					sb.append(","+value);
				}
				i++;
			}
		}
		return sb;
	}
	
	@DataProvider
	public Collection<ComponentEvent> loadComponentEvents(String componentId){
		return componentManager.loadComponentEvents(componentId);
	}
	
	@DataResolver
	public void saveComponents(Collection<ComponentInfo> components) throws Exception{
		for(ComponentInfo c:components){
			EntityState state=EntityUtils.getState(c);
			if(state.equals(EntityState.NEW)){
				componentManager.insertComponent(c);
			}else if(state.equals(EntityState.MODIFIED)){
				componentManager.updateComponent(c);
			}else if(state.equals(EntityState.MOVED)){
				componentManager.updateComponent(c);
			}else if(state.equals(EntityState.DELETED)){
				pageManager.deletePageComponents(null,c.getId());
				componentManager.deleteComponent(c.getId());
			}
			if(c.getChildren()!=null){
				for(ComponentInfo component:c.getChildren()){
					component.setParentId(c.getId());
				}
				this.saveComponents(c.getChildren());
			}
			if(c.getComponentProperties()!=null){
				this.saveComponentProperties(c);
			}
			if(c.getComponentEvents()!=null){
				this.saveComponentEvents(c);
			}
			if(c.getLayoutProperties()!=null){
				this.saveComponentLayoutProperties(c);
			}
			if(c.getLayoutConstraintProperties()!=null){
				this.saveComponentLayoutConstraintProperties(c);
			}
		}
	}
	
	private void saveComponentLayoutConstraintProperties(ComponentInfo component){
		for(LayoutConstraintProperty layout:component.getLayoutConstraintProperties()){
			EntityState state=EntityUtils.getState(layout);
			if(state.equals(EntityState.NEW)){
				layout.setComponentId(component.getId());
				componentManager.insertComponentLayoutConstraintProperty(layout);
			}
			if(state.equals(EntityState.MODIFIED)){
				componentManager.updateComponentLayoutConstraintProperty(layout);
			}
			if(state.equals(EntityState.DELETED)){
				componentManager.deleteComponentLayoutConstraintProperty(layout.getId());
			}
		}
	}
	private void saveComponentLayoutProperties(ComponentInfo component){
		for(LayoutProperty layout:component.getLayoutProperties()){
			EntityState state=EntityUtils.getState(layout);
			if(state.equals(EntityState.NEW)){
				layout.setComponentId(component.getId());
				layoutPropertyManager.insertLayoutProperty(layout);
			}
			if(state.equals(EntityState.MODIFIED)){
				layoutPropertyManager.updateLayoutProperty(layout);
			}
			if(state.equals(EntityState.DELETED)){
				layoutPropertyManager.deleteLayoutProperty(layout.getId());
			}
		}
	}
	
	private void saveComponentEvents(ComponentInfo component){
		for(ComponentEvent event:component.getComponentEvents()){
			event.setComponentId(component.getId());
			EntityState state=EntityUtils.getState(event);
			if(state.equals(EntityState.NEW)){
				componentManager.insertComponentEvent(event);
			}else if(state.equals(EntityState.MODIFIED)){
				componentManager.updateComponentEvent(event);
			}else if(state.equals(EntityState.DELETED)){
				componentManager.deleteComponentEvent(event.getId());
			}
		}
	}
	
	private void saveComponentProperties(ComponentInfo component){
		for(ComponentProperty p:component.getComponentProperties()){
			p.setComponentId(component.getId());
			EntityState state=EntityUtils.getState(p);
			if(state.equals(EntityState.NEW)){
				componentManager.insertComponentProperty(p);
			}else if(state.equals(EntityState.MODIFIED)){
				componentManager.updateComponentProperty(p);
			}else if(state.equals(EntityState.DELETED)){
				componentManager.deleteComponentProperty(p.getId());
			}
		}
	}
	
	@DataProvider
	public Collection<ActionDef> loadActions(Map<String,Object> map) throws Exception{
		if(map==null)map=new HashMap<String,Object>();
		return actionDefManager.loadActionDefs(map);
	}
	@DataProvider
	public Collection<Entity> loadEntitys(Map<String,Object> map) throws Exception{
		if(map==null)map=new HashMap<String,Object>();
		return entityManager.loadEntitys(map);
	}
	@DataProvider
	public Collection<PackageInfo> loadPackages(Map<String,Object> map){
		if(map==null)map=new HashMap<String,Object>();
		String packgeType=(String)map.get("packageType");
		return packageManager.loadPackage(PackageType.valueOf(packgeType), (String)map.get("parentId"));
	}
	
	@DataProvider
	public Collection<ComponentProperty> loadComponentProperties(String componentId){
		return componentManager.loadComponentProperties(componentId);
	}
	
	@DataProvider
	public Collection<LayoutProperty> loadComponentLayoutProperties(String componentId){
		return layoutPropertyManager.loadLayoutProperties(componentId);
	}
	
	@DataProvider
	public Collection<LayoutConstraintProperty> loadComponentLayoutConstraintProperties(String componentId){
		return componentManager.loadComponentLayoutConstraintProperties(componentId);
	}
	
	@DataProvider
	public Collection<SupportDef> loadSupports(){
		Collection<SupportDef> result=new ArrayList<SupportDef>();
		for(ISupport s:supports){
			result.add(copySupportProperty(s));
		}
		return result;
	}

	private SupportDef copySupportProperty(ISupport support) {
		SupportDef def=new SupportDef();
		def.setAlone(support.isAlone());
		def.setChildrenName(buildChildNames(support));
		def.setContainer(support.isContainer());
		def.setDisplayName(support.getDisplayName());
		def.setFullClassName(support.getFullClassName());
		def.setIcon(support.getIcon());
		def.setSupportAction(support.isSupportAction());
		def.setSupportEntity(support.isSupportEntity());
		def.setSupportLayout(support.isSupportLayout());
		def.setPropertySupports(support.getPropertySupports());
		return def;
	}
	
	private String[] buildChildNames(ISupport s){
		if(s.getChildren()!=null){
			String[] str=new String[s.getChildren().size()];
			int i=0;
			for(ISupport support:s.getChildren()){
				str[i]=support.getFullClassName();
				i++;
			}
			return str;
		}else{
			return null;
		}
	}
	
	public ActionDefManager getActionDefManager() {
		return actionDefManager;
	}
	public void setActionDefManager(ActionDefManager actionDefManager) {
		this.actionDefManager = actionDefManager;
	}
	private void buildComponentSupport(Collection<ComponentInfo> components){
		for(ComponentInfo component:components){
			this.fillComponentSupport(component);
			ISupport support=retriveComponentSupport(component.getClassName());
			if(support!=null){
				component.setSupport(copySupportProperty(support));
				component.setIcon("dorado/res/"+support.getIcon());				
			}
			if(component.getChildren()!=null){
				buildComponentSupport(component.getChildren());
			}
		}
	}
	
	private ISupport retriveComponentSupport(String className){
		ISupport target=null;
		for(ISupport support:supports){
			if(support.getFullClassName().equals(className)){
				target=support;
				break;
			}
		}
		return target;
	}
	
	public LayoutPropertyManager getLayoutPropertyManager() {
		return layoutPropertyManager;
	}
	public void setLayoutPropertyManager(LayoutPropertyManager layoutPropertyManager) {
		this.layoutPropertyManager = layoutPropertyManager;
	}
	public RuleSetHelper getRuleSetHelper() {
		return ruleSetHelper;
	}
	public void setRuleSetHelper(RuleSetHelper ruleSetHelper) {
		this.ruleSetHelper = ruleSetHelper;
	}
	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
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
	
	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		supports=applicationContext.getBeansOfType(ISupport.class).values();
	}
}
