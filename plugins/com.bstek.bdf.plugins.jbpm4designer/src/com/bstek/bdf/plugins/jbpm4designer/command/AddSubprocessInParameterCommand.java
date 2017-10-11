/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessNode;
import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessParameter;

/**
 * @author Jacky
 */
public class AddSubprocessInParameterCommand extends AbstractSubprocessParameterCommand {
	private SubprocessParameter newSubprocessParameter;
	public AddSubprocessInParameterCommand(SubprocessParameter newSubprocessParameter,SubprocessNode node){
		this.node=node;
		this.newSubprocessParameter=newSubprocessParameter;
	}
	@Override
	public void execute() {
		this.node.addInParameter(newSubprocessParameter);
	}
}
