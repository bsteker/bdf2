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
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.view.widget.base.SimpleIconButton;

public class SimpleIconButtonConverter extends AbstractConverter {

	public Element convert(ComponentInfo component, RuleSet ruleSet,
			Element rootElement) throws Exception {
		String name=SimpleIconButton.class.getSimpleName();
		XmlNode node=SimpleIconButton.class.getAnnotation(XmlNode.class);
		if(node!=null){
			name=node.nodeName();
		}
		Rule rule=ruleSet.getRule(name);
		BaseElement element = fillElement(component,ruleSet,rule,rootElement);
		return element;
	}

	public boolean support(ComponentInfo component) {
		return component.getClassName().equals(SimpleIconButton.class.getName());
	}
}
