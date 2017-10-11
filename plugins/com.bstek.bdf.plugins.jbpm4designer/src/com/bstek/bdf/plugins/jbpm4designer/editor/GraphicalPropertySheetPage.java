/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
/**
 * @author Jacky
 */
public class GraphicalPropertySheetPage extends TabbedPropertySheetPage {
	private CommandStack commandStack;
	public GraphicalPropertySheetPage(
			ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor,
			CommandStack commandStack) {
		super(tabbedPropertySheetPageContributor);
		this.commandStack = commandStack;
	}

	public CommandStack getCommandStack() {
		return commandStack;
	}
}