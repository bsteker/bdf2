/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.tabbed.ITypeMapper;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.part.ColumnEditPart;
import com.bstek.bdf.plugins.databasetool.part.ColumnTreeEditPart;
import com.bstek.bdf.plugins.databasetool.part.ConnectionEditPart;
import com.bstek.bdf.plugins.databasetool.part.SchemaEditPart;
import com.bstek.bdf.plugins.databasetool.part.SchemaTreeEditPart;
import com.bstek.bdf.plugins.databasetool.part.TableEditPart;
import com.bstek.bdf.plugins.databasetool.part.TableTreeEditPart;

public class BaseModelLabelProvider extends LabelProvider {
	private ITypeMapper typeMapper;

	public BaseModelLabelProvider() {
		super();
		typeMapper = new BaseModelTypeMapper();
	}

	public Image getImage(Object objects) {
		if (objects == null || objects.equals(StructuredSelection.EMPTY)) {
			return null;
		}
		Object object = ((IStructuredSelection) objects).getFirstElement();
		if (object instanceof TableEditPart || object instanceof TableTreeEditPart) {
			return Activator.getImage(Activator.IMAGE_TABLE_16);
		} else if (object instanceof ColumnEditPart || object instanceof ColumnTreeEditPart) {
			return Activator.getImage(Activator.IMAGE_COLUMN_16);
		} else if (object instanceof SchemaEditPart || object instanceof SchemaTreeEditPart) {
			return Activator.getImage(Activator.IMAGE_SCHEMA_16);
		} else if (object instanceof ConnectionEditPart) {
			return Activator.getImage(Activator.IMAGE_CONNECTION);
		}
		return null;
	}

	public String getText(Object objects) {
		if (objects == null || objects.equals(StructuredSelection.EMPTY)) {
			return "No items selected";
		}
		final boolean multiple[] = { false };
		final Object object = getObject(objects, multiple);
		if (object == null || ((IStructuredSelection) objects).size() > 1) {
			return ((IStructuredSelection) objects).size() + " items selected";
		} else {
			String name = typeMapper.mapType(object).getName();
			return name.substring(name.lastIndexOf('.') + 1);
		}
	}

	@SuppressWarnings("rawtypes")
	private Object getObject(Object objects, boolean multiple[]) {
		Object object = null;
		if (objects instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) objects;
			object = selection.getFirstElement();
			if (selection.size() == 1) {
				multiple[0] = false;
				return object;
			}
			multiple[0] = true;
			Class firstClass = typeMapper.mapType(object);
			if (selection.size() > 1) {
				for (Iterator i = selection.iterator(); i.hasNext();) {
					Object next = i.next();
					Class nextClass = typeMapper.mapType(next);
					if (!nextClass.equals(firstClass)) {
						multiple[0] = false;
						object = null;
						break;
					}
				}
			}
		} else {
			multiple[0] = false;
			object = objects;
		}
		return object;
	}

}