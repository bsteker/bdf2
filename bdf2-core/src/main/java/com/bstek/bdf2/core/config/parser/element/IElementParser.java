package com.bstek.bdf2.core.config.parser.element;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Node;

/**
 * @since 2013-1-21
 * @author Jacky.gao
 */
public interface IElementParser<T> {
	T parse(Node node,ParserContext parserContext);
	String propertyName();
}
