/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public abstract class ControlChangeListener implements Listener {

	public abstract void controlChanged(Control control);

	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.KeyDown:
			if (event.character == SWT.CR)
				controlChanged((Control) event.widget);
			break;
		case SWT.FocusOut:
			controlChanged((Control) event.widget);
			break;
		}
	}

	public void startListeningTo(Control control) {
		control.addListener(SWT.FocusOut, this);
		control.addListener(SWT.KeyDown, this);
	}

	public void stopListeningTo(Control control) {
		control.removeListener(SWT.FocusOut, this);
		control.removeListener(SWT.KeyDown, this);
	}
}
