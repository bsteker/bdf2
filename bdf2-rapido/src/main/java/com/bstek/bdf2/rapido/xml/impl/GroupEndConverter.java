package com.bstek.bdf2.rapido.xml.impl;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.xml.AbstractConverter;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;

public class GroupEndConverter extends AbstractConverter {

	public Element convert(ComponentInfo component, RuleSet ruleSet,
			Element rootElement) throws Exception {
		Rule rule=ruleSet.getRule("GroupEnd");
		BaseElement element = fillElement(component,ruleSet,rule,rootElement);
		return element;
	}

	public boolean support(ComponentInfo component) {
		return component.getClassName().equals("GroupEnd");
	}

}
