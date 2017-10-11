/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.databasetool.Activator;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnType;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialect;
import com.bstek.bdf.plugins.databasetool.dialect.DbDialectManager;
import com.bstek.bdf.plugins.databasetool.figure.base.AbstractFigure;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class ColumnFigure extends AbstractFigure {
	private IFigure dataFigure = new Figure();
	private ImageFigure pkFigure = new ImageFigure();
	private ImageFigure fkFigure = new ImageFigure();
	private ImageFigure notNullFigure = new ImageFigure();
	private ImageFigure uniqueFigure = new ImageFigure();
	private Label firstLabel = new Label();
	private Label lastLabel = new Label() {

	};

	public ColumnFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		layout.setSpacing(2);
		layout.setStretchMinorAxis(false);
		layout.setMinorAlignment(OrderedLayout.ALIGN_TOPLEFT);
		setOpaque(true);
		setBorder(new ColumnFigureBorder());
		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 5;
		gridLayout.horizontalSpacing = 5;
		dataFigure.setLayoutManager(gridLayout);
		dataFigure.add(pkFigure);
		dataFigure.add(fkFigure);
		dataFigure.add(firstLabel);
		dataFigure.add(lastLabel);
		dataFigure.add(notNullFigure);
		dataFigure.add(uniqueFigure);
		add(dataFigure);

	}

	public void setSelected(boolean isSelected) {
		/*
		 * LineBorder lineBorder = (LineBorder) getBorder();
		 * lineBorder.setColor(isSelected ? ColorConstants.gray :
		 * ColorConstants.lightGray); lineBorder.setWidth(isSelected ? 2 : 1);
		 * erase(); repaint();
		 */
	}

	private class ColumnFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(0);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {

			// graphics.drawLine(figure.getBounds().getBottomLeft(),figure.getBounds().getBottomRight());
		}
	}

	public void refreshFigure(BaseModel model) {
		Image imageBlank = Activator.getImage(Activator.IMAGE_BLANK);
		Column column = (Column) model;
		setForegroundColor(column.isPk() ? ColorConstants.red : ColorConstants.black);
		pkFigure.setImage(column.isPk() ? Activator.getImage(Activator.IMAGE_PK) : imageBlank);
		fkFigure.setImage(column.isFk() ? Activator.getImage(Activator.IMAGE_FK) : imageBlank);
		firstLabel.setText(" " + column.getLabel() + "/" + column.getName() + " ");

		String type = column.getType();
		DbDialect dbDialect = DbDialectManager.getDbDialect(column.getTable().getSchema().getCurrentDbType());
		ColumnType columnType = DbDialectManager.getColumnType(dbDialect, type);
		String length = column.getLength();
		String decimal = column.getDecimalLength();
		if (columnType.isLength() && columnType.isDecimal() && length.length() > 0 && decimal.length() > 0) {
			type += "(" + length + "," + decimal + ")";
		} else if (columnType.isLength() && length.length() > 0 && decimal.length() == 0) {
			type += "(" + length + ")";
		}
		lastLabel.setText(type);
		notNullFigure.setImage(column.isNotNull() ? Activator.getImage(Activator.IMAGE_NOTNULL) : imageBlank);
		uniqueFigure.setImage(column.isUnique() ? Activator.getImage(Activator.IMAGE_UNIQUE) : imageBlank);

	}

}
