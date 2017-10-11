package com.bstek.bdf2.core.config.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.bstek.bdf2.core.security.metadata.AnonymousUrl;

/**
 * @since 2013-1-29
 * @author Jacky.gao
 */
public class AnonymousUrlBeanParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return AnonymousUrl.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext,BeanDefinitionBuilder builder) {
		String urlPattern=element.getAttribute("urlPattern");
		builder.addPropertyValue("urlPattern", urlPattern);
	}

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}

}
