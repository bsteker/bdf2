package com.bstek.bdf2.core.config.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.bstek.bdf2.core.orm.AnnotationPackages;

/**
 * @author Jacky.gao
 * @since 2013-2-23
 */
public class AnnotationPackagesBeanParser  extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return AnnotationPackages.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext,BeanDefinitionBuilder builder) {
		String packages=element.getAttribute("packages");
		List<String> list=new ArrayList<String>();
		for(String p:packages.split(",")){
			list.add(p);
		}
		builder.addPropertyValue("scanPackages", list);
		String dataSourceRegisterName=element.getAttribute("dataSourceRegisterName");
		if(StringUtils.isNotEmpty(dataSourceRegisterName)){
			builder.addPropertyValue("dataSourceRegisterName", dataSourceRegisterName);
		}
	}

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}
}

