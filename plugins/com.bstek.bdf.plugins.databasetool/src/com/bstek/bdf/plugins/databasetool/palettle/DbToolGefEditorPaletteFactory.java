/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.palettle;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.requests.SimpleFactory;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class DbToolGefEditorPaletteFactory {
	public static PaletteRoot createPalette() {
		PaletteRoot root = new PaletteRoot();
		root.add(createPaletteGroup());
		root.add(createPaletteDrawer());
		return root;
	}

	private static PaletteGroup createPaletteGroup() {
		PaletteGroup selectionGroup = new PaletteGroup("Selections");
		selectionGroup.add(new SelectionToolEntry());
		return selectionGroup;
	}

	private static PaletteDrawer createPaletteDrawer() {
		PaletteDrawer drawer = new PaletteDrawer("Elements");
		CombinedTemplateCreationEntry table = new CombinedTemplateCreationEntry("Table", "This is table tool", Table.class, new SimpleFactory(
				Table.class), Activator.getImageDescriptor(Activator.IMAGE_TABLE_16), Activator.getImageDescriptor(Activator.IMAGE_TABLE_24));
		drawer.add(table);
		CombinedTemplateCreationEntry column = new CombinedTemplateCreationEntry("Column", "This is column tool", Column.class, new SimpleFactory(
				Column.class), Activator.getImageDescriptor(Activator.IMAGE_COLUMN_16), Activator.getImageDescriptor(Activator.IMAGE_COLUMN_24));
		drawer.add(column);
		ConnectionCreationToolEntry connection = new ConnectionCreationToolEntry("Connection", "this is table connection", new SimpleFactory(
				Connection.class), Activator.getImageDescriptor(Activator.IMAGE_CONNECTION_16),
				Activator.getImageDescriptor(Activator.IMAGE_CONNECTION_24));
		drawer.add(connection);
		return drawer;

	}
}