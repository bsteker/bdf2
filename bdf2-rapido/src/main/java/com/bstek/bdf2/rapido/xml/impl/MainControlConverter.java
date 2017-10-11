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

import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.xml.AbstractConverter;
import com.bstek.dorado.idesupport.model.RuleSet;

public class MainControlConverter extends AbstractConverter {

	public Element convert(ComponentInfo component, RuleSet ruleSet,
			Element rootElement) throws Exception {
		BaseElement element = new BaseElement("MainControl");		
		if(component.getChildren()!=null){
			for(ComponentInfo c:component.getChildren()){
				this.buildChildren(element,c,ruleSet,rootElement);							
			}
		}
		return element;
	}

	public boolean support(ComponentInfo component) {
		return "MainControl".equals(component.getClassName());
	}

}
