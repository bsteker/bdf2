package com.bstek.bdf2.jbpm4.designer.converter.impl;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.designer.converter.JpdlInfo;

/**
 * @author Jacky.gao
 * @since 2013-6-18
 */
@Component
public class ForeachConverter extends AbstractConverter{

	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=new BaseElement("foreach");
		String name=this.buildCommonJpdlElement(element, targetElement);
		String collectionVarName=element.attributeValue("collectionVarName");
		String varElementName=element.attributeValue("varElementName");
		targetElement.addAttribute("in", collectionVarName);
		targetElement.addAttribute("var", varElementName);
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		return shapeId.equals("jbpm4.Foreach");
	}
}
