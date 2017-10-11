/*
 * This file is part of BDF
 * BDF£¬Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.common.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
/**
 * @author Jacky
 */
public class BbfPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public BbfPreferencePage() {
		super();
		noDefaultAndApplyButton();
	}

	protected Control createContents(Composite parent) {
		return null;
	}
	
	public void init(IWorkbench workbench) {
	}
	

}
