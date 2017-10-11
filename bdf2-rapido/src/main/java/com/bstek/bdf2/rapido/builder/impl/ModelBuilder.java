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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.rapido.Constants;
import com.bstek.bdf2.rapido.builder.AbstractBuilder;
import com.bstek.bdf2.rapido.domain.Action;
import com.bstek.bdf2.rapido.domain.ActionDef;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.KeyGenerateType;
import com.bstek.bdf2.rapido.domain.Mapping;
import com.bstek.bdf2.rapido.domain.MappingSource;
import com.bstek.bdf2.rapido.domain.MetaData;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.bdf2.rapido.domain.Parameter;
import com.bstek.bdf2.rapido.domain.Validator;
import com.bstek.bdf2.rapido.domain.ValidatorProperty;
import com.bstek.bdf2.rapido.manager.EntityManager;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.BasePropertyDef;
import com.bstek.dorado.data.type.property.Reference;
import com.bstek.dorado.idesupport.model.Property;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;

/**
 * 主要用于创建位于Model中的DataType
 * @author jacky.gao@bstek.com
 * @since 2012-7-18
 */
public class ModelBuilder extends AbstractBuilder{
	private EntityManager entityManager;
	public Element build(PageInfo page,RuleSet ruleSet) throws Exception{
		Rule modelRule=ruleSet.getRule("Model");
		BaseElement rootElement=new BaseElement(modelRule.getNodeName());
		Collection<Entity> pageEntities=retriveAllPageEntity(page);
		Map<String,Element> elementMap=new HashMap<String,Element>();
		Map<String,String> map=new HashMap<String,String>();
		for(Entity entity:pageEntities){
			buildDataTypeElement(entity,ruleSet,elementMap);
			if(!map.containsKey(entity.getName())){
				rootElement.add(this.createDataProvider(entity));			
				map.put(entity.getName(), entity.getName());
			}
		}
		Map<String,ActionDef> actionMap=new HashMap<String,ActionDef>();
		for(ComponentInfo component:page.getComponents()){
			this.retriveAllActionDefs(component, actionMap);
		}
		for(ActionDef actionDef:actionMap.values()){
			rootElement.add(this.createDataResolver(actionDef));
		}
		for(Element element:elementMap.values()){
			rootElement.add(element);
		}
		
		return rootElement;
	}
	
	private void retriveAllActionDefs(ComponentInfo component,Map<String,ActionDef> actionMap){
		ActionDef actionDef=component.getActionDef();
		if(actionDef!=null && !actionMap.containsKey(actionDef.getId())){
			actionMap.put(actionDef.getId(), actionDef);
		}
		if(component.getChildren()!=null){
			for(ComponentInfo c:component.getChildren()){
				this.retriveAllActionDefs(c, actionMap);
			}
		}
	}
	
	private Element createDataProvider(Entity entity){
		Element element=new BaseElement("DataProvider");
		element.addAttribute("type", "direct");
		String entityName=entity.getName();
		element.addAttribute("name", "dataProvider"+entityName.substring(0,1).toUpperCase()+entityName.substring(1,entityName.length()));
		element.add(this.createPropertyElement("interceptor", "spring:bdf.rapido.commonPR#loadEntityData"));
		Element metaData=this.createPropertyElement("metaData", null);
		metaData.add(this.createPropertyElement(Constants.QUERY_SQL, entity.getQuerySql()));
		element.add(metaData);
		return element;
	}
	
	private Element createDataResolver(ActionDef actionDef){
		Element element=new BaseElement("DataResolver");
		element.addAttribute("type", "direct");
		String actionName=actionDef.getName();
		element.addAttribute("name", "dataResolver"+actionName.substring(0,1).toUpperCase()+actionName.substring(1,actionName.length()));
		if(actionDef.getEntity()==null){
			element.add(this.createPropertyElement("interceptor", "spring:bdf.rapido.commonPR#ajaxInvoke"));			
		}else{
			element.add(this.createPropertyElement("interceptor", "spring:bdf.rapido.commonPR#saveEntityData"));						
		}
		Element metaData=this.createPropertyElement("metaData", null);
		if(StringHelper.isNotEmpty(actionDef.getScript())){
			metaData.add(this.createPropertyElement(Constants.BEAN_SHELL_SCRIPT, actionDef.getScript()));
		}
		if(actionDef.getActions()!=null){
			int i=0;
			StringBuffer sb=new StringBuffer();
			for(Action action:actionDef.getActions()){
				if(i==0){
					sb.append(action.getBeanId());
				}else{
					sb.append(",");
					sb.append(action.getBeanId());				
				}
				i++;
			}
			if(i>0){
				metaData.add(createPropertyElement(Constants.ACTION_BEANS_ID,sb.toString()));
			}
		}
		if(actionDef.getEntity()!=null){
			metaData.add(this.createPropertyElement(Constants.ENTITY_TABLE_NAME, actionDef.getEntity().getTableName()));
			metaData.add(this.createPropertyElement(Constants.ENTITY_TABLE_PRIMARY_KEYS, this.retrivePrimaryKeys(actionDef.getEntity())));			
		}
		element.add(metaData);
		return element;
	}
	
