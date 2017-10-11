/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.Event;
/**
 * @author Jacky
 */
public abstract class AbstractEventChangeCommand extends Command {
	protected AbstractNodeElement node;
	protected Event newEvent;
	public AbstractEventChangeCommand(AbstractNodeElement node,Event event){
		this.node=node;
		this.newEvent=event;
	}
}
