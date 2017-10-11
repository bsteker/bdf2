package com.bstek.bdf2.core.config.parser.element;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @since 2013-1-21
 * @author Jacky.gao
 */
public class HibernatePropertiesElementParser implements IElementParser<Properties> {

	public Properties parse(Node rootNode, ParserContext parserContext) {
		NamedNodeMap nodeMap=rootNode.getAttributes();
		String dialect=getAttributeValue("dialect",nodeMap);
		String showSql=getAttributeValue("showSql",nodeMap);
		String hbm2ddlAuto=getAttributeValue("hbm2ddlAuto",nodeMap);
		String formatSql=getAttributeValue("formatSql",nodeMap);
		String maxFetchDepth=getAttributeValue("maxFetchDepth",nodeMap);
		String defaultBatchFetchSize=getAttributeValue("defaultBatchFetchSize",nodeMap);
		String transactionFactoryClass=getAttributeValue("transactionFactoryClass",nodeMap);
		String jtaUserTransaction=getAttributeValue("jtaUserTransaction",nodeMap);
		String transactionManagerLookupClass=getAttributeValue("transactionManagerLookupClass",nodeMap);
		Properties prop=new Properties();
		if(StringUtils.isNotEmpty(dialect)){
			prop.put("hibernate.dialect", dialect);
		}
		if(StringUtils.isNotEmpty(showSql)){
			prop.put("hibernate.show_sql", showSql);
		}
		if(StringUtils.isNotEmpty(hbm2ddlAuto)){
			prop.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
		}
		if(StringUtils.isNotEmpty(formatSql)){
			prop.put("hibernate.format_sql", formatSql);
		}
		if(StringUtils.isNotEmpty(maxFetchDepth)){
			prop.put("hibernate.max_fetch_depth", maxFetchDepth);
		}
		if(StringUtils.isNotEmpty(defaultBatchFetchSize)){
			prop.put("hibernate.default_batch_fetch_size", defaultBatchFetchSize);
		}
		if(StringUtils.isNotEmpty(transactionFactoryClass)){
			prop.put("hibernate.default_batch_fetch_size", transactionFactoryClass);
		}
		if(StringUtils.isNotEmpty(jtaUserTransaction)){
			prop.put("jta.UserTransaction", jtaUserTransaction);
		}
		if(StringUtils.isNotEmpty(transactionManagerLookupClass)){
			prop.put("hibernate.transaction.manager_lookup_class", transactionManagerLookupClass);
		}
		return prop;
	}

	private String getAttributeValue(String attributeName,NamedNodeMap nodeMap){
		Node node=nodeMap.getNamedItem(attributeName);
		if(node!=null){
			return node.getNodeValue();
		}else{
			return null;
		}
	}
	
	public String propertyName() {
		return "hibernateProperties";
	}
}
