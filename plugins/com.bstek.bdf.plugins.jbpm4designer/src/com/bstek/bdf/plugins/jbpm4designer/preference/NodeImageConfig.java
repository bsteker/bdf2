/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.preference;

import org.eclipse.swt.graphics.Image;
/**
 * @author Jacky
 */
public class NodeImageConfig {
	private String nodeName;
	private Image customImage;
	private String customImagePath;
	private Image defaultImage;
	private Image customSmallImage;
	private String customSmallImagePath;
	private Image defaultSmallImage;
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public Image getCustomImage() {
		return customImage;
	}
	public void setCustomImage(Image customImage) {
		this.customImage = customImage;
	}
	public Image getDefaultImage() {
		return defaultImage;
	}
	public void setDefaultImage(Image defaultImage) {
		this.defaultImage = defaultImage;
	}
	
	public Image getCustomSmallImage() {
		return customSmallImage;
	}
	public void setCustomSmallImage(Image customSmallImage) {
		this.customSmallImage = customSmallImage;
	}
	public Image getDefaultSmallImage() {
		return defaultSmallImage;
	}

	public String getCustomImagePath() {
		return customImagePath;
	}
	public void setCustomImagePath(String customImagePath) {
		this.customImagePath = customImagePath;
	}
	public String getCustomSmallImagePath() {
		return customSmallImagePath;
	}
	public void setCustomSmallImagePath(String customSmallImagePath) {
		this.customSmallImagePath = customSmallImagePath;
	}
	public void setDefaultSmallImage(Image defaultSmallImage) {
		this.defaultSmallImage = defaultSmallImage;
	}
	public Image getImage(){
		if(this.customImage==null){
			return this.defaultImage;
		}else{
			return this.customImage;
		}
	}
	public Image getSmallImage(){
		if(this.customSmallImage==null){
			return this.defaultSmallImage;
		}else{
			return this.customSmallImage;
		}
	}
}
