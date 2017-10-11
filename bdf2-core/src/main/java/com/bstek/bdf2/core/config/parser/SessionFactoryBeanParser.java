package com.bstek.bdf2.core.config.parser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bstek.bdf2.core.config.parser.element.Elements;
import com.bstek.bdf2.core.config.parser.element.HibernatePropertiesElementParser;
import com.bstek.bdf2.core.config.parser.element.IElementParser;
import com.bstek.bdf2.core.config.parser.element.ResourcesElementParser;
import com.bstek.bdf2.core.orm.hibernate.HibernateSessionFactory;
import com.bstek.bdf2.core.orm.hibernate.UnByteCodeProxyInterceptor;

/**
 * @since 2013-1-20
 * @author Jacky.gao
 */
public class SessionFactoryBeanParser extends AbstractSingleBeanDefinitionParser {
	private boolean hasId=true;
	private Map<String,IElementParser<?>> parserMap=new HashMap<String,IElementParser<?>>();
	public SessionFactoryBeanParser(){
		parserMap.put(Elements.MAPPING_DIRECTORY_LOCATIONS, new ResourcesElementParser("mappingDirectoryLocations"));
		parserMap.put(Elements.MAPPING_JAR_LOCATIONS, new ResourcesElementParser("mappingJarLocations"));
		parserMap.put(Elements.MAPPING_LOCATIONS, new ResourcesElementParser("mappingLocations"));
		parserMap.put(Elements.MAPPING_RESOURCES, new ResourcesElementParser("mappingResources"));
		parserMap.put(Elements.HIBERNATE_PROPERTIES, new HibernatePropertiesElementParser());
	}
	@Override
	protected Class<?> getBeanClass(Element element) {
		return HibernateSessionFactory.class;
	}

	@Override
	protected boolean shouldGenerateId() {
		return hasId;
	}

	@Override
	protected void doParse(Element element,ParserContext parserContext,BeanDefinitionBuilder builder) {
		String id=element.getAttribute("id");
		if(StringUtils.isNotEmpty(id))hasId=false;
		String dataSourceRegisterName=element.getAttribute("dataSourceRegisterName");
		builder.addPropertyValue("dataSourceName", dataSourceRegisterName);
		builder.addPropertyReference("entityInterceptor", UnByteCodeProxyInterceptor.BEAN_ID);
		NodeList nodeList=element.getChildNodes();
		for(int i=0;i<nodeList.getLength();i++){
			Node node=nodeList.item(i);
			String nodeName=parserContext.getDelegate().getLocalName(node);
			IElementParser<?> parser=parserMap.get(nodeName);
			if(parser!=null){
				builder.addPropertyValue(parser.propertyName(), parser.parse(node, parserContext));
			}
		}
		String scanPackages=element.getAttribute("scanPackages");
		if(StringUtils.isNotEmpty(scanPackages)){
			builder.addPropertyValue("scanPackages", scanPackages.split(","));			
		}
	}
	
	
}
