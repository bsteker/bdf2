/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.propeditor.Activator;

public class PropertiesOutlineLabelProvider extends LabelProvider implements ILabelProvider {

	@Override
	public Image getImage(Object element) {
		return Activator.getImageDescriptor("icons/outlineMarker.gif").createImage();
	}

	@Override
	public String getText(Object element) {
		if (element instanceof PropertyElement) {
			PropertyElement entry = (PropertyElement) element;
			return entry.getKey();
		}
		if (element == null)
			return "<null>";
		return element.toString();
	}

}
