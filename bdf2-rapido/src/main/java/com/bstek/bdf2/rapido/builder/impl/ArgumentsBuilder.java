package com.bstek.bdf2.rapido.builder.impl;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import com.bstek.bdf2.rapido.builder.IBuilder;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.Mapping;
import com.bstek.bdf2.rapido.domain.MappingSource;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.dorado.idesupport.model.RuleSet;

public class ArgumentsBuilder implements IBuilder {

	public Element build(PageInfo page, RuleSet ruleSet) throws Exception {
		BaseElement element=new BaseElement("Arguments");
		Map<String,Mapping> map=new HashMap<String,Mapping>();
		for(ComponentInfo component:page.getComponents()){
			this.retriveMapping(component, map);
		}
		for(Mapping mapping:map.values()){
			BaseElement attributeElement=new BaseElement("Argument");
			attributeElement.addAttribute("name","arg"+mapping.getId().replace("-", ""));
			BaseElement propertyElement=new BaseElement("Property");
			propertyElement.addAttribute("name","value");
			propertyElement.setText(mapping.getQuerySql());
			attributeElement.add(propertyElement);
			element.add(attributeElement);			
		}
		return element;
	}

	
	private void retriveMapping(ComponentInfo component,Map<String,Mapping> map){
		if(component.getEntity()!=null){
			for(EntityField ef:component.getEntity().getEntityFields()){
				Mapping mapping=ef.getMapping();
				if(mapping!=null){
					if(mapping.getSource().equals(MappingSource.table) && !map.containsKey(mapping.getId())){
						map.put(mapping.getId(),mapping);
					}
				}else if(ef.getMetaData()!=null){
					mapping=ef.getMetaData().getMapping();
					if(mapping!=null && mapping.getSource().equals(MappingSource.table) && !map.containsKey(mapping.getId())){
						map.put(mapping.getId(),mapping);
					}
				}
			}
		}
		if(component.getChildren()!=null){
			for(ComponentInfo c:component.getChildren()){
				this.retriveMapping(c, map);
			}
		}
	}
}
