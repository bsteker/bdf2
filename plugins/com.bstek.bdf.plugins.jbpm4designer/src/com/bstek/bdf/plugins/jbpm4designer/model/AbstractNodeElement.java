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

import org.eclipse.draw2d.geometry.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bstek.bdf.plugins.common.StringHelper;

/**
 * @author Jacky
 */
public abstract class AbstractNodeElement extends AbstractElement implements IModelParser{
	public static final String SOURCE_TRANSITION_DROP="SOURCE_TRANSITION_DROP";
	public static final String TARGET_TRANSITION_DROP="TARGET_TRANSITION_DROP";
	public static final String TRANSITION_REMOVE="TRANSITION_REMOVE";
	public static final String LABEL_CHANGED="LABEL_CHANGED";
	private List<Transition> outTransitions=new ArrayList<Transition>();
	private List<Transition> inTransitions=new ArrayList<Transition>();
	private String label;
	private String description;
	private List<Event> events=new ArrayList<Event>();
	public AbstractNodeElement(String label){
		this.label=label;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
		this.firePropertyChange(LABEL_CHANGED, null, label);
	}
	public List<Transition> getOutTransitions() {
		return new ArrayList<Transition>(outTransitions);
	}
	public void addTransition(Transition transition) {
		boolean isadd=false;
		if(transition.getSource()==this){
			this.outTransitions.add(transition);
			this.firePropertyChange(SOURCE_TRANSITION_DROP, null, transition);
			isadd=true;
		}
		if(transition.getTarget()==this){
			this.inTransitions.add(transition);
			this.firePropertyChange(TARGET_TRANSITION_DROP, null, transition);	
			isadd=true;
		}
		if(this.label.equals(transition.getSourceNodeName()) && !isadd){
			this.outTransitions.add(transition);
			isadd=true;
		}
		if(this.label.equals(transition.getTargetNodeName()) && !isadd){
			this.inTransitions.add(transition);
		}
	}
	public List<Transition> getInTransitions() {
		return new ArrayList<Transition>(inTransitions);
	}
	
	public void removeTransition(Transition transition){
		if(transition.getSource()==this){
			this.outTransitions.remove(transition);
		}
		if(transition.getTarget()==this){
			this.inTransitions.remove(transition);
		}
		this.firePropertyChange(TRANSITION_REMOVE, null, null);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void addEvent(Event event) {
		this.events.add(event);
	}
	public void removeEvent(Event event) {
		this.events.remove(event);
	}

	@Override
	public void parseXml(Node node) {
		NamedNodeMap nodeMap=node.getAttributes();
		for(int i=0;i<nodeMap.getLength();i++){
			Node  cnode=nodeMap.item(i);
			String nodeName=cnode.getNodeName();
			if(nodeName.equals("name")){
				this.setLabel(cnode.getNodeValue());
			}
			if(nodeName.equals("g")){
				String[] g=cnode.getNodeValue().split(",");
				this.setX(Integer.parseInt(g[0]));
				this.setY(Integer.parseInt(g[1]));
				this.setWidth(Integer.parseInt(g[2]));
				this.setHeight(Integer.parseInt(g[3]));
			}
		}
		NodeList list=node.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node cnode=list.item(i);
			if("transition".equals(cnode.getNodeName())){
				this.parseTransition(cnode);
			}
			if("on".equals(cnode.getNodeName())){
				this.parseEvents(cnode);
			}
			if("description".equals(cnode.getNodeName())){
				this.description=cnode.getTextContent();
				
			}
		}
	}
	
	private void parseEvents(Node node){
		NamedNodeMap map=node.getAttributes();
		if(map.getNamedItem("event")!=null){
			Event event=new Event();
			this.addEvent(event);
			event.setType(EventType.valueOf(map.getNamedItem("event").getNodeValue()));
			NodeList list=node.getChildNodes();
			for(int i=0;i<list.getLength();i++){
				Node cnode=list.item(i);
				if("event-listener".equals(cnode.getNodeName())){
					event.setListenerClass(cnode.getAttributes().getNamedItem("class").getNodeValue());
					break;
				}
			}
		}
	}
	
