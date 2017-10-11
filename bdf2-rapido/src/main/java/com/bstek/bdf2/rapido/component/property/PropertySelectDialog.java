/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.component.property;


public class PropertySelectDialog{
	private String dialogId;
	private String dialogImportUrl;
	
	public void setDialogId(String dialogId) {
		this.dialogId = dialogId;
	}

	public void setDialogImportUrl(String dialogImportUrl) {
		this.dialogImportUrl = dialogImportUrl;
	}

	public String getDialogId() {
		return this.dialogId;
	}

	public String getDialogImportUrl() {
		return this.dialogImportUrl;
	}
}
