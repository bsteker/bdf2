/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.xml;

import java.util.Collection;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.bdf2.rapido.domain.ComponentEvent;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.LayoutConstraintProperty;
import com.bstek.bdf2.rapido.domain.LayoutProperty;
import com.bstek.bdf2.rapido.manager.EntityManager;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;
import com.bstek.dorado.view.widget.grid.DataColumn;


public abstract class AbstractConverter implements ApplicationContextAware,IConverter{
	private Collection<ISupport> supports;
	private Collection<IConverter> converters;
	private EntityManager entityManager;
	private String getPropertyValue(Collection<ComponentProperty> componentProperties,String propertyName){
		if(componentProperties==null)return null;
		String propertyValue=null;
		for(ComponentProperty property:componentProperties){
			if(property.getName().equals(propertyName)){
				propertyValue=property.getValue();
				break;
			}
		}
		return propertyValue;
	}

	protected void fillDataSetAndDataPathProperty(Element element,ComponentInfo component,boolean isCollection){
		String[] dataSetDataPath=this.retriveDataSetAndDataPath(component);
		if(dataSetDataPath!=null){
			if(StringHelper.isNotEmpty(dataSetDataPath[0])){
				BaseElement dataSetElement=new BaseElement("Property");	
				dataSetElement.addAttribute("name", "dataSet");
				dataSetElement.setText(dataSetDataPath[0]);
				element.add(dataSetElement);
			}
			if(StringHelper.isNotEmpty(dataSetDataPath[1]) && !hasDataPath(component)){
				BaseElement dataPathElement=new BaseElement("Property");
				dataPathElement.addAttribute("name", "dataPath");
				String dataPath=dataSetDataPath[1];
				if(!isCollection){
					int pos=dataPath.lastIndexOf(".");
					if(pos>0){
						dataPath=dataPath.substring(0,pos)+".#"+dataPath.substring(pos+1,dataPath.length());
					}					
				}
				
				dataPathElement.setText(dataPath);
				element.add(dataPathElement);
			}
		}
	}
	
	private boolean hasDataPath(ComponentInfo component){
		boolean result=false;
		if(component.getComponentProperties()!=null){
			for(ComponentProperty p:component.getComponentProperties()){
				if("dataPath".equals(p.getName())){
					result=true;
					break;
				}
			}
		}
		return result;
	}
	
	private String getEventScript(Collection<ComponentEvent> componentEvents,String eventName){
		if(componentEvents==null)return null;
		String script=null;
		for(ComponentEvent event:componentEvents){
			if(eventName.equals(event.getName())){
				script=event.getScript();
				break;
			}
		}
		return script;
	}
	
	protected BaseElement fillElement(ComponentInfo component,RuleSet ruleSet,Rule rule,Element rootElement) {
		BaseElement element=new BaseElement(rule.getNodeName());
		String componentName=component.getName();
		String layout=component.getLayout();
		Map<String,Property> primitiveProperties=rule.getPrimitiveProperties();
		for(String key:primitiveProperties.keySet()){
			String propertyName=primitiveProperties.get(key).getName();
			if("layout".equals(propertyName)){
				element.addAttribute(propertyName, layout);
			}else if("id".equals(propertyName)){
				if(!component.getClassName().equals(AutoFormElement.class.getName()) && !component.getClassName().equals(DataColumn.class.getSimpleName())){
					element.addAttribute(propertyName, componentName);					
				}
			}else{
				String propertyValue=getPropertyValue(component.getComponentProperties(),propertyName);
				if(StringUtils.hasText(propertyValue)){
					element.addAttribute(propertyName, propertyValue);				
				}				
			}
		}
		Map<String,Property> properties=rule.getProperties();
		for(String key:properties.keySet()){
			Property p=properties.get(key);
			if(p!=null){
				String propertyName=p.getName();
				String propertyValue=getPropertyValue(component.getComponentProperties(),propertyName);
				if(StringUtils.hasText(propertyValue)){
					if(propertyName.equals("readOnly") && component.isReadOnly()){
						propertyValue="true";
					}
					BaseElement propertyElement=new BaseElement("Property");
					propertyElement.addAttribute("name", propertyName);
					propertyElement.setText(propertyValue);
					element.add(propertyElement);
				}
			}
		}
		if(component.isReadOnly() && properties.containsKey("readOnly")){
			String propertyValue=getPropertyValue(component.getComponentProperties(),"readOnly");
			if(StringHelper.isEmpty(propertyValue)){
				BaseElement readOnlyPropertyElement=new BaseElement("Property");
				readOnlyPropertyElement.addAttribute("name", "readOnly");
				readOnlyPropertyElement.setText("true");
				element.add(readOnlyPropertyElement);				
			}
		}
		if(component.getActionDef()!=null){
			Element actionElement=new BaseElement("Property");
			actionElement.addAttribute("name","action");
			actionElement.setText(component.getActionDef().getName());
			element.add(actionElement);
		}
		
		Map<String,ClientEvent> events=rule.getClientEvents();
		for(String key:events.keySet()){
			String eventName=events.get(key).getName();
			String eventScript=getEventScript(component.getComponentEvents(),eventName);
			if(StringUtils.hasText(eventScript)){
				BaseElement eventElement=new BaseElement("ClientEvent");
				eventElement.addAttribute("name", eventName);
				eventElement.setText(eventScript);
				element.add(eventElement);
			}
		}
		String layoutInfo=buildLayout(component);
		if(layoutInfo!=null){
			element.addAttribute("layout",layoutInfo);			
		}
		String layoutConstraint=buildLayoutContraint(component);
		if(layoutConstraint!=null){
			element.addAttribute("layoutConstraint",layoutConstraint);			
		}
		return element;
	}
	
