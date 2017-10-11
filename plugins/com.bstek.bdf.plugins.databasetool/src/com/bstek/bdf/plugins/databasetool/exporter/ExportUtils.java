/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class ExportUtils {
	public static final String ENCODE_NAME = "UTF-8";
	public static final String JAVA_EXTENSION = ".java";

	public static void exportImage(GraphicalViewer viewer, String savePath, int format) {
		int i = 5;
		LayerManager layerManager = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
		IFigure contentLayer = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
		Image img = null;
		GC gc = null;
		Graphics graphics = null;
		try {
			img = new Image(Display.getDefault(), contentLayer.getSize().width + i, contentLayer.getSize().height + i);
			gc = new GC(img);
			graphics = new SWTGraphics(gc);
			graphics.translate(contentLayer.getBounds().getLocation());
			contentLayer.paint(graphics);
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { img.getImageData() };
			imageLoader.save(savePath, format);
		} finally {
			if (graphics != null) {
				graphics.dispose();
			}
			if (gc != null) {
				gc.dispose();
			}
			if (img != null) {
				img.dispose();
			}
		}
	}

	public static void exportDDL(Schema schema, String savePath) throws Exception {
		DbDialect dbDialect = DbDialectManager.getDbDialect(schema.getCurrentDbType());
		String sql = dbDialect.generateDDLSQL(schema);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(savePath));
			fos.write(sql.getBytes(ExportUtils.ENCODE_NAME));
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static List<JavaBeanProperty> exportJavaBeans(String packageName, boolean doradoPropertyDef, boolean hibernateAnotation, Schema shema)
			throws Exception {
		List<JavaBeanProperty> list = new ArrayList<JavaBeanProperty>();
		JavaBeanProperty bean = null;
		ExportSchemaToJavaBeanBuilder builder = ExportSchemaToJavaBeanBuilder.getInstance();
		builder.setDoradoPropertyDef(doradoPropertyDef);
		builder.setHibernateAnotation(hibernateAnotation);
		for (Table table : shema.getTables()) {
			bean = builder.createJavaBeanProperty(packageName, table);
			list.add(bean);
		}
		return list;
	}
}
