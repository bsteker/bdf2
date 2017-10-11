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
public class JoinConverter extends AbstractConverter{

	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=new BaseElement("join");
		String name=this.buildCommonJpdlElement(element, targetElement);
		String lockmode=element.attributeValue("lockmode");
		String multiplicity=element.attributeValue("multiplicity");
		if(StringUtils.isNotEmpty(lockmode)){
			targetElement.addAttribute("lockmode", lockmode);
		}
		if(StringUtils.isNotEmpty(multiplicity)){
			targetElement.addAttribute("multiplicity", multiplicity);
		}
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		return shapeId.equals("jbpm4.Join");
	}
}
