package com.bstek.bdf.plugins.pojo2datatype.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.bstek.bdf.plugins.pojo2datatype.ModelActivator;

/**
 * Plug-in Model对象
 * @author Jake.Wang@bstek.com
 * @since Dec 18, 2012
 *
 */
public class Node {
	private boolean isPackage;
	private String name;
	private Node parent;
	private Image icon;
	private List<Node> children = new ArrayList<Node>();

	public Image getIcon() {
		return icon;
	}

	public Node(String name, boolean isPack) {
		if (isPack) {
			this.isPackage = true;
			getCreateImage("icons/package_obj.gif");
		} else {
			this.isPackage = false;
			getCreateImage("icons/class_obj.gif");
		}
		this.name = name;
	}

	private void getCreateImage(String path) {
		URL url = ModelActivator.getDefault().getBundle().getResource(path);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(url);
		icon = imageDesc.createImage();
	}

	public Node getParent() {
		return this.parent;
	}

	public void setParent(Node node) {
		this.parent = node;
	}

	public boolean isPackage() {
		return isPackage;
	}

	public String getName() {
		return name;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		if (!child.isPackage) {
			child.setParent(this);
			children.add(child);
		}
	}
	
	public void dispose(){
		icon.dispose();
		for(Node node : children){
			node.dispose();
		}
	}
}
