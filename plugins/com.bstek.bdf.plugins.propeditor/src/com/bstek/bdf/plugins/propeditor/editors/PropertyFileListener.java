/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.propeditor.editors;

public interface PropertyFileListener {
	void keyChanged(PropertyEntry element);

	void valueChanged(PropertyEntry element);

	void entryAdded(PropertyEntry element);

	void entryRemoved(PropertyEntry element);
}
