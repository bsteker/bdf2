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

public class TaskNode extends AbstractNodeElement {
	private Assignment assignment=new Assignment();
	public TaskNode(String label) {
		super(label);
	}
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	@Override
	public Element parseModel(Document doc) {
		Element task=super.parseModel(doc);
		if(this.getLabel()!=null){
			task.setAttribute("name", this.getLabel());	
		}
		if(assignment!=null){
			if(assignment.getType().equals(AssignmentType.assignee)){
				task.setAttribute("assignee", assignment.getValue());	
			}
			if(assignment.getType().equals(AssignmentType.assignmenthandler)){
				Element assignmentHandler=doc.createElement("assignment-handler");
				assignmentHandler.setAttribute("class", assignment.getValue());
				task.appendChild(assignmentHandler);
			}
			if(assignment.getType().equals(AssignmentType.candidategroups)){
				task.setAttribute("candidate-groups", assignment.getValue());	
			}
			if(assignment.getType().equals(AssignmentType.candidateusers)){
				task.setAttribute("candidate-users", assignment.getValue());	
			}
			if(assignment.getType().equals(AssignmentType.swimlane)){
				task.setAttribute("swimlane", assignment.getValue());	
			}
		}
		return task;
	}
	
	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
		NamedNodeMap nodeMap= node.getAttributes();
		for(int i=0;i<nodeMap.getLength();i++){
			Node cnode=nodeMap.item(i);
			if(cnode.getNodeName().equals("assignee")){
				if(this.assignment==null){
					this.assignment=new Assignment();
				}
				this.assignment.setType(AssignmentType.assignee);
				this.assignment.setValue(cnode.getNodeValue());
			}
			if(cnode.getNodeName().equals("candidate-groups")){
				if(this.assignment==null){
					this.assignment=new Assignment();
				}
				this.assignment.setType(AssignmentType.candidategroups);
				this.assignment.setValue(cnode.getNodeValue());
			}
			if(cnode.getNodeName().equals("candidate-users")){
				if(this.assignment==null){
					this.assignment=new Assignment();
				}
				this.assignment.setType(AssignmentType.candidateusers);
				this.assignment.setValue(cnode.getNodeValue());
			}
			if(cnode.getNodeName().equals("swimlane")){
				if(this.assignment==null){
					this.assignment=new Assignment();
				}
				this.assignment.setType(AssignmentType.swimlane);
				this.assignment.setValue(cnode.getNodeValue());
			}
		}
		NodeList nodeList=node.getChildNodes();
		Node assignmentHandlerNode=null;
		for(int i=0;i<nodeList.getLength();i++){
			Node cnode=nodeList.item(i);
			if(cnode.getNodeName().equals("assignment-handler")){
				assignmentHandlerNode=cnode;
				break;
			}
		}
		if(assignmentHandlerNode!=null){
			if(this.assignment==null){
				this.assignment=new Assignment();
			}
			this.assignment.setType(AssignmentType.assignmenthandler);
			if(assignmentHandlerNode.getAttributes().getLength()>0){
				this.assignment.setValue(assignmentHandlerNode.getAttributes().item(0).getNodeValue());				
			}
		}
	}
	@Override
	public String nodeName() {
		return "task";
	}
}
