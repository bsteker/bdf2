package com.bstek.bdf2.jbpm4.designer.converter.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.designer.converter.JpdlInfo;

/**
 * @author Jacky.gao
 * @since 2013-6-18
 */
@Component
public class EndConverter extends AbstractConverter{
	private String shapeId;
	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=null;
		if(this.shapeId.equals("jbpm4.End")){
			targetElement=new BaseElement("end");			
		}else if(this.shapeId.equals("jbpm4.ErrorEnd")){
			targetElement=new BaseElement("end-error");			
		}else{
			targetElement=new BaseElement("end-cancel");						
		}
		String name=this.buildCommonJpdlElement(element, targetElement);
		String endType=element.attributeValue("endType");
		if(StringUtils.isNotEmpty(endType)){
			targetElement.addAttribute("ends", endType);
		}
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		this.shapeId=shapeId;
		return shapeId.equals("jbpm4.End") || shapeId.equals("jbpm4.ErrorEnd") || shapeId.equals("jbpm4.CancelEnd");
	}
}
