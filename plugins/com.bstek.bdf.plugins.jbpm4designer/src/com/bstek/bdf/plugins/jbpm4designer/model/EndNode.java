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

public class EndNode extends AbstractNodeElement {
	private EndType ends=EndType.execution;
	public EndNode(String label) {
		super(label);
	}

	@Override
	public Element parseModel(Document doc){
		Element end=super.parseModel(doc);
		if(this.ends!=null){
			if(this.ends.equals(EndType.execution)){
				end.setAttribute("ends", EndType.execution.toString());	
			}
			if(this.ends.equals(EndType.processInstance)){
				end.setAttribute("ends", EndType.processInstance.toString());	
			}
		}
		return end;
	}
	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
		NamedNodeMap map=node.getAttributes();
		Node ends=map.getNamedItem("ends");
		if(ends==null){
			return;
		}
		String endsValue=ends.getNodeValue();
		if(endsValue.equals(EndType.execution.toString())){
			this.ends=EndType.execution;
		}
		if(endsValue.equals(EndType.processInstance.toString())){
			this.ends=EndType.processInstance;
		}
	}

	public EndType getEnds() {
		return ends;
	}

	public void setEnds(EndType ends) {
		this.ends = ends;
	}

	@Override
	public String nodeName() {
		return "end";
	}
}
