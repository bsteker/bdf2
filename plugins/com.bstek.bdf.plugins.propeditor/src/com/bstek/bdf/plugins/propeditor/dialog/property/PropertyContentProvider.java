/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.dialog.property;

import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bstek.bdf.plugins.propeditor.xml.domain.PropertyCategory;

public class PropertyContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof PropertyCategory) {
			return ((PropertyCategory) parentElement).getPropertyComments().toArray();
		} else if (parentElement instanceof Map) {
			return ((Map) parentElement).values().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof PropertyCategory) {
			return true;
		} else {
			return false;
		}
	}

}
