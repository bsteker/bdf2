package com.bstek.bdf2.jbpm4.designer.converter.impl;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import com.bstek.bdf2.jbpm4.designer.converter.IConverter;

/**
 * @author Jacky.gao
 * @since 2013-6-18
 */
public abstract class AbstractConverter implements IConverter {
	protected String buildCommonJpdlElement(Element element,Element targetElement){
		String name=element.attributeValue("name");
		String desc=element.attributeValue("desc");
		targetElement.addAttribute("name",name);
		Element descElement=new BaseElement("description");
		if(StringUtils.isNotEmpty(desc)){
			descElement.setText(desc);			
		}
		int width=Integer.parseInt(element.attributeValue("width"));
		int height=Integer.parseInt(element.attributeValue("height"));
		int x=Integer.parseInt(element.attributeValue("x"));
		int y=Integer.parseInt(element.attributeValue("y"));
		x=x-(width/2);
		y=y-(height/2);
		String g=x+","+y+","+width+","+height;
		if(this instanceof TransitionConverter){
			g=x+","+y;			
		}
		targetElement.addAttribute("g", g);
		/*for(Object obj:element.attributes()){
			Attribute attr=(Attribute)obj;
			targetElement.addAttribute(attr.getName(),attr.getValue());
		}*/
		return name;
	}
	public Element toGraph(Element element) {
		BaseElement targetElement=new BaseElement("cell");
		this.buildCommonGraphCellElement(element, targetElement);
		return targetElement;
	}
	protected void buildCommonGraphCellElement(Element element,Element targetElement){
		for(Object obj:element.attributes()){
			Attribute attr=(Attribute)obj;
			String name=attr.getName();
			if(name.equals("name") || name.equals("g")){
				continue;
			}
			targetElement.addAttribute(attr.getName(),attr.getValue());
		}
	}
}
