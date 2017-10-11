﻿/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.Event;
/**
 * @author Jacky
 */
public class RemoveEventCommand extends AbstractEventChangeCommand {

	public RemoveEventCommand(AbstractNodeElement node, Event event) {
		super(node, event);
	}
	@Override
	public void execute() {
		node.removeEvent(newEvent);
	}
}