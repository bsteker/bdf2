/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model.persistence;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class W3cParser {

	public Document getDocument(InputStream stream) throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(stream);
		return document;
	}

	public Document getDocument() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		return document;
	}

	public DocumentBuilder getDocumentBuilder() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder;
	}

	public Element writeElement(Document document, Element root, String key, Object value) {
		Element element = document.createElement(key);
		String data;
		if (value instanceof String) {
			data = (String) value;
		} else if (value instanceof Integer) {
			data = String.valueOf(value);
		} else if (value instanceof Boolean) {
			data = String.valueOf(value);
		} else {
			data = value.toString();
		}
		element.appendChild(document.createTextNode(data.trim()));
		root.appendChild(element);
		return element;
	}

	public String readElement(Element element, String elementName) {
		NodeList childNodes = element.getElementsByTagName(elementName);
		if (childNodes != null && childNodes.getLength() > 0) {
			Element elementBendpoint = (Element) childNodes.item(0);
			return elementBendpoint.getTextContent();
		}
		return "";
	}

	public Element writeAttribute(Element element, String key, Object value) {
		element.setAttribute(key, String.valueOf(value).trim());
		return element;
	}

	public String readAttribute(Element element, String attributeName) {
		return element.getAttribute(attributeName);
	}

	public void saveXml(OutputStream outputStream, Document doc) throws Exception {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		transFactory.setAttribute("indent-number", new Integer(3));
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.0 "); 
		PrintWriter printWriter = new PrintWriter(outputStream);
		transformer.transform(new DOMSource(doc), new StreamResult(printWriter));
	}
}
