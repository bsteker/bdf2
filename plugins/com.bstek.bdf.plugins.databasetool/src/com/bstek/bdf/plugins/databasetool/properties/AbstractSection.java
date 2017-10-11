/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;

import com.bstek.bdf.plugins.databasetool.DbToolGefEditor;
import com.bstek.bdf.plugins.databasetool.command.PropertyChangeCommand;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;
import com.bstek.bdf.plugins.databasetool.outline.DbToolGefEditorOutlinePage;

public abstract class AbstractSection extends AbstractPropertySection implements PropertyChangeListener {
	private IPropertySource model;
	public DbToolGefEditor editor;

	public IPropertySource getModel() {
		return model;
	}

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (model != null) {
			BaseModel baseModel = ((BaseModel) model);
			baseModel.removePropertyChangeListener(this);
		}
		if (part instanceof EditorPart) {
			editor = ((DbToolGefEditor) part);
		}
		if (part instanceof ContentOutline) {
			IPage page = ((ContentOutline) part).getCurrentPage();
			if (page instanceof DbToolGefEditorOutlinePage) {
				editor = ((DbToolGefEditorOutlinePage) page).getDbToolGefEditor();
			}
		}
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Object obj = ((EditPart) input).getModel();
		BaseModel baseModel = ((BaseModel) obj);
		baseModel.addPropertyChangeListener(this);
		this.model = baseModel;
	}

	public CommandStack getCommandStack() {
		return editor.getCommandStack();
	}

	public void fireChangPropertyEvent(String propertyId, Object newValue) {
		if (newValue != null) {
			Object oldValue = model.getPropertyValue(propertyId);
			if (!newValue.equals(oldValue)) {
				PropertyChangeCommand command = new PropertyChangeCommand(model, propertyId, newValue, oldValue);
				getCommandStack().execute(command);
			}
		}
		;
	}

	public abstract void updateValues();

	public abstract void sectionChanged(Control control);

	public void propertyChange(PropertyChangeEvent evt) {
		refresh();
	}

	public void refresh() {
		updateValues();
	}

	public ControlChangeListener listener = new ControlChangeListener() {
		public void controlChanged(Control control) {
			sectionChanged(control);
			if (control instanceof Text) {
				Text t = ((Text) control);
				t.setFocus();
				t.setSelection(t.getText().length());
			} else if (control instanceof Combo) {
				Combo combo = (Combo) control;
				combo.setFocus();
			} else {
				control.setFocus();
			}
		}
	};
}
