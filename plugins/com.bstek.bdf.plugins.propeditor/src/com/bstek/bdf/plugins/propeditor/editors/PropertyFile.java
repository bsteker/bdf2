/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class PropertyFile extends PropertyEntry {
	private Collection<PropertyElement> propertyElements;

	public PropertyFile(IDocument document) {
		super(null);
		BufferedReader br = new BufferedReader(new StringReader(document.get()));
		int lineNumber = 0;
		propertyElements = new ArrayList<PropertyElement>();
		StringBuilder comment = new StringBuilder();

		int index = 0;
		while (true) {
			try {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				lineNumber++;
				if (line.length() == 0 || line.charAt(0) == '#') {
					comment.append(line).append("\n");
					continue;
				}
				PropertyElement propertyElement = createPropertyElement(line);

				if (propertyElement != null) {
					try {
						propertyElement.setLineOffset(document.getLineOffset(lineNumber - 1));
						propertyElement.setIndex(index++);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					propertyElement.setComment(comment.toString());
					comment = new StringBuilder();
					propertyElements.add(propertyElement);
				} else {
					propertyElements.add(new PropertyElement(this, "", "", comment.toString()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	PropertyElement createPropertyElement(String line) {
		int index = line.indexOf('=');
		if (index != -1) {
			String key = line.substring(0, index).trim();
			String value = line.substring(index + 1).trim();
			return new PropertyElement(this, key, value);
		}
		return null;
	}

	private Set<PropertyFileListener> listeners = new HashSet<PropertyFileListener>();

	public void removePropertyFileListener(PropertyFileListener propertyFileListener) {
		listeners.remove(propertyFileListener);
	}

	public void addPropertyFileListener(PropertyFileListener propertyFileListener) {
		listeners.add(propertyFileListener);
	}

	@Override
	public PropertyElement[] getChildren() {
		return propertyElements.toArray(new PropertyElement[0]);
	}

	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub

	}

	public void keyChanged(PropertyEntry propertyEntry) {
		for (PropertyFileListener listener : listeners) {
			listener.keyChanged(propertyEntry);
		}
	}

	public void valueChanged(PropertyEntry propertyEntry) {
		for (PropertyFileListener listener : listeners) {
			listener.valueChanged(propertyEntry);
		}
	}

	public void entryAdded(PropertyEntry propertyEntry) {
		for (PropertyFileListener listener : listeners) {
			listener.entryAdded(propertyEntry);
		}
	}

	public void entryRemoved(PropertyEntry propertyEntry) {
		for (PropertyFileListener listener : listeners) {
			listener.entryRemoved(propertyEntry);
		}
	}

	public String asText() {
		StringWriter stringWriter = new StringWriter(2000);
		PrintWriter writer = new PrintWriter(stringWriter);
		PropertyElement[] elements = getChildren();
		for (PropertyElement propertyElement : elements) {
			propertyElement.appendText(writer);
		}
		return stringWriter.toString();
	}

	public void addPropertyElement(PropertyElement propertyElement) {
		if (propertyElement.getIndex() == 0) {
			propertyElement.setIndex(propertyElements.size());
		}
		propertyElements.add(propertyElement);
		entryAdded(propertyElement);
	}

	public void removePropertyElement(PropertyElement propertyElement) {
		propertyElements.remove(propertyElement);
		entryRemoved(propertyElement);
	}

}
