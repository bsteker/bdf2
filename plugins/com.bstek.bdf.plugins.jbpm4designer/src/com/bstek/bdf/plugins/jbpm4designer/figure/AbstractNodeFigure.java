/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.figure;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
/**
 * @author Jacky
 */
public class AbstractNodeFigure extends RoundedRectangle {
	private AbstractNodeElement node;
	private Label label;
	public AbstractNodeFigure(AbstractNodeElement node,Image icon){
		this.node=node;
		BorderLayout layout=new BorderLayout();
		this.setLayoutManager(layout);
		ImageFigure iconFigure=new ImageFigure(icon);
		this.add(iconFigure,BorderLayout.LEFT);
		this.label=new Label(this.node.getLabel());
		this.label.setForegroundColor(ColorConstants.black);
		this.add(this.label,BorderLayout.CENTER);
		this.setAntialias(SWT.ON);
		RGB rgb=Activator.getPreference().getBorderColor();
		this.setForegroundColor(new Color(null,rgb.red,rgb.green,rgb.blue));
		rgb=Activator.getPreference().getBackgroundColor();
		this.setBackgroundColor(new Color(null,rgb.red,rgb.green,rgb.blue));
		this.setLineWidth(2);
	}
	public void setLabel(String label) {
		this.label.setText(label);
	}
}
