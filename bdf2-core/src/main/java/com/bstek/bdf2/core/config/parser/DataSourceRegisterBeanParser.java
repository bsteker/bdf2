package com.bstek.bdf2.core.config.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.bstek.bdf2.core.orm.DataSourceRegister;


/**
 * @since 2013-1-20
 * @author Jacky.gao
 */
public class DataSourceRegisterBeanParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return DataSourceRegister.class;
	}

	@Override
	protected void doParse(Element element,BeanDefinitionBuilder builder) {
		String name=element.getAttribute("name");
		String asDefault=element.getAttribute("asDefault");
		String dataSource=element.getAttribute("dataSource-ref");
		builder.addPropertyValue("name", name);
		builder.addPropertyValue("asDefault", asDefault);
		builder.addPropertyReference("dataSource", dataSource);
	}

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}
}
