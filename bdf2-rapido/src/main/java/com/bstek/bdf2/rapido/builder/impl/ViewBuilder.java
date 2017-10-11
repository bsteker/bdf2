/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.builder.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.rapido.Constants;
import com.bstek.bdf2.rapido.builder.AbstractBuilder;
import com.bstek.bdf2.rapido.domain.Action;
import com.bstek.bdf2.rapido.domain.ActionDef;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.LayoutProperty;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.bdf2.rapido.domain.Parameter;
import com.bstek.bdf2.rapido.manager.EntityManager;
import com.bstek.bdf2.rapido.xml.IConverter;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.view.widget.data.DataSet;

public class ViewBuilder extends AbstractBuilder implements ApplicationContextAware{
	private EntityManager entityManager;
	private Collection<IConverter> converters;
	public Element build(PageInfo page, RuleSet ruleSet) throws Exception {
		Rule viewRule=ruleSet.getRule("View");
		BaseElement rootElement=new BaseElement(viewRule.getNodeName());
		String layout=buildLayout(page);
		if(layout!=null){
			rootElement.addAttribute("layout",layout);
		}
		Collection<Entity> allEntity=retriveAllPageEntity(page);
		Map<String,String> dsMap=new HashMap<String,String>();
		Map<String,String> actionMap=new HashMap<String,String>();
		for(Entity entity:allEntity){
			if(dsMap.containsKey(entity.getId())){
				continue;
			}
			Element ele=createDataSetElement(entity,ruleSet);
			if(ele!=null){
				rootElement.add(ele);	
				dsMap.put(entity.getId(), entity.getId());
			}
		}
		for(ComponentInfo component:page.getComponents()){
			createAction(component,page,rootElement,actionMap);
			for(IConverter converter:converters){
				if(converter.support(component)){
					rootElement.add(converter.convert(component, ruleSet,rootElement));		
					break;
				}
			}
		}
		
		if(page.getProperties()!=null){
			this.fillViewProperties(rootElement, viewRule, page);
		}
		return rootElement;
	}
	
	private void fillViewProperties(Element element,Rule rule,PageInfo page){
		Map<String,Property> properties=rule.getProperties();
		for(String key:properties.keySet()){
			Property p=properties.get(key);
			if(p!=null){
				String propertyName=p.getName();
				String propertyValue=getPropertyValue(page.getProperties(),propertyName);
				if(StringUtils.hasText(propertyValue)){
					BaseElement propertyElement=new BaseElement("Property");
					propertyElement.addAttribute("name", propertyName);
					propertyElement.setText(propertyValue);
					element.add(propertyElement);
				}
			}
		}
	}
	
	private String getPropertyValue(Collection<ComponentProperty> properties,String propertyName){
		String value=null;
		for(ComponentProperty cp:properties){
			if(cp.getName().equals(propertyName)){
				value=cp.getValue();
				break;
			}
		}
		return value;
	}
	
	private void createAction(ComponentInfo component,PageInfo page,Element viewRootElement,Map<String,String> actionMap){
		if(component.getActionDef()!=null){
			ActionDef actionDef=component.getActionDef();
			if(!actionMap.containsKey(actionDef.getId())){
				String actionName=actionDef.getName();
				String dataResolverName= "dataResolver"+actionName.substring(0,1).toUpperCase()+actionName.substring(1,actionName.length());
				actionMap.put(actionDef.getId(), actionName);
				BaseElement element=new BaseElement("UpdateAction");
				element.add(this.createPropertyElement("dataResolver",dataResolverName));;
				if(actionDef.getEntity()==null){
					element.add(this.createPropertyElement("alwaysExecute","true"));
				}else{
					element.add(createUpdateItem(actionDef,page));			
				}
				element.addAttribute("id",actionDef.getName());
				if(StringHelper.isNotEmpty(actionDef.getBeforeExecuteScript())){
					element.add(this.createEvent("beforeExecute", actionDef.getBeforeExecuteScript()));
				}
				if(StringHelper.isNotEmpty(actionDef.getOnExecuteScript())){
					element.add(this.createEvent("onSuccess", actionDef.getOnExecuteScript()));
				}
				if(StringHelper.isNotEmpty(actionDef.getConfirmMessage())){
					Element confirmMsgElement=new BaseElement("Property");
					confirmMsgElement.addAttribute("name","confirmMessage");
					confirmMsgElement.setText(actionDef.getConfirmMessage());
					element.add(confirmMsgElement);
				}
				if(StringHelper.isNotEmpty(actionDef.getSuccessMessage())){
					Element successMessageElement=new BaseElement("Property");
					successMessageElement.addAttribute("name","successMessage");
					successMessageElement.setText(actionDef.getSuccessMessage());
					element.add(successMessageElement);
				}
				element.add(this.createActionParameter(actionDef));
				viewRootElement.add(element);
			}
		}
		if(component.getChildren()!=null){
			for(ComponentInfo child:component.getChildren()){
				this.createAction(child, page, viewRootElement,actionMap);
			}
		}
	}
	
	
	private Element createActionParameter(ActionDef actionDef){
		Element element=new BaseElement("Property");
		element.addAttribute("name", "parameter");
		Element entityElement=new BaseElement("Entity");
		element.add(entityElement);
		if(actionDef.getParameters()!=null){
			for(Parameter p:actionDef.getParameters()){
				entityElement.add(this.createPropertyElement(p.getName(), p.getValue()));
			}
		}
		
		for(Action action:actionDef.getActions()){
			if(action.getParameters()!=null){
				for(Parameter p:action.getParameters()){
					if(StringHelper.isNotEmpty(p.getName()) && StringHelper.isNotEmpty(p.getValue())){
						entityElement.add(this.createPropertyElement(p.getName(),p.getValue()));
					}
				}
			}
		}
		if(actionDef.getEntity()!=null){	
			Entity entity=actionDef.getEntity();
			if(entity.getEntityFields()!=null){
				Element entityFieldsElement=this.createPropertyElement(Constants.ENTITY_FIELDS, null);
				entityFieldsElement.add(this.createEntityFieldsCollectionElement(entity));
				entityElement.add(entityFieldsElement);
			}
		}
		return element;
	}
	
