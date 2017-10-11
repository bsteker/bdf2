/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.dialect;

public class ColumnType {

	public static final int GROUP_NUMBER = 1;
	public static final int GROUP_DECIMAL = 2;
	public static final int GROUP_CHARACTOR = 3;
	public static final int GROUP_STRING = 4;
	public static final int GROUP_DATE = 5;
	public static final int GROUP_TIME = 6;
	public static final int GROUP_BINARY_LOB = 7;
	public static final int GROUP_CHARACTOR_LOB = 8;

	private String type;
	private boolean length;
	private boolean decimal;
	private int defaultLength = 0;
	private int defaultDecimal = 0;
	private int group;

	public ColumnType(String type) {
		super();
		this.type = type;
		this.length = false;
		this.decimal = false;
		this.group = ColumnType.GROUP_STRING;
	}

	public ColumnType(String type, boolean length, boolean decimal) {
		super();
		this.type = type;
		this.length = length;
		this.decimal = decimal;
		this.group = ColumnType.GROUP_STRING;
	}

	public ColumnType(String type, boolean length, boolean decimal, int group) {
		super();
		this.type = type;
		this.length = length;
		this.decimal = decimal;
		this.group = group;
	}

	public ColumnType(String type, boolean length, boolean decimal, int defaultLength, int defaultDecimal) {
		super();
		this.type = type;
		this.length = length;
		this.decimal = decimal;
		this.defaultLength = defaultLength;
		this.defaultDecimal = defaultDecimal;
		this.group = ColumnType.GROUP_STRING;
	}

	public ColumnType(String type, boolean length, boolean decimal, int defaultLength, int defaultDecimal, int group) {
		super();
		this.type = type;
		this.length = length;
		this.decimal = decimal;
		this.defaultLength = defaultLength;
		this.defaultDecimal = defaultDecimal;
		this.group = group;
	}

	public String getType() {
		return type;
	}

	public boolean isLength() {
		return length;
	}

	public boolean isDecimal() {
		return decimal;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLength(boolean length) {
		this.length = length;
	}

	public void setDecimal(boolean decimal) {
		this.decimal = decimal;
	}

	public int getDefaultLength() {
		return defaultLength;
	}

	public int getDefaultDecimal() {
		return defaultDecimal;
	}

	public void setDefaultLength(int defaultLength) {
		this.defaultLength = defaultLength;
	}

	public void setDefaultDecimal(int defaultDecimal) {
		this.defaultDecimal = defaultDecimal;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

}
