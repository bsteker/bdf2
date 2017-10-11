/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/**
 * @author Jacky
 */
public class ForeachNode extends AbstractNodeElement {
	private String variable;
	private String collection;
	public ForeachNode(String label) {
		super(label);
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	@Override
	public Element parseModel(Document doc) {
		Element foreach=super.parseModel(doc);
		if(this.collection!=null){
			foreach.setAttribute("in", collection);	
		}
		if(this.variable!=null){
			foreach.setAttribute("var", variable);	
		}
		return foreach;
	}
	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
		NamedNodeMap map=node.getAttributes();
		Node var=map.getNamedItem("var");
		if(var!=null){
			this.setVariable(var.getNodeValue());
		}
		Node in=map.getNamedItem("in");
		if(in!=null){
			this.setCollection(in.getNodeValue());
		}
	}

	@Override
	public String nodeName() {
		return "foreach";
	}
}
