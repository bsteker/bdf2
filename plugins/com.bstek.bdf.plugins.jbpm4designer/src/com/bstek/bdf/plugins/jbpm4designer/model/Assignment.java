/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

import com.bstek.bdf.plugins.jbpm4designer.Activator;

/**
 * @author Jacky
 */
public class Assignment{
	private String bdf1="com.bstek.bdf.jbpm4.assignment.DefaultAssignmentHandler";
	private String bdf2="com.bstek.bdf2.jbpm4.context.GenericTaskAssignmentHandler";
	private AssignmentType type=AssignmentType.assignmenthandler;
	private String value;
	public Assignment(){
		if(Activator.getPreference().getBdfVersion()==1){
			this.value=bdf1;
		}else if(Activator.getPreference().getBdfVersion()==2){
			this.value=bdf2;			
		}
	}
	public AssignmentType getType() {
		return type;
	}
	public void setType(AssignmentType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
