/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.bstek.bdf.plugins.propeditor.dialog.property.PropertyContentProvider;
import com.bstek.bdf.plugins.propeditor.dialog.property.PropertyLabelProvider;
import com.bstek.bdf.plugins.propeditor.xml.domain.PropertyCategory;
import com.bstek.bdf.plugins.propeditor.xml.domain.PropertyComment;

public class XmlReader {

	static public void main(String[] arg) throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);

		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new PropertyLabelProvider(),
				new PropertyContentProvider());

		dialog.setInput(loadXmlReader(null));
		dialog.open();
		try {
			loadXmlReader(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Map<String, PropertyCategory> loadXmlReader(String path) throws Exception {
		if (path == null || path.length() == 0) {
			path = "bdf.popertiesLoderController.upload.c.xml";
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder;
		Document doc = null;
		try {
			dbBuilder = dbFactory.newDocumentBuilder();
			doc = dbBuilder.parse(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		Map<String, PropertyCategory> propertyCommentMap = new HashMap<String, PropertyCategory>();
		NodeList categoryList = doc.getElementsByTagName("category");
		for (int i = 0; i < categoryList.getLength(); i++) {
			Element node = (Element) categoryList.item(i);
			String category = getElementTagValue(node, "name");

			NodeList propertyList = node.getElementsByTagName("property");

			for (int j = 0; j < propertyList.getLength(); j++) {
				Element propertyNode = (Element) propertyList.item(j);
				String key = getElementTagValue(propertyNode, "key");
				String value = getElementTagValue(propertyNode, "value");
				String comment = getElementTagValue(propertyNode, "comment");
				getCategory(propertyCommentMap, category).addPropertyComment(
						new PropertyComment(category, key, value, comment));
			}
		}
		System.out.println(propertyCommentMap.size());
		return propertyCommentMap;
	}

	public static PropertyCategory getCategory(Map<String, PropertyCategory> propertyCommentMap, String category) {
		PropertyCategory propertyCategory = propertyCommentMap.get(category);
		if (propertyCategory == null) {
			propertyCategory = new PropertyCategory(category);
			propertyCommentMap.put(category, propertyCategory);
		}
		return propertyCategory;
	}

	public static String getElementTagValue(Element node, String tagname) {
		try {
			return node.getElementsByTagName(tagname).item(0).getFirstChild().getNodeValue();
		} catch (NullPointerException e) {
			return "";
		}
	}

}
