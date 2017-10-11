/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.builder.impl;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import com.bstek.bdf2.rapido.builder.IBuilder;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.dorado.idesupport.model.RuleSet;

public class ContextBuilder implements IBuilder {

	public Element build(PageInfo page, RuleSet ruleSet) throws Exception {
		BaseElement element=new BaseElement("Context");
/*		BaseElement attributeElement=new BaseElement("Attribute");
		attributeElement.addAttribute("name", "pageId");
		BaseElement propertyElement=new BaseElement("Property");
		propertyElement.addAttribute("name","value");
		propertyElement.setText(page.getId());
		attributeElement.add(propertyElement);
		element.add(attributeElement);*/
		return element;
	}
}
