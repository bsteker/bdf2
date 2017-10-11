package com.bstek.bdf.plugins.jbpm4designer.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.bstek.bdf.plugins.common.StringHelper;

/**
 * @author Jacky
 *
 */
public class CustomNode extends AbstractNodeElement {
	private String clazz;
	public CustomNode(String label) {
		super(label);
	}

	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
		NamedNodeMap nodeMap=node.getAttributes();
		Node clazzNode=nodeMap.getNamedItem("class");
		if(clazzNode!=null){
			this.clazz=clazzNode.getNodeValue();
		}
	}

	@Override
	public Element parseModel(Document doc) {
		Element decision=super.parseModel(doc);
		if(StringHelper.isNotEmpty(clazz)){
			decision.setAttribute("class",clazz);	
		}
		return decision;
	}	
	
	@Override
	public String nodeName() {
		return "custom";
	}
}

