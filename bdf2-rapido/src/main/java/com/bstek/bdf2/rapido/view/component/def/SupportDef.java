/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.component.def;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.property.PropertySupport;

public class SupportDef{
	private String displayName;
	private String fullClassName;
	private String icon;
	private Collection<PropertySupport> propertySupports;
	private String[] childrenName;
	private boolean supportEntity;
	private boolean supportLayout;
	private boolean supportAction;
	private boolean container;
	private boolean alone;
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getFullClassName() {
		return fullClassName;
	}
	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Collection<PropertySupport> getPropertySupports() {
		return propertySupports;
	}
	public void setPropertySupports(Collection<PropertySupport> propertySupports) {
		this.propertySupports = propertySupports;
	}
	public String[] getChildrenName() {
		return childrenName;
	}
	public void setChildrenName(String[] childrenName) {
		this.childrenName = childrenName;
	}
	public boolean isSupportEntity() {
		return supportEntity;
	}
	public void setSupportEntity(boolean supportEntity) {
		this.supportEntity = supportEntity;
	}
	public boolean isSupportLayout() {
		return supportLayout;
	}
	public void setSupportLayout(boolean supportLayout) {
		this.supportLayout = supportLayout;
	}
	public boolean isSupportAction() {
		return supportAction;
	}
	public void setSupportAction(boolean supportAction) {
		this.supportAction = supportAction;
	}
	public boolean isContainer() {
		return container;
	}
	public void setContainer(boolean container) {
		this.container = container;
	}
	public boolean isAlone() {
		return alone;
	}
	public void setAlone(boolean alone) {
		this.alone = alone;
	}
}