	private void parseTransition(Node node){
		NamedNodeMap nodeMap=node.getAttributes();
		if(nodeMap==null)return;
		Transition transition=new Transition();
		TransitionLabel transitionLabel=new TransitionLabel("");
		transition.setTransitionLabel(transitionLabel);
		if(nodeMap.getNamedItem("name")!=null){
			transitionLabel.setText(nodeMap.getNamedItem("name").getNodeValue());			
		}
		if(nodeMap.getNamedItem("g")!=null){
			String g=nodeMap.getNamedItem("g").getNodeValue();
			int colonIndex=g.indexOf(":");
			if(colonIndex!=-1){
				String bendpointStr=g.substring(0,colonIndex);
				for(String bendpoint:bendpointStr.split(";")){
					String[] point=bendpoint.split(",");
					transition.addBendpoint(new Point(Integer.valueOf(point[0]),Integer.valueOf(point[1])));
				}
				String[] labelPoint=g.substring(colonIndex+1,g.length()).split(",");
				transitionLabel.setOffset(new Point(Integer.parseInt(labelPoint[0]),Integer.parseInt(labelPoint[1])));
			}else{
				String[] labelPoint=g.split(",");
				transitionLabel.setOffset(new Point(Integer.parseInt(labelPoint[0]),Integer.parseInt(labelPoint[1])));
			}
		}
		if(nodeMap.getNamedItem("to")!=null){
			transition.setTargetNodeName(nodeMap.getNamedItem("to").getNodeValue());			
		}
		transition.setSourceNodeName(this.label);
		this.outTransitions.add(transition);
	}
	@Override
	public Element parseModel(Document doc) {
		Element node=doc.createElement(this.nodeName());
		if(StringHelper.isNotEmpty(label)){
			node.setAttribute("name", label);
		}
		if(StringHelper.isNotEmpty(description)){
			Element descElement=doc.createElement("description");
			descElement.setTextContent(description);
			node.appendChild(descElement);
		}
		String g=this.getX()+","+this.getY()+","+this.getWidth()+","+this.getHeight();
		node.setAttribute("g",g);
		this.buildTransitions(doc, node);
		this.buildEvents(doc, node);
		return node;
	}
	
	private void buildEvents(Document doc,Element parentElement){
		for(Event event:this.events){
			Element eventElement=doc.createElement("on");
			parentElement.appendChild(eventElement);
			eventElement.setAttribute("event", event.getType().toString());
			if(StringHelper.isNotEmpty(event.getListenerClass())){
				Element classElement=doc.createElement("event-listener");
				eventElement.appendChild(classElement);
				classElement.setAttribute("class", event.getListenerClass());
			}
		}
	}
	
	private void buildTransitions(Document doc,Element parentElement){
		for(Transition trans:this.outTransitions){
			Element transitionElement=doc.createElement("transition");
			parentElement.appendChild(transitionElement);
			TransitionLabel transitionLabel=trans.getTransitionLabel();
			if(transitionLabel!=null){
				String transitionLabelText=transitionLabel.getText();
				if(StringHelper.isNotEmpty(transitionLabelText)){
					transitionElement.setAttribute("name", transitionLabel.getText());					
				}
				String bendPoints=null;
				int i=0;
				for(Point bendpoint:trans.getBendpoints()){
					if(i==0){
						bendPoints=bendpoint.x+","+bendpoint.y;
					}else{
						bendPoints+=";";
						bendPoints+=bendpoint.x+","+bendpoint.y;
					}
					i++;
				}
				if(transitionLabel.getOffset()!=null){
					String g=transitionLabel.getOffset().x+","+transitionLabel.getOffset().y;
					if(bendPoints!=null){
						transitionElement.setAttribute("g", bendPoints+":"+g);						
					}else{
						transitionElement.setAttribute("g", g);						
					}
				}
			}
			if(trans.getTarget()!=null){
				transitionElement.setAttribute("to",trans.getTarget().getLabel());										
			}else{
				transitionElement.setAttribute("to",trans.getTargetNodeName());					
			}
		}
	}
	
	@Override
	public boolean support(Node node) {
		boolean support=false;
		if(node!=null && this.nodeName().equals(node.getNodeName())){
			support=true;
		}
		return support;
	}
	public abstract String nodeName();
}
