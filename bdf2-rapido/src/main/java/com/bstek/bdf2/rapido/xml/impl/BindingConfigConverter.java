/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.xml.impl;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.hibernate.annotations.common.util.StringHelper;

import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.xml.AbstractConverter;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.view.widget.tree.BindingConfig;

public class BindingConfigConverter extends AbstractConverter {
	public Element convert(ComponentInfo component, RuleSet ruleSet,
			Element rootElement) throws Exception {
		String name=BindingConfig.class.getSimpleName();
		XmlNode node=BindingConfig.class.getAnnotation(XmlNode.class);
		if(node!=null && StringHelper.isNotEmpty(node.nodeName())){
			name=node.nodeName();
		}
		Rule rule=ruleSet.getRule(name);
		BaseElement element = fillElement(component,ruleSet,rule,rootElement);
		String[] dsInfo=this.retriveDataSetAndDataPath(component);
		if(dsInfo!=null){
			String dataPath=dsInfo[1];
			int pos=dataPath.lastIndexOf(".");
			if(pos!=-1){
				dataPath=dataPath.substring(pos+1);
			}
			Element childrenPropertyElement=new BaseElement("Property");
			childrenPropertyElement.addAttribute("name", "childrenProperty");
			childrenPropertyElement.setText(dataPath);
			element.add(childrenPropertyElement);
		}
		if(component.getChildren()!=null){
			for(ComponentInfo c:component.getChildren()){
				this.buildChildren(element,c,ruleSet,rootElement);							
			}
		}
		return element;
	}
	

	public boolean support(ComponentInfo component) {
		return BindingConfig.class.getName().equals(component.getClassName());
	}
}
