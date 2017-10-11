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

import com.bstek.bdf.plugins.common.StringHelper;
/**
 * @author Jacky
 */
public class JoinNode extends AbstractNodeElement {
	private LockModeType lockmode;
	private String multiplicity;
	public JoinNode(String label) {
		super(label);
	}
	public String getMultiplicity() {
		return multiplicity;
	}
	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}
	public LockModeType getLockmode() {
		return lockmode;
	}

	public void setLockmode(LockModeType lockmode) {
		this.lockmode = lockmode;
	}
	@Override
	public Element parseModel(Document doc) {
		Element join=super.parseModel(doc);
		if(this.lockmode!=null){
			join.setAttribute("lockmode", lockmode.toString());	
		}
		if(StringHelper.isNotEmpty(multiplicity)){
			join.setAttribute("multiplicity", multiplicity);	
		}
		return join;
	}
	@Override
	public void parseXml(Node node) {
		super.parseXml(node);
		NamedNodeMap map=node.getAttributes();
		Node lockmodeNode=map.getNamedItem("lockmode");
		if(lockmodeNode!=null){
			LockModeType type=null;
			for(LockModeType mode:LockModeType.values()){
				if(mode.toString().equals(lockmodeNode.getNodeValue())){
					type=mode;
				}
			}
			this.lockmode=type;
		}
		Node multiplicityNode=map.getNamedItem("multiplicity");
		if(multiplicityNode!=null){
			this.multiplicity=multiplicityNode.getNodeValue();
		}
	}
	@Override
	public String nodeName() {
		return "join";
	}
}
