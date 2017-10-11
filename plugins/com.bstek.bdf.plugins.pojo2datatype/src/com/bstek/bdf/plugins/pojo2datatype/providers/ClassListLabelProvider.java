package com.bstek.bdf.plugins.pojo2datatype.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.pojo2datatype.model.Node;

/**
 * 
 * @author Jake.Wang@bstek.com
 * @since Dec 18, 2012
 *
 */
public class ClassListLabelProvider  extends LabelProvider{
	public Image getImage(Object element) {
		Node node = (Node) element;
		return node.getIcon();
	}

	public String getText(Object element) {
		Node node = (Node) element;
		return node.getName();
	}
}
