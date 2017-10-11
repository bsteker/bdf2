/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.section.table;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessParameter;
/**
 * @author Jacky
 */
public class ParameterCellModifier implements ICellModifier {
	private TableViewer viewer;
	public ParameterCellModifier(TableViewer viewer){
		this.viewer=viewer;
	}
	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		SubprocessParameter p=(SubprocessParameter)element;
		if(property.equals("var")){
			return p.getVar()!=null?p.getVar():"";
		}
		if(property.equals("subvar")){
			return p.getSubvar()!=null?p.getSubvar():"";
		}
		return "";
	}

	@Override
	public void modify(Object element, String property, Object value) {
		SubprocessParameter p=(SubprocessParameter)((TableItem)element).getData();
		if(property.equals("var")){
			p.setVar((String)value);
		}
		if(property.equals("subvar")){
			p.setSubvar((String)value);
		}
		viewer.refresh();
	}
}
