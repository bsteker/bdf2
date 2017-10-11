/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.domain;

import java.util.Collection;

public class ActionDef {
	private String id;
	private String name;
	private String desc;
	private ActionType type;
	private String script;
	private Collection<Parameter> parameters;
	private boolean async;
	private String confirmMessage;
	private String successMessage;
	private String beforeExecuteScript;
	private String onExecuteScript;
	private String entityId;
	private String packageId;
	private Entity entity;
	private Collection<Action> actions;
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
	public ActionType getType() {
		return type;
	}
	public void setType(ActionType type) {
		this.type = type;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public Collection<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(Collection<Parameter> parameters) {
		this.parameters = parameters;
	}
	public boolean isAsync() {
		return async;
	}
	public void setAsync(boolean async) {
		this.async = async;
	}
	public String getConfirmMessage() {
		return confirmMessage;
	}
	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getBeforeExecuteScript() {
		return beforeExecuteScript;
	}
	public void setBeforeExecuteScript(String beforeExecuteScript) {
		this.beforeExecuteScript = beforeExecuteScript;
	}
	public String getOnExecuteScript() {
		return onExecuteScript;
	}
	public void setOnExecuteScript(String onExecuteScript) {
		this.onExecuteScript = onExecuteScript;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public Collection<Action> getActions() {
		return actions;
	}
	public void setActions(Collection<Action> actions) {
		this.actions = actions;
	}
}
