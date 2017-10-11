/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

public class DbToolGefEditorActionBarContributor extends ActionBarContributor {

	@Override
	protected void buildActions() {
		this.addRetargetAction(new UndoRetargetAction());
		this.addRetargetAction(new RedoRetargetAction());
		this.addRetargetAction(new DeleteRetargetAction());
		this.addRetargetAction(getShowGridRetargetAction());
		this.addRetargetAction(new ZoomInRetargetAction());
		this.addRetargetAction(new ZoomOutRetargetAction());
	}

	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
		toolBarManager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
	}

	private RetargetAction getShowGridRetargetAction() {
		RetargetAction showGridAction = new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, "显示网格", IAction.AS_CHECK_BOX);
		showGridAction.setImageDescriptor(Activator.getImageDescriptor(Activator.IMAGE_GRID));
		return showGridAction;
	}

	@Override
	protected void declareGlobalActionKeys() {

	}
}
