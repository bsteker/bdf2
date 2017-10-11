package com.bstek.bdf2.jbpm4.designer.converter;

import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2013-6-18
 */
public interface IConverter {
	JpdlInfo toJpdl(Element element);
	Element toGraph(Element element);
	boolean support(String shapeId);
}
