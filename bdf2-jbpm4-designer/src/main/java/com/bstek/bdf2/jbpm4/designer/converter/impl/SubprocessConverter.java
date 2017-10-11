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
public class SubprocessConverter extends AbstractConverter{

	public JpdlInfo toJpdl(Element element) {
		BaseElement targetElement=new BaseElement("sub-process");
		String name=this.buildCommonJpdlElement(element, targetElement);
		String transitionName=element.attributeValue("transitionName");
		if(StringUtils.isNotEmpty(transitionName)){
			targetElement.addAttribute("outcome", transitionName);
		}
		String subprocessType=element.attributeValue("subprocessType");
		String subprocess=element.attributeValue("subprocess");
		targetElement.addAttribute(subprocessType, subprocess);
		String childToParentProcessVariables=element.attributeValue("childToParentProcessVariables");
		String parentToChildProcessVariables=element.attributeValue("parentToChildProcessVariables");
		if(StringUtils.isNotEmpty(childToParentProcessVariables)){
			for(String pstr:childToParentProcessVariables.split(";")){
				String[] str=pstr.split(">");
				if(str.length<2){
					continue;
				}
				BaseElement p=new BaseElement("parameter-in");
				p.addAttribute("subvar", str[0]);
				p.addAttribute("var", str[1]);
				targetElement.add(p);
			}
		}
		if(StringUtils.isNotEmpty(parentToChildProcessVariables)){
			for(String pstr:parentToChildProcessVariables.split(";")){
				String[] str=pstr.split(">");
				if(str.length<2){
					continue;
				}
				BaseElement p=new BaseElement("parameter-out");
				p.addAttribute("subvar", str[0]);
				p.addAttribute("var", str[1]);
				targetElement.add(p);
			}
		}
		JpdlInfo info=new JpdlInfo();
		info.setName(name);
		info.setElement(targetElement);
		return info;
	}
	public boolean support(String shapeId){
		return shapeId.equals("jbpm4.Subprocess");
	}
}