	private void buildDataTypeElement(Entity entity,RuleSet ruleSet,Map<String,Element> elementMap) throws Exception{
		if(elementMap.containsKey(entity.getId())){
			return;
		}
		String parentId=entity.getParentId();
		if(parentId==null){
			//创建顶级dataType
			Element dataTypeElement = createDataType(entity, ruleSet);
			elementMap.put(entity.getId(),dataTypeElement);
		}else{
			Entity parentEntity=entityManager.loadEntity(parentId);
			Element dataTypeElement = createDataType(entity, ruleSet);
			if(!elementMap.containsKey(parentId)){
				//查出当前entity上级entity并创建为DataType
				buildDataTypeElement(parentEntity,ruleSet,elementMap);
				Element parentElement=elementMap.get(parentId);
				elementMap.put(entity.getId(),dataTypeElement);
				parentElement.add(this.createReferencePropertyDef(entity, ruleSet,false));
			}else if(elementMap.containsKey(parentId)){
				Element parentElement=elementMap.get(parentId);
				elementMap.put(entity.getId(),dataTypeElement);
				parentElement.add(this.createReferencePropertyDef(entity, ruleSet,false));
			}
			if(entity.isRecursive()){
				dataTypeElement.add(this.createReferencePropertyDef(entity, ruleSet,true));
			}
		} 
	}
	
	private Element createReferencePropertyDef(Entity entity,RuleSet ruleSet,boolean isSelf){
		String nodeName=Reference.class.getSimpleName();
		Rule rule=ruleSet.getRule(nodeName);
		Element referenceDefElement=new BaseElement(rule.getNodeName());
		referenceDefElement.addAttribute("name", entity.getName());
		//创建dataType与dataProvider属性
		Element dataTypeElement=createPropertyElement("dataType",null);
		if(isSelf){
			dataTypeElement.setText("[SELF]");			
		}else{
			dataTypeElement.setText("["+ entity.getName()+"]");						
		}
		referenceDefElement.add(dataTypeElement);
		String entityName=entity.getName();
		Element dataProviderElement=createPropertyElement("dataProvider","dataProvider"+entityName.substring(0,1).toUpperCase()+entityName.substring(1,entityName.length()));
		if(entity.getPageSize()>0){
			Element pageSizeElement=createPropertyElement("pageSize",String.valueOf(entity.getPageSize()));
			referenceDefElement.add(pageSizeElement);
		}
		referenceDefElement.add(dataProviderElement);
		referenceDefElement.add(createReferenceParameterElement(entity));
		return referenceDefElement;
	}
	
	private Element createReferenceParameterElement(Entity entity){
		Element element = createPropertyElement("parameter",null);
		BaseElement entityElement = new BaseElement("Entity");
		element.add(entityElement);
		if(entity.getParameters()!=null){
			Element entityQueryParametersElement = createPropertyElement(Constants.ENTITY_QUERY_PARAMETERS,null);
			entityQueryParametersElement.add(this.createEntityQueryParametersElement(entity.getParameters()));
			entityElement.add(entityQueryParametersElement);
			for(Parameter p:entity.getParameters()){
				entityElement.add(createPropertyElement(p.getName(),p.getValue()));						
			}
		}
		if(entity.getEntityFields()!=null){
			Element entityFieldsElement=this.createPropertyElement("entityFields", null);
			entityFieldsElement.add(this.createEntityFieldsCollectionElement(entity));
			entityElement.add(entityFieldsElement);
		}
		return element;
		
	}

	
	private BaseElement createDataType(Entity entity, RuleSet ruleSet)
			throws Exception {
		String nodeName=EntityDataType.class.getSimpleName();
		XmlNode xmlNode=EntityDataType.class.getAnnotation(XmlNode.class);
		if(xmlNode!=null && StringHelper.isNotEmpty(xmlNode.nodeName())){
			nodeName=xmlNode.nodeName();
		}
		Rule rule=ruleSet.getRule(nodeName);
		BaseElement dataTypeElement=new BaseElement(rule.getNodeName());
		dataTypeElement.addAttribute("name",entity.getName());
		if(entity.getEntityFields()!=null){
			for(EntityField field:entity.getEntityFields()){
				dataTypeElement.add(buildPropertyDefElement(field,ruleSet));
			}
		}
		return dataTypeElement;
	}
	
