/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bstek.bdf.plugins.databasetool"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private static Map<String, Image> customImages = new HashMap<String, Image>();

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		disposeCustomImages();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static DbToolGefEditor getEditor() {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof DbToolGefEditor) {
			return (DbToolGefEditor) editor;
		}
		return null;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private void disposeCustomImages() {
		for (Image image : customImages.values()) {
			image.dispose();
		}
		customImages = null;
	}

	public static Image getImage(String path) {
		if (customImages.containsKey(path)) {
			return customImages.get(path);
		} else {
			Image image = Activator.getImageDescriptor(path).createImage();
			customImages.put(path, image);
			return image;
		}
	}

	public static String IMAGE_TABLE = "icons/table.png";
	public static String IMAGE_TABLE_16 = "icons/table16.png";
	public static String IMAGE_TABLE_24 = "icons/table24.png";
	public static String IMAGE_COLUMN_16 = "icons/column16.png";
	public static String IMAGE_COLUMN_24 = "icons/column24.png";
	public static String IMAGE_SCHEMA_16 = "icons/schema16.png";
	public static String IMAGE_CONNECTION = "icons/connection.png";
	public static String IMAGE_CONNECTION_16 = "icons/con16.png";
	public static String IMAGE_CONNECTION_24 = "icons/con24.png";
	public static String IMAGE_PK = "icons/pk.png";
	public static String IMAGE_BLANK = "icons/blank.jpg";
	public static String IMAGE_FK = "icons/fk.png";
	public static String IMAGE_UNIQUE = "icons/unique.png";
	public static String IMAGE_NOTNULL = "icons/notnull.png";
	public static String IMAGE_DDL = "icons/ddl.png";
	public static String IMAGE_1 = "icons/image.png";
	public static String IMAGE_EXPORT = "icons/export.png";
	public static String IMAGE_EDITOR = "icons/editor.png";
	public static String IMAGE_JAVABEAN = "icons/javabean.png";
	public static String IMAGE_GRID = "icons/grid.png";
	public static String IMAGE_SAVE = "icons/save.png";
	public static String IMAGE_EXPORT_DATABASE = "icons/export_database.png";
	public static String IMAGE_IMPORT_DATABASE = "icons/import_database.png";
	public static String IMAGE_DATABASE_CONNECT = "icons/database_connect.png";
	public static String IMAGE_DATABASE_LINK = "icons/database_link.png";
}
