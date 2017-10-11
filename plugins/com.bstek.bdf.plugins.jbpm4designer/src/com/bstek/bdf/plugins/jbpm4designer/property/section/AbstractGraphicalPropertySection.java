/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.bstek.bdf.plugins.jbpm4designer.command.PropertyChangeCommand;
import com.bstek.bdf.plugins.jbpm4designer.editor.GraphicalPropertySheetPage;
/**
 * @author Jacky
 */
public abstract class AbstractGraphicalPropertySection extends AbstractPropertySection {
	protected Object target;
	protected CommandStack commandStack;
	@Override
	public void createControls(Composite parent,TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		if(aTabbedPropertySheetPage instanceof GraphicalPropertySheetPage){
			GraphicalPropertySheetPage page=(GraphicalPropertySheetPage)aTabbedPropertySheetPage;
			commandStack=page.getCommandStack();
		}
	}
	
	protected PropertyChangeCommand createPropertyChangeCommand(String propertyName,Object newValue){
		PropertyChangeCommand command=new PropertyChangeCommand(target,propertyName,newValue);
		return command;
	}
	
	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}

	protected Object getTargetProperty(String propertyName){
		Object value=null;
		try {
			value = PropertyUtils.getProperty(target, propertyName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Object obj=((IStructuredSelection)selection).getFirstElement();
		if(obj instanceof EditPart){
			EditPart editPart=(EditPart)obj;
			target=editPart.getModel();
		}
	}
}
