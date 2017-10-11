/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section.table;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessParameter;
/**
 * @author Jacky
 */
public class ParameterTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		SubprocessParameter parameter=(SubprocessParameter)element;
		switch(columnIndex){
		case 0:
			return parameter.getVar();
		case 1:
			return parameter.getSubvar();
		}
		return null;
	}

}
