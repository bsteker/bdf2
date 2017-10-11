package com.bstek.bdf2.core.config.parser.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @since 2013-1-21
 * @author Jacky.gao
 */
public class ResourcesElementParser implements IElementParser<Resource[]> {
	public static final String LOCATION_SEPARATOR=",";
	private String propertyName;
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	public ResourcesElementParser(String propertyName){
		this.propertyName=propertyName;
	}
	public Resource[] parse(Node rootNode, ParserContext parserContext) {
		NamedNodeMap nodeMap=rootNode.getAttributes();
		String locations=getAttributeValue("locations",nodeMap);
		if(StringUtils.isNotEmpty(locations)){
			String[] locationsArray=locations.split(LOCATION_SEPARATOR);
			List<Resource> resources=new ArrayList<Resource>();
			for(int i=0;i<locationsArray.length;i++){
				String location=locationsArray[i];
				if(StringUtils.isNotEmpty(location)){
					resources.add(resourcePatternResolver.getResource(location));					
				}
			}
			return resources.toArray(new Resource[resources.size()]);
		}else{
			return null;			
		}
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
		return propertyName;
	}
}
