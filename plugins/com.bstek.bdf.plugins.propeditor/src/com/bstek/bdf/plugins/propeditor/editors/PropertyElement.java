/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

import java.io.PrintWriter;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bstek.bdf.plugins.propeditor.util.UnicodeConvert;

public class PropertyElement extends PropertyEntry implements IAdaptable {
	String key;
	String value;
	int lineOffset;
	String comment;
	int index;

	public PropertyElement(PropertyEntry propertyEntry, String key, String value) {
		super(propertyEntry);
		this.key = key;
		this.value = value;
	}

	public PropertyElement(PropertyEntry propertyEntry, String key, String value, String comment) {
		super(propertyEntry);
		this.key = key;
		this.value = value;
		this.comment = comment;
	}

	public void keyChanged(PropertyElement element) {

	}

	public void valueChanged(PropertyElement element) {

	}

	public void removed(PropertyElement element) {

	}

	public String getKey() {
		return UnicodeConvert.decode(key);
	}

	public void setKey(String text) {
		if (key.equals(text)) {
			return;
		}
		this.key = UnicodeConvert.encode(text);
		((PropertyFile) this.getParent()).keyChanged(this);
	}

	public String getValue() {
		return UnicodeConvert.decode(value);
	}

	public void setValue(String text) {
		if (value.equals(text)) {
			return;
		}
		this.value = UnicodeConvert.encode(text);
		((PropertyFile) this.getParent()).valueChanged(this);
	}

	@Override
	public PropertyElement[] getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub

	}

	public void appendText(PrintWriter writer) {
		if (comment != null) {
			writer.print(comment);
		}
		if (key != null && key.length() > 0) {
			writer.print(key);
			writer.print(" = ");
			writer.println(value);
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getLineOffset() {
		return lineOffset;
	}

	public void setLineOffset(int lineOffset) {
		this.lineOffset = lineOffset;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private PropPropertySource propPropertySource;

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IPropertySource.class)) {
			if (propPropertySource == null) {
				propPropertySource = new PropPropertySource(this);
			}
			return propPropertySource;
		}
		return null;
	}
}
