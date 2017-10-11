/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class DbToolPerspective implements IPerspectiveFactory {
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.85f, editorArea);
		
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.3f, editorArea);
		left.addView(JavaUI.ID_PACKAGES);
		left.addView(IPageLayout.ID_PROP_SHEET);

	}
}
