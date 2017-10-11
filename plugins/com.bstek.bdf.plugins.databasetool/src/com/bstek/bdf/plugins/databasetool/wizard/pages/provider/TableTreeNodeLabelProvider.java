package com.bstek.bdf.plugins.databasetool.wizard.pages.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.databasetool.Activator;

public class TableTreeNodeLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		TableTreeNode node = (TableTreeNode) element;
		if (node.isTable) {
			return Activator.getImage(Activator.IMAGE_TABLE);
		} else {
			return Activator.getImage(Activator.IMAGE_DATABASE_CONNECT);
		}
	}

	public String getText(Object element) {
		TableTreeNode node = (TableTreeNode) element;
		if (!node.isTable) {
			return node.name + " <共" + node.getChildren().length + "张表>";
		}
		return node.name;
	}
}