	private String buildLayoutContraint(ComponentInfo component){
		StringBuffer sb=new StringBuffer();
		if(component.getLayoutConstraintProperties()!=null){
			int i=0;
			for(LayoutConstraintProperty lcp:component.getLayoutConstraintProperties()){
				if(StringHelper.isNotEmpty(lcp.getName()) && StringHelper.isNotEmpty(lcp.getValue())){
					if(i>0){
						sb.append(";");
					}
					sb.append(lcp.getName()+":"+lcp.getValue());
					i++;
				}
			}
		}
		if(sb.length()>0){
			return sb.toString();			
		}else{
			return null;
		}
	}
	
	private String buildLayout(ComponentInfo component){
		if(!retriveComponentSupport(component).isSupportLayout()){
			return null;
		}
		StringBuffer sb=new StringBuffer();
		if(StringHelper.isNotEmpty(component.getLayout())){
			sb.append(component.getLayout());
		}
		if(component.getLayoutProperties()!=null){
			int i=0;
			for(LayoutProperty lp:component.getLayoutProperties()){
				if(i==0){
					sb.append(" ");					
				}
				if(StringHelper.isNotEmpty(lp.getName()) && StringHelper.isNotEmpty(lp.getValue())){
					if(i>0){
						sb.append(";");
					}
					sb.append(lp.getName()+":"+lp.getValue());
					i++;
				}
			}
		}
		if(sb.length()>0){
			return sb.toString();
		}else{
			return null;
		}
	}
	
	protected void buildChildren(Element parentElement,ComponentInfo component,RuleSet ruleSet,Element rootElement){
		try{
			for(IConverter converter:this.converters){
				if(converter.support(component)){
					Element element=converter.convert(component, ruleSet, rootElement);			
					parentElement.add(element);
					break;
				}
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	protected String[] retriveDataSetAndDataPath(ComponentInfo component){
		String[] result=new String[2];
		Entity entity=component.getEntity();
		StringBuffer dataPath=new StringBuffer();
		if(entity==null){
			return null;
		}else{
			entity=getTopEntity(entity,dataPath);
		}
		result[0]="dataSet"+entity.getName();
		result[1]=dataPath.toString();
		return result;
	}
	
	private Entity getTopEntity(Entity entity,StringBuffer dataPath){
		if(StringHelper.isEmpty(entity.getParentId())){
			if(dataPath.length()>0){
				dataPath.insert(0, "#");
			}
			return entity;
		}else{
			int pointPos=dataPath.indexOf(".");
			if(pointPos==-1){
				dataPath.insert(0, "."+entity.getName());				
			}else{
				dataPath.insert(0, ".#"+entity.getName());								
			}
			return getTopEntity(entityManager.loadEntity(entity.getParentId()),dataPath);
		}
	}
	
	protected ISupport retriveComponentSupport(ComponentInfo component){
		ISupport support=null;
		for(ISupport s:this.supports){
			if(component.getClassName().equals(s.getFullClassName())){
				support=s;
				break;
			}
		}
		return support;
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.converters=applicationContext.getBeansOfType(IConverter.class).values();
		this.supports=applicationContext.getBeansOfType(ISupport.class).values();
	}
}
