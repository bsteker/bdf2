package com.bstek.bdf2.rapido.xml.impl;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.hibernate.annotations.common.util.StringHelper;

import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.xml.AbstractConverter;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.view.widget.grid.RowNumColumn;

public class RowNumColumnConverter extends AbstractConverter {

	public Element convert(ComponentInfo component, RuleSet ruleSet,
			Element rootElement) throws Exception {
		String name=RowNumColumn.class.getSimpleName();
		XmlNode node=RowNumColumn.class.getAnnotation(XmlNode.class);
		if(node!=null && StringHelper.isNotEmpty(node.nodeName())){
			name=node.nodeName();
		}
		Rule rule=ruleSet.getRule(name);
		BaseElement element = fillElement(component,ruleSet,rule,rootElement);
		return element;
	}

	public boolean support(ComponentInfo component) {
		return RowNumColumn.class.getName().equals(component.getClassName());
	}

}
