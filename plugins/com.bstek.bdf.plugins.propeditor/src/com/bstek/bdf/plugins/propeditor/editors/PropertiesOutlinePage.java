/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.bstek.bdf.plugins.propeditor.Activator;

public class PropertiesOutlinePage extends ContentOutlinePage {
	protected IDocumentProvider fDocumentProvider;

	private PropertyFile inputPropertyFile;
	private TextEditor editor;
	private PropEditor propEditor;
	private ImageDescriptor imageAz = null;

	PropertiesOutlinePage(PropEditor propEditor, PropertyFile input, TextEditor editor) {
		this.propEditor = propEditor;
		this.inputPropertyFile = propEditor.getInput();
		this.editor = propEditor.getEditor();
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		this.getTreeViewer().setLabelProvider(new PropertiesOutlineLabelProvider());
		this.getTreeViewer().setContentProvider(new PropsOutlineProvider());
		this.getTreeViewer().setInput(inputPropertyFile);

		IToolBarManager toolbar = getSite().getActionBars().getToolBarManager();
		Action action = new SortAction("Sort", 2);
		action.setText(action.getText());
		action.setImageDescriptor(getDescriptor());
		toolbar.add(action);
	}

	private ImageDescriptor getDescriptor() {
		if (imageAz == null) {
			imageAz = Activator.getImageDescriptor("icons/alphab_sort_co.gif");
		}
		return imageAz;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);
		TreeSelection selection = (TreeSelection) event.getSelection();
		PropertyElement propertyElement = (PropertyElement) selection.getFirstElement();

		editor.setHighlightRange(propertyElement.getLineOffset(), 0, true);

		TableViewer tableViewer = propEditor.getTableViewer();
		StructuredSelection ss = new StructuredSelection(tableViewer.getElementAt(propertyElement
				.getIndex()));
		tableViewer.setSelection(ss, true);
	}

	class SortAction extends Action {

		public SortAction(String text, int style) {
			super(text, style);
		}

		public void run() {
			TreeViewer v = getTreeViewer();
			boolean checked = isChecked();
			if (checked) {
				v.setSorter(new ViewerSorter());
				v.setInput(v.getInput());
			} else {
				v.setSorter(null);
				redraw();
			}
		}
	}

	public void redraw() {
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			Control control = viewer.getControl();
			if ((control != null) && (!control.isDisposed())) {
				control.setRedraw(false);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}
}
