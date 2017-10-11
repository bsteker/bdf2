/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.ActionFactory;

import com.bstek.bdf.plugins.databasetool.action.ActionIdConstants;

public class DbToolGefEditorContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	public DbToolGefEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		if (registry == null) {
			throw new IllegalArgumentException();
		}
		actionRegistry = registry;
	}

	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);

		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.UNDO.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionFactory.DELETE.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ActionIdConstants.MODIFY_ACTION_ID));

		IAction saveAction = getAction(ActionFactory.SAVE.getId());
		saveAction.setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_SAVE));

		menu.appendToGroup(GEFActionConstants.GROUP_SAVE, saveAction);
		menu.appendToGroup(GEFActionConstants.MB_ADDITIONS, createExportMenuManager());
		menu.appendToGroup(GEFActionConstants.MB_ADDITIONS, getAction(ActionIdConstants.IMPORT_DATABASE_ACTION_ID));
		menu.appendToGroup(GEFActionConstants.MB_ADDITIONS, getAction(ActionIdConstants.CHANGE_DATABASE_ACTION_ID));

	}

	private MenuManager createExportMenuManager() {
		MenuManager exportMenuManager = new MenuManager("&Export", Activator.getImageDescriptor(Activator.IMAGE_EXPORT), null);
		exportMenuManager.add(getAction(ActionIdConstants.EXPORT_IMAGE_ACTION_ID));
		exportMenuManager.add(getAction(ActionIdConstants.EXPORT_DDL_ACTION_ID));
		exportMenuManager.add(getAction(ActionIdConstants.EXPORT_JAVABEAN_ACTION_ID));
		exportMenuManager.add(getAction(ActionIdConstants.EXPORT_DATABASE_ACTION_ID));
		return exportMenuManager;
	}


	private IAction getAction(String actionId) {
		return actionRegistry.getAction(actionId);
	}

}
