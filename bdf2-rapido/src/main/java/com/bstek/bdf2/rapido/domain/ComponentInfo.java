/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.domain;

import java.util.Collection;

import com.bstek.bdf2.rapido.view.component.def.SupportDef;

public class ComponentInfo {
	private String id;
	private String name;
	private String desc;
	private String className;
	private String entityId;
	private String parentId;
	private String actionDefId;
	private ActionDef actionDef;
	private String layout;
	private boolean container;
	private String packageId;
	private String icon;	
	private int order;
	private boolean readOnly;
	private Collection<ComponentInfo> children;
	private Collection<LayoutProperty> layoutProperties;
	private Collection<LayoutConstraintProperty> layoutConstraintProperties;
	private Collection<ComponentProperty> componentProperties;
	private Collection<ComponentEvent> componentEvents;
	private SupportDef support;
	private Entity entity;
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public Collection<ComponentEvent> getComponentEvents() {
		return componentEvents;
	}
	public void setComponentEvents(Collection<ComponentEvent> componentEvents) {
		this.componentEvents = componentEvents;
	}
	public Collection<ComponentProperty> getComponentProperties() {
		return componentProperties;
	}
	public void setComponentProperties(
			Collection<ComponentProperty> componentProperties) {
		this.componentProperties = componentProperties;
	}
	public Collection<LayoutProperty> getLayoutProperties() {
		return layoutProperties;
	}
	public void setLayoutProperties(Collection<LayoutProperty> layoutProperties) {
		this.layoutProperties = layoutProperties;
	}
	public Collection<ComponentInfo> getChildren() {
		return children;
	}
	public void setChildren(Collection<ComponentInfo> children) {
		this.children = children;
	}
	
	public Collection<LayoutConstraintProperty> getLayoutConstraintProperties() {
		return layoutConstraintProperties;
	}
	public void setLayoutConstraintProperties(
			Collection<LayoutConstraintProperty> layoutConstraintProperties) {
		this.layoutConstraintProperties = layoutConstraintProperties;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public boolean isContainer() {
		return container;
	}
	public void setContainer(boolean container) {
		this.container = container;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	public SupportDef getSupport() {
		return support;
	}
	public void setSupport(SupportDef support) {
		this.support = support;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getActionDefId() {
		return actionDefId;
	}
	public void setActionDefId(String actionDefId) {
		this.actionDefId = actionDefId;
	}
	public ActionDef getActionDef() {
		return actionDef;
	}
	public void setActionDef(ActionDef actionDef) {
		this.actionDef = actionDef;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
