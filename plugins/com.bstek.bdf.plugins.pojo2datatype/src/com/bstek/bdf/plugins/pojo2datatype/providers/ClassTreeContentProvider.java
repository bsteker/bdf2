package com.bstek.bdf.plugins.pojo2datatype.providers;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.bstek.bdf.plugins.pojo2datatype.model.Node;

/**
 * 
 * @author Jake.Wang@bstek.com
 * @since Dec 18, 2012
 *com.bstek.bdf.plugins.pojo2datatype
 */
public class ClassTreeContentProvider extends ArrayContentProvider implements
		ITreeContentProvider {
	public Object[] getChildren(Object parentElement) {
		Node node = (Node) parentElement;
		return node.getChildren().toArray();
	}

	public Object getParent(Object element) {
		Node node = (Node) element;
		return node.getParent();
	}

	public boolean hasChildren(Object element) {
		Node node = (Node) element;
		return node.getChildren().size() > 0;
	}
}
