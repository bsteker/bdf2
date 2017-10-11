/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.figure.base.AbstractFigure;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class TableFigure extends AbstractFigure {

	private ColumnFigure columnFigure = new ColumnFigure();
	private Label label = new Label();

	public TableFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		LineBorder lineBorder = new LineBorder(ColorConstants.lightGray, 2);
		setBorder(lineBorder);
		setOpaque(true);
		setBackgroundColor(ColorConstants.white);

		Font font = new Font(null, "宋体", 11, SWT.NORMAL);
		label.setFont(font);
		label.setBorder(new LineBorder(ColorConstants.lightGray));
		label.setIcon(Activator.getImage(Activator.IMAGE_TABLE_16));
		label.setOpaque(true);
		label.setBackgroundColor(ColorConstants.lightBlue);
		label.setForegroundColor(ColorConstants.black);
		add(label);
		add(columnFigure);
	}

	public void setSelected(boolean isSelected) {
		LineBorder lineBorder = (LineBorder) getBorder();
		lineBorder.setColor(isSelected ? ColorConstants.gray : ColorConstants.lightGray);
		lineBorder.setWidth(isSelected ? 3 : 2);
		erase();
		repaint();
	}

	public ColumnFigure getColumnFigure() {
		return columnFigure;
	}

	public void refreshFigure(BaseModel model) {
		Table t = (Table) model;
		label.setText(t.getTitle());
	}
}
