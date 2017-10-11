/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.dialog.property;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.propeditor.xml.domain.PropertyCategory;
import com.bstek.bdf.plugins.propeditor.xml.domain.PropertyComment;

public class PropertyLabelProvider extends LabelProvider implements ITableLabelProvider {

	public String getText(Object element) {
		if (element == null) {
			return "";
		} else if (element instanceof PropertyCategory) {
			return ((PropertyCategory) element).getName();
		} else if (element instanceof PropertyComment) {
			return ((PropertyComment) element).getKey();
		} else {
			return element.toString();
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof PropertyCategory) {
			PropertyCategory propertyCategory = (PropertyCategory) element;
			switch (columnIndex) {
			case 0:
				return propertyCategory.getName();
			case 1:
				return "";
			}
		}

		if (element instanceof PropertyComment) {
			PropertyComment propertyCategory = (PropertyComment) element;
			switch (columnIndex) {
			case 0:
				return propertyCategory.getKey();
			case 1:
				return propertyCategory.getValue();
			}
		}
		return null;
	}
}