	private Element createUpdateItem(ActionDef actionDef,PageInfo page){
		Entity topEntity=actionDef.getEntity();
		if(actionDef.getEntity().getParentId()!=null){
			topEntity=this.findTopEntity(page);			
		}
		String dataSetId="dataSet"+topEntity.getName();
		Element updateItemElement=new BaseElement("UpdateItem");
		updateItemElement.add(this.createPropertyElement("dataSet", dataSetId));
		updateItemElement.add(this.createPropertyElement("alias", "data"));
		return updateItemElement;
	}
	
	private Entity findTopEntity(PageInfo page){
		Entity topEntity=null;
		for(Entity entity:this.retriveAllPageEntity(page)){
			if(StringHelper.isEmpty(entity.getParentId())){
				topEntity=entity;
				break;
			}
		}
		return topEntity;
	}
	
	private Element createEvent(String eventName,String script){
		BaseElement element=new BaseElement("ClientEvent");
		element.addAttribute("name", eventName);
		element.setText(script);
		return element;
	}

	private String buildLayout(PageInfo page){
		StringBuffer sb=new StringBuffer();
		if(StringHelper.isNotEmpty(page.getLayout())){
			sb.append(page.getLayout());
		}
		if(page.getLayoutProperties()!=null){
			int i=0;
			for(LayoutProperty lp:page.getLayoutProperties()){
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
	
	private Element createDataSetElement(Entity entity,RuleSet ruleSet) throws Exception{
		if(StringHelper.isEmpty(entity.getParentId())){
			BaseElement element = generateDataSetElement(ruleSet);
			element.addAttribute("id", "dataSet"+entity.getName());
			String entityName=entity.getName();
			Element dataTypeElement=createPropertyElement("dataType","["+entityName+"]");
			element.add(dataTypeElement);
			Element dataProviderElement=createPropertyElement("dataProvider","dataProvider"+entityName.substring(0,1).toUpperCase()+entityName.substring(1,entityName.length()));
			element.add(dataProviderElement);
			element.add(createDataSetParameterElement(entity));
			if(entity.getPageSize()>0){
				Element pageSizeElement=createPropertyElement("pageSize",String.valueOf(entity.getPageSize()));
				element.add(pageSizeElement);
			}
			return element;			
		}else{
			return null;
		}
	}

	private Element createDataSetParameterElement(Entity entity){
		Element element = this.createPropertyElement("parameter", null);
		BaseElement entityElement = new BaseElement("Entity");
		element.add(entityElement);
		if(entity.getParameters()!=null){
			Element entityQueryParametersElement = createPropertyElement(Constants.ENTITY_QUERY_PARAMETERS,null);
			entityQueryParametersElement.add(this.createEntityQueryParametersElement(entity.getParameters()));
			entityElement.add(entityQueryParametersElement);
			for(Parameter p:entity.getParameters()){
				entityElement.add(this.createPropertyElement(p.getName(), p.getValue()));
			}
		}
		return element;
	}
	
	private BaseElement generateDataSetElement(RuleSet ruleSet) {
		String nodeName=DataSet.class.getSimpleName();
		XmlNode xmlNode=DataSet.class.getAnnotation(XmlNode.class);
		if(xmlNode!=null && StringHelper.isNotEmpty(xmlNode.nodeName())){
			nodeName=xmlNode.nodeName();
		}
		Rule rule=ruleSet.getRule(nodeName);
		BaseElement element=new BaseElement(rule.getNodeName());
		return element;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		converters=applicationContext.getBeansOfType(IConverter.class).values();
	}
}