	private Element buildPropertyDefElement(EntityField field,RuleSet ruleSet) throws Exception{
		String nodeName=BasePropertyDef.class.getSimpleName();
		XmlNode xmlNode=BasePropertyDef.class.getAnnotation(XmlNode.class);
		if(xmlNode!=null && StringHelper.isNotEmpty(xmlNode.nodeName())){
			nodeName=xmlNode.nodeName();
		}
		MetaData metadata=field.getMetaData();
		Rule rule=ruleSet.getRule(nodeName);
		BaseElement propertyDefElement=new BaseElement(rule.getNodeName());
		propertyDefElement.addAttribute("name", field.getName());
		if(field.getMapping()!=null){
			propertyDefElement.add(createMappingElement(field.getMapping()));			
		}else if(metadata!=null && metadata.getMapping()!=null){
			propertyDefElement.add(createMappingElement(metadata.getMapping()));						
		}
		if(StringHelper.isEmpty(field.getDefaultValue()) && field.isPrimaryKey() && field.getKeyGenerateType().equals(KeyGenerateType.custom)){
			propertyDefElement.add(this.createPropertyElement("defaultValue", "0"));//防止因没有在页面手工赋值导致dorado7不提交到后台的情况发生
		}
		Map<String,Property> propertiesMap=rule.getProperties();
		for(Property prop:propertiesMap.values()){
			 String propertyName=prop.getName();
			 if(propertyName.equals("mapping") || propertyName.equals("metaData")){
				 continue;
			 }
			 if(PropertyUtils.isReadable(field, propertyName)){
				 String propertyValue = BeanUtils.getProperty(field, propertyName);
				 if(StringUtils.hasText(propertyValue)){
					 propertyDefElement.add(createPropertyElement(propertyName,propertyValue));
				 }else if(metadata!=null && PropertyUtils.isReadable(metadata, propertyName)){
					 propertyValue = BeanUtils.getProperty(metadata, propertyName);
					 if(StringUtils.hasText(propertyValue)){
						 propertyDefElement.add(createPropertyElement(propertyName,propertyValue));						 
					 }
				 }
			 }else if(metadata!=null && PropertyUtils.isReadable(metadata, propertyName)){
				 String propertyValue = BeanUtils.getProperty(metadata, propertyName);
				 if(StringUtils.hasText(propertyValue)){
					 propertyDefElement.add(createPropertyElement(propertyName,propertyValue));						 
				 }
			 }
		}
		if(field.getValidators()!=null && field.getValidators().size()>0){
			for(Validator v:field.getValidators()){
				Element validatorElement=this.buildValidatorElement(v);
				propertyDefElement.add(validatorElement);
			}
		}else if(metadata!=null && metadata.getValidators()!=null){
			for(Validator v:metadata.getValidators()){
				Element validatorElement=this.buildValidatorElement(v);
				propertyDefElement.add(validatorElement);
			}
		}
		return propertyDefElement;
	}

	private Element buildValidatorElement(Validator validator){
		BaseElement element=new BaseElement("Validator");
		int pos=validator.getName().lastIndexOf("Validator");
		String validatorType=validator.getName().substring(0,pos);
		validatorType=validatorType.substring(0,1).toLowerCase()+validatorType.substring(1,validatorType.length());
		element.addAttribute("type",validatorType);
		if(validator.getProperties()!=null){
			for(ValidatorProperty vp:validator.getProperties()){
				if(StringHelper.isNotEmpty(vp.getName()) && StringHelper.isNotEmpty(vp.getValue())){
					Element propertyElement=createPropertyElement(vp.getName(),vp.getValue());
					element.add(propertyElement);					
				}
			}
		}
		return element;
	}
	
	private Element createMappingElement(Mapping mapping) {
		Element element = createPropertyElement("mapping",null);
		Element mapValuesElement=createPropertyElement("mapValues",null);
		if (mapping.getSource().equals(MappingSource.custom)) {
			String customKeyValues=mapping.getCustomKeyValue();
			if(StringUtils.hasText(customKeyValues)){
				BaseElement collectionElement=new BaseElement("Collection");
				mapValuesElement.add(collectionElement);
				String[] keyValues=customKeyValues.split(";");
				for(String keyValue:keyValues){
					String[] kvs=keyValue.split("=");
					BaseElement entityElement=new BaseElement("Entity");
					collectionElement.add(entityElement);
					Element keyElement=createPropertyElement("key",kvs[0]);
					Element valueElement=createPropertyElement("value",kvs[1]);
					entityElement.add(keyElement);
					entityElement.add(valueElement);
				}
			}
		}
		if (mapping.getSource().equals(MappingSource.table)) {
			mapValuesElement.setText("${dorado.getDataProvider(\"bdf.rapido.commonPR#mappingProvider\").getResult(argument.arg"+mapping.getId().replace("-", "")+")}");
			if(mapping.getKeyField()!=null){
				BaseElement keyPropertyElement=new BaseElement("Property");
				keyPropertyElement.addAttribute("name", "keyProperty");
				keyPropertyElement.setText(mapping.getKeyField());
				element.add(keyPropertyElement);
			}
			if(mapping.getValueField()!=null){
				Element valuePropertyElement=createPropertyElement("valueProperty",mapping.getValueField());
				element.add(valuePropertyElement);
			}
		}
		element.add(mapValuesElement);
		return element;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
