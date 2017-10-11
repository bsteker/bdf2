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
public class CustomConverter extends AbstractConverter{

	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=new BaseElement("custom");
		String name=this.buildCommonJpdlElement(element, targetElement);
		String customClass=element.attributeValue("customClass");
		targetElement.addAttribute("class", customClass);
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		return shapeId.equals("jbpm4.Custom");
	}
}
