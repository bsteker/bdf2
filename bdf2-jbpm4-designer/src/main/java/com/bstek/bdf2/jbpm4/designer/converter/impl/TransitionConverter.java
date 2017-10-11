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
public class TransitionConverter extends AbstractConverter{

	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=new BaseElement("transition");
		String name=this.buildCommonJpdlElement(element, targetElement);
		String fromShapeLabel=element.attributeValue("fromShapeLabel");
		String toShapeLabel=element.attributeValue("toShapeLabel");
		targetElement.addAttribute("to", toShapeLabel);
		targetElement.addAttribute("fromShapeLabel", fromShapeLabel);
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		return shapeId.equals("OG.shape.EdgeShape");
	}
}
