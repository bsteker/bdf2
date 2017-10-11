/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PropsOutlineProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	protected void parse(IDocument document) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((PropertyFile) inputElement).getChildren();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		PropertyFile parent = (PropertyFile) parentElement;
		return parent.getChildren();
	}

	@Override
	public Object getParent(Object propertyFile) {
		return ((PropertyFile) propertyFile).getParent();
	}

	@Override
	public boolean hasChildren(Object propertyFile) {
		PropertyEntry[] entries = ((PropertyEntry) propertyFile).getChildren();
		if (entries != null && entries.length > 0) {
			return true;
		} else {
			return false;
		}
	}
}
