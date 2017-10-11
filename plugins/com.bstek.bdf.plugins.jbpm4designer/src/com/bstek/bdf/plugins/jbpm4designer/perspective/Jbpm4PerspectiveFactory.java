/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.perspective;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
/**
 * @author Jacky
 */
public class Jbpm4PerspectiveFactory implements IPerspectiveFactory {
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.8f, layout.getEditorArea());
		layout.addView(JavaUI.ID_PACKAGES, IPageLayout.LEFT, 0.2f, layout.getEditorArea());
		layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM, 0.7f, layout.getEditorArea());
	}
}
