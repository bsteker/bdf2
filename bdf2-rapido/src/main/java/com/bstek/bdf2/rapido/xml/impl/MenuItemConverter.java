package com.bstek.bdf2.rapido.xml.impl;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.hibernate.annotations.common.util.StringHelper;

import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.xml.AbstractConverter;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.view.widget.base.menu.MenuItem;

public class MenuItemConverter extends AbstractConverter {

	public Element convert(ComponentInfo component, RuleSet ruleSet,
			Element rootElement) throws Exception {
		String name=MenuItem.class.getSimpleName();
		XmlNode node=MenuItem.class.getAnnotation(XmlNode.class);
		if(node!=null && StringHelper.isNotEmpty(node.nodeName())){
			name=node.nodeName();
		}
		Rule rule=ruleSet.getRule(name);
		BaseElement element = fillElement(component,ruleSet,rule,rootElement);
		if(component.getChildren()!=null){
			for(ComponentInfo c:component.getChildren()){
				this.buildChildren(element,c,ruleSet,rootElement);							
			}
		}
		return element;
	}

	public boolean support(ComponentInfo component) {
		return component.getClassName().equals(MenuItem.class.getName());
	}

}
