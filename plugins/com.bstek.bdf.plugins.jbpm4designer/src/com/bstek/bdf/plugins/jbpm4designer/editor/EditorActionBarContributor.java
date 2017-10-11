/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
/**
 * @author Jacky
 */
public class EditorActionBarContributor extends
		MultiPageEditorActionBarContributor {
	
	private Map<String, RetargetAction> actionMap = new HashMap<String, RetargetAction>();

	private static final String[] WORKBENCH_ACTION_IDS = {
			ActionFactory.PRINT.getId(), ActionFactory.DELETE.getId(),
			ActionFactory.SAVE.getId(), ActionFactory.UNDO.getId(),
			ActionFactory.REDO.getId(), ActionFactory.CUT.getId(),
			ActionFactory.COPY.getId(), ActionFactory.PASTE.getId(),
			ActionFactory.SELECT_ALL.getId(), ActionFactory.FIND.getId(),
			IDEActionFactory.BOOKMARK.getId() };
	private static final String[] TEXTEDITOR_ACTION_IDS = {
			ITextEditorActionConstants.PRINT,
			ITextEditorActionConstants.DELETE, ITextEditorActionConstants.SAVE,
			ITextEditorActionConstants.UNDO, ITextEditorActionConstants.REDO,
			ITextEditorActionConstants.CUT, ITextEditorActionConstants.COPY,
			ITextEditorActionConstants.PASTE,
			ITextEditorActionConstants.SELECT_ALL,
			ITextEditorActionConstants.FIND, IDEActionFactory.BOOKMARK.getId() };
	@Override
	public void init(IActionBars bars) {
		buildActions(bars);
		contributeGraphicalEditorToolBarAction(bars.getToolBarManager());
		super.init(bars);
	}
	private void buildActions(IActionBars actionBars) {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		
		RetargetAction showGridAction=new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, "Grid");
		showGridAction.setImageDescriptor(Activator.getImageDescriptorFromPlugin("icons/grid.png"));
		addRetargetAction(showGridAction);
	}
	
	@Override
 	public void setActivePage(IEditorPart activeEditor) {
		IActionBars actionBars = getActionBars();
		if (actionBars == null) return;
		actionBars.clearGlobalActionHandlers();
		if (activeEditor instanceof GraphicalEditorPage) {
			addGraphicalEditorActions((GraphicalEditorPage) activeEditor, actionBars);
		} else if (activeEditor instanceof StructuredTextEditor) {
			addXmlEditorActions((StructuredTextEditor) activeEditor, actionBars);
		}
		actionBars.updateActionBars();
	}
	@Override
	public void dispose() {
		String[] keys = actionMap.keySet().toArray(new String[actionMap.keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			RetargetAction action = (RetargetAction) actionMap.get(keys[i]);
			getPage().removePartListener(action);
			action.dispose();
		}
		actionMap.clear();
		actionMap = null;
	}
	
	private void addGraphicalEditorActions(GraphicalEditorPage activeEditor,
			IActionBars actionBars) {
		// get the registry of actions from jpdl editor
		ActionRegistry registry = (ActionRegistry) activeEditor.getAdapter(ActionRegistry.class);
		
		// set jpdl global action to workbench global action
		for (int i = 0; i < WORKBENCH_ACTION_IDS.length; i++) {
			actionBars.setGlobalActionHandler(WORKBENCH_ACTION_IDS[i], registry
					.getAction(WORKBENCH_ACTION_IDS[i]));
		}
		
		// set the retarget action of jpdl editor to jpdl golbal action
		String[] keys = actionMap.keySet().toArray(new String[actionMap.keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			actionBars.setGlobalActionHandler(keys[i], registry.getAction(keys[i]));
		}
		actionBars.getToolBarManager().update(true);
	}
	
	private void addXmlEditorActions(StructuredTextEditor activeEditor,
			IActionBars actionBars) {	
		// set the global actions of jpdl editor are unable.
		String[] keys = actionMap.keySet().toArray(new String[actionMap.keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			actionBars.setGlobalActionHandler(keys[i], null);
		}
		//set the global actions to xml editor global action
		for (int i = 0; i < WORKBENCH_ACTION_IDS.length; i++) {
			actionBars.setGlobalActionHandler(WORKBENCH_ACTION_IDS[i],activeEditor.getAction(TEXTEDITOR_ACTION_IDS[i]));
		}
	}
	
	private void addRetargetAction(RetargetAction action) {
		actionMap.put(action.getId(), action);
		getPage().addPartListener(action);
	}
	
	private void contributeGraphicalEditorToolBarAction(IToolBarManager toolBarManager) {
		toolBarManager.add(actionMap.get(ActionFactory.UNDO.getId()));
		toolBarManager.add(actionMap.get(ActionFactory.REDO.getId()));
		toolBarManager.add(actionMap.get(ActionFactory.DELETE.getId()));
		toolBarManager.add(actionMap.get(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
	}
}
