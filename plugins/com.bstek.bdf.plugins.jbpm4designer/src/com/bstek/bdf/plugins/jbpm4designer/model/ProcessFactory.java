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
/**
 * @author Jacky
 */
public class ProcessFactory {
	private static ProcessFactory processFactory;
	private List<AbstractNodeElement> allNodes=new ArrayList<AbstractNodeElement>();
	private ProcessFactory(){
	}
	private void init(){
		allNodes.add(new StartNode("start"));
		allNodes.add(new EndNode("end"));
		allNodes.add(new EndCancelNode("end-cancel"));
		allNodes.add(new EndErrorNode("end-error"));
		allNodes.add(new TaskNode("task"));
		allNodes.add(new StateNode("state"));
		allNodes.add(new DecisionNode("decision"));
		allNodes.add(new JoinNode("join"));
		allNodes.add(new ForkNode("fork"));
		allNodes.add(new ForeachNode("foreach"));
		allNodes.add(new CustomNode("action"));
		allNodes.add(new SubprocessNode("subprocess"));
	}
	public static ProcessFactory getInstance(){
		if(processFactory==null){
			processFactory=new ProcessFactory();
		}
		return processFactory;
	}
	public List<AbstractNodeElement> getAllAvaliableNodes(){
		allNodes.clear();
		this.init();
		return allNodes;
	}
}
