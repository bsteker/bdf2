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
public class TaskConverter extends AbstractConverter{

	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=new BaseElement("task");
		String name=this.buildCommonJpdlElement(element, targetElement);
		String assignmentType=element.attributeValue("assignmentType");
		String assignmentValue=element.attributeValue("assignmentValue");
		if(assignmentType.equals("assignment-handler")){
			BaseElement handlerElement=new BaseElement("assignment-handler");
			handlerElement.addAttribute("class", assignmentValue);
			targetElement.add(handlerElement);
		}else{
			targetElement.addAttribute(assignmentType, assignmentValue);
		}
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		return shapeId.equals("jbpm4.Task");
	}
}
