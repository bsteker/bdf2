/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * @author Jacky
 */
public class SubprocessNode extends AbstractNodeElement {
	private SubprocessType type;
	private String id;
	private String key;
	private String outcome;
	private List<SubprocessParameter> inParameters=new ArrayList<SubprocessParameter>();
	private List<SubprocessParameter> outParameters=new ArrayList<SubprocessParameter>();
	public SubprocessNode(String label) {
		super(label);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public SubprocessType getType() {
		return type;
	}
	public void setType(SubprocessType type) {
		this.type = type;
	}
	public List<SubprocessParameter> getInParameters() {
		return inParameters;
	}
	public void addInParameter(SubprocessParameter inParameter) {
		this.inParameters.add(inParameter);
	}
	public void removeInParameter(SubprocessParameter inParameter) {
		this.inParameters.remove(inParameter);
	}
	public List<SubprocessParameter> getOutParameters() {
		return outParameters;
	}
	public void addOutParameter(SubprocessParameter outParameter) {
		this.outParameters.add(outParameter);
	}
	public void removeOutParameter(SubprocessParameter outParameter) {
		this.outParameters.remove(outParameter);
	}
	@Override
	public Element parseModel(Document doc) {
		Element subprocess=super.parseModel(doc);
		if(type!=null){
			if(type.equals(SubprocessType.id)){
				subprocess.setAttribute("sub-process-id", this.getId());	
			}
			if(type.equals(SubprocessType.key)){
				subprocess.setAttribute("sub-process-key", this.getKey());	
			}
		}
		if(outcome!=null){
			subprocess.setAttribute("outcome", this.getOutcome());	
		}
		if(!this.inParameters.isEmpty()){
			this.buildParameters(inParameters, doc, subprocess, "parameter-in");
		}
		if(!this.outParameters.isEmpty()){
			this.buildParameters(outParameters, doc, subprocess, "parameter-out");
		}
		return subprocess;
	}
	
	private void buildParameters(List<SubprocessParameter> params,Document doc,Element subprocess,String nodeName){
		for(SubprocessParameter p:params){
			Element paramElement=doc.createElement(nodeName);
			paramElement.setAttribute("subvar", p.getSubvar());
			paramElement.setAttribute("var", p.getVar());
			subprocess.appendChild(paramElement);
		}
	}
	
	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
		NamedNodeMap map=node.getAttributes();
		Node idNode=map.getNamedItem("sub-process-id");
		if(idNode!=null){
			this.id=idNode.getNodeValue();
			this.type=SubprocessType.id;
		}
		Node keyNode=map.getNamedItem("sub-process-key");
		if(keyNode!=null){
			this.key=keyNode.getNodeValue();
			this.type=SubprocessType.key;
		}
		Node outcomeNode=map.getNamedItem("outcome");
		if(outcomeNode!=null){
			this.outcome=outcomeNode.getNodeValue();
		}
		NodeList list=node.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node cnode=list.item(i);
			if("parameter-in".equals(cnode.getNodeName())){
				NamedNodeMap nodeMap=cnode.getAttributes();
				SubprocessParameter p=new SubprocessParameter();
				Node n=nodeMap.getNamedItem("var");
				if(n!=null){
					p.setVar(n.getNodeValue());
				}
				n=nodeMap.getNamedItem("subvar");
				if(n!=null){
					p.setSubvar(n.getNodeValue());
				}
				this.inParameters.add(p);
			}
			if("parameter-out".equals(cnode.getNodeName())){
				NamedNodeMap nodeMap=cnode.getAttributes();
				SubprocessParameter p=new SubprocessParameter();
				Node n=nodeMap.getNamedItem("var");
				if(n!=null){
					p.setVar(n.getNodeValue());
				}
				n=nodeMap.getNamedItem("subvar");
				if(n!=null){
					p.setSubvar(n.getNodeValue());
				}
				this.outParameters.add(p);
			}
		}
	}

	@Override
	public String nodeName() {
		return "sub-process";
	}
}
