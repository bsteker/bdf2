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
public class DecisionConverter extends AbstractConverter{
	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=new BaseElement("decision");
		String name=this.buildCommonJpdlElement(element, targetElement);
		String decisionType=element.attributeValue("decisionType");
		String decisionValue=element.attributeValue("decisionValue");
		if(decisionType.equals("handler")){
			BaseElement handlerElement=new BaseElement("handler");
			handlerElement.addAttribute("class", decisionValue);
			targetElement.add(handlerElement);
		}else{
			targetElement.addAttribute("expr",decisionValue);
		}
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		return shapeId.equals("jbpm4.Decision");
	}
}
