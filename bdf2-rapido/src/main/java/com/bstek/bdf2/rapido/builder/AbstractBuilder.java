/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.bdf2.rapido.domain.Parameter;


public abstract class AbstractBuilder implements IBuilder {
	protected Collection<Entity> retriveAllPageEntity(PageInfo page){
		List<Entity> pageEntities=new ArrayList<Entity>();
		for(ComponentInfo component:page.getComponents()){
			if(component.getEntity()!=null)pageEntities.add(component.getEntity());
			retriveAllPageComponents(component.getChildren(),pageEntities);
		}
		return pageEntities;
	}
	
	protected Element createEntityQueryParametersElement(Collection<Parameter> parameters){
		Element entity=new BaseElement("Entity");
		for(Parameter p:parameters){
			entity.add(this.createPropertyElement(p.getName(), null));
		}
		return entity;
	}
	
	protected Element createPropertyElement(String name,String value){
		Element element=new BaseElement("Property");
		element.addAttribute("name", name);
		if(value!=null){
			element.setText(value);			
		}
		return element;
	}
	
	protected String retrivePrimaryKeys(Entity entity){
		StringBuffer sb=new StringBuffer();
		if(entity.getEntityFields()!=null){
			int i=0;
			for(EntityField ef:entity.getEntityFields()){
				if(ef.isPrimaryKey()){
					if(i>0){
						sb.append(",");
					}
					sb.append(ef.getName());
					i++;
				}
			}			
		}
		return sb.toString();
	}
	
	protected Element createEntityFieldsCollectionElement(Entity fieldEntity){
		Collection<EntityField> entityFields=fieldEntity.getEntityFields();
		String entityTableName=fieldEntity.getTableName();
		Element collection=new BaseElement("Collection");
		for(EntityField ef:entityFields){
			if(!entityTableName.equals(ef.getTableName())){
				continue;
			}
			Element entity = new BaseElement("Entity");
			entity.add(this.createPropertyElement("name", ef.getName()));
			entity.add(this.createPropertyElement("keyGenerator", ef.getKeyGenerator()));
			entity.add(this.createPropertyElement("keyGenerateType", ef.getKeyGenerateType().toString()));
			entity.add(this.createPropertyElement("submittable", String.valueOf(ef.isSubmittable())));
			collection.add(entity);
		}
		return collection;
	}
	
	private void retriveAllPageComponents(Collection<ComponentInfo> componentInfos,List<Entity> pageEntities){
		if(componentInfos==null)return;
		for(ComponentInfo info:componentInfos){
			if(info.getEntity()!=null){
				pageEntities.add(info.getEntity());
				if(info.getChildren()!=null){
					retriveAllPageComponents(info.getChildren(),pageEntities);					
				}
			}
			if(info.getChildren()!=null){
				this.retriveAllPageComponents(info.getChildren(), pageEntities);
			}
		}
	}
}
