/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;
/**
 * @author Jacky
 */
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DecisionNode extends AbstractNodeElement{
	private DecisionType type=DecisionType.handlerClass;
	private String expression;
	private String handlerClass="com.bstek.bdf.jbpm4.decision.DefaultDecisionHandler";
	public DecisionNode(String label) {
		super(label);
	}
	public DecisionType getType() {
		return type;
	}

	public void setType(DecisionType type) {
		this.type = type;
	}

	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getHandlerClass() {
		return handlerClass;
	}
	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}
	@Override
	public Element parseModel(Document doc) {
		Element decision=super.parseModel(doc);
		if(type.equals(DecisionType.expression) && expression!=null){
			decision.setAttribute("expr",expression);	
		}
		if(type.equals(DecisionType.handlerClass) && handlerClass!=null){
			Element handler=doc.createElement("handler");
			handler.setAttribute("class", handlerClass);
			decision.appendChild(handler);
		}
		return decision;
	}
	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
		NamedNodeMap nodeMap=node.getAttributes();
		Node expr=nodeMap.getNamedItem("expr");
		if(expr!=null){
			this.type=DecisionType.expression;
			this.expression=expr.getNodeValue();
		}else{
			NodeList list=node.getChildNodes();
			for(int i=0;i<list.getLength();i++){
				Node cnode=list.item(i);
				if(cnode.getNodeName().equals("handler")){
					this.type=DecisionType.handlerClass;
					NamedNodeMap map=cnode.getAttributes();
					this.handlerClass=map.getNamedItem("class").getNodeValue();
					break;
				}
			}			
		}
	}
	@Override
	public String nodeName() {
		return "decision";
	}
}
