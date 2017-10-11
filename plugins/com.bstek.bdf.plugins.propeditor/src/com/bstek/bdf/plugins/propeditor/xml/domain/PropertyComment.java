/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.xml.domain;

import com.bstek.bdf.plugins.propeditor.util.UnicodeConvert;

public class PropertyComment {
	private String category;
	private String key;
	private String value;
	private String comment;

	public PropertyComment(String category, String key, String value, String comment) {
		setCategory(category);
		setKey(key);
		setValue(value);
		setComment(comment);
	}

	public PropertyComment() {

	}

	public String getCategory() {

		return category;
	}

	public void setCategory(String category) {
		if (category == null) {
			this.category = "未分类";
		} else {
			this.category = UnicodeConvert.decode(category);
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		if (key == null) {
			this.key = "";
		} else {
			this.key = UnicodeConvert.decode(key);
		}
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {
		if (value == null) {
			this.value = "";
		} else {
			this.value = UnicodeConvert.decode(value);
		}
	}

	@Override
	public String toString() {
		return "PropertyComment [category=" + category + ", key=" + key + ", value=" + value
				+ ", comment=" + comment + "]";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		if (comment == null) {
			comment = "";
		}
		this.comment = UnicodeConvert.decode(comment);
	}
}
