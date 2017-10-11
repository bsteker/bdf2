package com.bstek.bdf2.jbpm4.designer.converter;

import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2013-6-20
 */
public class JpdlInfo {
	private String name;
	private Element element;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
}
