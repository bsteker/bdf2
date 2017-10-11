/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.domain;

import java.util.Collection;

public class PageInfo {
	private String id;
	private String name;
	private String packageId;
	private String desc;
	private String layout;
	private Collection<LayoutProperty> layoutProperties;
	private Collection<ComponentInfo> components;
	private Collection<ComponentProperty> properties;
	
	public Collection<LayoutProperty> getLayoutProperties() {
		return layoutProperties;
	}
	public void setLayoutProperties(Collection<LayoutProperty> layoutProperties) {
		this.layoutProperties = layoutProperties;
	}

	public Collection<ComponentInfo> getComponents() {
		return components;
	}
	public void setComponents(Collection<ComponentInfo> components) {
		this.components = components;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Collection<ComponentProperty> getProperties() {
		return properties;
	}
	public void setProperties(Collection<ComponentProperty> properties) {
		this.properties = properties;
	}
}
