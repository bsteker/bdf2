/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.action;

import java.lang.reflect.Constructor;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.bstek.bdf.plugins.jbpm4designer.editor.GraphicalEditorPage;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.part.NodeElementEditPart;
/**
 * @author Jacky
 */
public class PasteAction extends SelectionAction {
	private GraphicalEditorPage graphicalEditorPage;
	public PasteAction(IWorkbenchPart part) {
		super(part);
		this.setId(ActionFactory.PASTE.getId());
		this.graphicalEditorPage=(GraphicalEditorPage)part;
	}

	@Override
	public void run() {
		List<?> list=(List<?>)Clipboard.getDefault().getContents();
		for(Object obj:list){
			if(obj instanceof NodeElementEditPart){
				NodeElementEditPart editPart=(NodeElementEditPart)obj;
				AbstractNodeElement model=editPart.getModel();
				AbstractNodeElement newModel=null;
				try{
					Class<?> clazz=model.getClass();
					Constructor<?> constructor=clazz.getConstructor(new Class[]{String.class});
					newModel=(AbstractNodeElement)constructor.newInstance(model.getLabel());
					newModel.setDescription(model.getDescription());
					newModel.setHeight(model.getHeight());
					newModel.setWidth(model.getWidth());
					newModel.setX(model.getX()+5);
					newModel.setY(model.getY()+5);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				graphicalEditorPage.getProcessDefinition().addNode(newModel);
			}
		}
	}

	@Override
	protected boolean calculateEnabled() {
		if(Clipboard.getDefault().getContents()!=null){
			return true;
		}else{
			return false;			
		}
	}
}
