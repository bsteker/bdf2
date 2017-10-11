/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bstek.bdf.plugins.common.StringHelper;
/**
 * @author Jacky
 */
public class ProcessDefinition implements IModelParser{
	private String name;
	private String key;
	private String version;
	private String description;
	private List<AbstractNodeElement> nodes=new ArrayList<AbstractNodeElement>();
	private PropertyChangeSupport listeners=new PropertyChangeSupport(this);
	public ProcessDefinition(){
	}

	public List<AbstractNodeElement> getNodes() {
		return nodes;
	}
	public void addNode(AbstractNodeElement node) {
		this.nodes.add(node);
		this.firePropertyChange(AbstractNodeElement.NODE_ADD, null, node);
	}
	public void removeNode(AbstractNodeElement node) {
		List<Transition> inTrans=node.getInTransitions();
		List<Transition> outTrans=node.getOutTransitions();
		this.nodes.remove(node);
		for(Transition trans:inTrans){
			trans.disconnection();
		}
		for(Transition trans:outTrans){
			trans.disconnection();
		}
		this.firePropertyChange(AbstractNodeElement.NODE_REMOVE, null, node);
	}
	protected void firePropertyChange(String eventName,Object oldValue,Object newValue){
		this.listeners.firePropertyChange(eventName, oldValue, newValue);
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
		this.listeners.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		this.listeners.removePropertyChangeListener(listener);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Element parseModel(Document doc){
		Element process=doc.createElement("process");
		if(StringHelper.isNotEmpty(name)){
			process.setAttribute("name", name);
		}
		process.setAttribute("xmlns", "http://jbpm.org/4.4/jpdl");
		if(StringHelper.isNotEmpty(key)){
			process.setAttribute("key", key);
		}
		if(StringHelper.isNotEmpty(version)){
			process.setAttribute("version", version);
		}
		if(StringHelper.isNotEmpty(description)){
			Element desc=doc.createElement("description");
			desc.setTextContent(description);
			process.appendChild(desc);
		}
		return process;
	}

	@Override
	public void parseXml(Node node) {
		NamedNodeMap nodeMap=node.getAttributes();
		for(int i=0;i<nodeMap.getLength();i++){
			Node  cnode=nodeMap.item(i);
			String nodeName=cnode.getNodeName();
			if(nodeName.equals("name")){
				this.name=cnode.getNodeValue();
			}
			if(nodeName.equals("key")){
				this.key=cnode.getNodeValue();
			}
			if(nodeName.equals("version")){
				this.version=cnode.getNodeValue();
			}
		}
		NodeList list=node.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node cnode=list.item(i);
			if("description".equals(cnode.getNodeName())){
				this.description=cnode.getTextContent();
				
			}
		}
	}

	@Override
	public boolean support(Node node) {
		boolean support=false;
		if("process".equals(node.getNodeName())){
			support=true;
		}
		return support;
	}

	public void buildInTransitions(){
		for(AbstractNodeElement node:this.nodes){
			for(Transition trans:node.getOutTransitions()){
				String targetNodeName=trans.getTargetNodeName();
				if(StringHelper.isNotEmpty(targetNodeName)){
					AbstractNodeElement targetNode=this.findNodeByName(targetNodeName);
					if(targetNode!=null){
						targetNode.addTransition(trans);
					}
				}
			}
		}
	}
	private AbstractNodeElement findNodeByName(String nodeName){
		AbstractNodeElement resultNode=null;
		for(AbstractNodeElement node:this.nodes){
			if(node.getLabel().equals(nodeName)){
				resultNode=node;
				break;
			}
		}
		return resultNode;
	}
}
