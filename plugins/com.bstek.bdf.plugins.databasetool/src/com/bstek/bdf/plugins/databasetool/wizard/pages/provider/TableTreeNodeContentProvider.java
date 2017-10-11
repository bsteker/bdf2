package com.bstek.bdf.plugins.databasetool.wizard.pages.provider;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class TableTreeNodeContentProvider extends ArrayContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		TableTreeNode node = (TableTreeNode) parentElement;
		return node.children;
	}

	public Object getParent(Object element) {
		TableTreeNode node = (TableTreeNode) element;
		return node.parent;
	}

	public boolean hasChildren(Object element) {
		TableTreeNode node = (TableTreeNode) element;
		return node.children.length > 0;
	}
}
