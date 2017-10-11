/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.preference;

import java.io.File;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.RGB;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
/**
 * @author Jacky
 */
public class Preference {
	private String customBorderColorStoreKey="customBorderColor_";
	private String customBackgroundColorStoreKey="customBackgroundColor_";
	private String customTransitionColorStoreKey="customTransitionColor_";
	private String nodeImageKey="_node_image_key_";
	private String nodeSmallImageKey="_node_small_image_key_";
	private String bdfVersionKey="_bdf_version_";
	private int bdfVersion=2;
	private RGB customBorderColor;
	private RGB customBackgroundColor;
	private RGB customTransitionColor;
	private RGB defaultBorderColor=new RGB(102,178,238);
	private RGB defaultTransitionColor=new RGB(102,178,238);
	private RGB defaultBackgroundColor=new RGB(245,245,245);
	private List<NodeImageConfig> nodeImageConfigs;
	public RGB getCustomBorderColor() {
		return customBorderColor;
	}
	public void setCustomBorderColor(RGB customBorderColor) {
		this.customBorderColor = customBorderColor;
	}
	public void setCustomBackgroundColor(RGB customBackgroundColor) {
		this.customBackgroundColor = customBackgroundColor;
	}
	public void setCustomTransitionColor(RGB customTransitionColor) {
		this.customTransitionColor = customTransitionColor;
	}
	public RGB getDefaultBorderColor() {
		return defaultBorderColor;
	}
	public void setDefaultBorderColor(RGB defaultBorderColor) {
		this.defaultBorderColor = defaultBorderColor;
	}
	public RGB getBorderColor(){
		if(customBorderColor==null){
			return this.defaultBorderColor;
		}else{
			return customBorderColor;
		}
	}
	public RGB getBackgroundColor(){
		if(customBackgroundColor==null){
			return this.defaultBackgroundColor;
		}else{
			return customBackgroundColor;
		}
	}
	public int getBdfVersion() {
		return bdfVersion;
	}
	public void setBdfVersion(int bdfVersion) {
		this.bdfVersion = bdfVersion;
	}
	public RGB getTransitionColor(){
		if(customTransitionColor==null){
			return this.defaultTransitionColor;
		}else{
			return customTransitionColor;
		}
	}
	public List<NodeImageConfig> getNodeImageConfigs() {
		return nodeImageConfigs;
	}
	public void setNodeImageConfigs(List<NodeImageConfig> nodeImageConfigs) {
		this.nodeImageConfigs = nodeImageConfigs;
	}
	public void initFromPreferenceStore(IPreferenceStore store){
		int version=store.getInt(bdfVersionKey);
		if(version>0){
			this.bdfVersion=version;
		}
		String customBorderColorStr=store.getString(customBorderColorStoreKey);
		if(customBorderColorStr!=null && customBorderColorStr.split(",").length==3){
			String[] colorStr=customBorderColorStr.split(",");
			this.customBorderColor=new RGB(Integer.valueOf(colorStr[0]),Integer.valueOf(colorStr[1]),Integer.valueOf(colorStr[2]));
		}
		String customBackgroundColorStr=store.getString(customBackgroundColorStoreKey);
		if(customBackgroundColorStr!=null && customBackgroundColorStr.split(",").length==3){
			String[] colorStr=customBackgroundColorStr.split(",");
			this.customBackgroundColor=new RGB(Integer.valueOf(colorStr[0]),Integer.valueOf(colorStr[1]),Integer.valueOf(colorStr[2]));
		}
		String customTransitionColorStr=store.getString(customTransitionColorStoreKey);
		if(customTransitionColorStr!=null && customTransitionColorStr.split(",").length==3){
			String[] colorStr=customTransitionColorStr.split(",");
			this.customTransitionColor=new RGB(Integer.valueOf(colorStr[0]),Integer.valueOf(colorStr[1]),Integer.valueOf(colorStr[2]));
		}
		for(NodeImageConfig config:this.nodeImageConfigs){
			String nodeImage=config.getNodeName()+this.nodeImageKey;
			String nodeImagePath=store.getString(nodeImage);
			if(nodeImagePath!=null && nodeImagePath.length()>0){
				File f=new File(nodeImagePath);
				if(f.exists()){
					config.setCustomImage(Activator.getImageFromLocal(nodeImagePath));
				}
			}
			String smallNodeImage=config.getNodeName()+this.nodeSmallImageKey;
			String smallNodeImagePath=store.getString(smallNodeImage);
			if(smallNodeImagePath!=null && smallNodeImagePath.length()>0){
				File f=new File(smallNodeImagePath);
				if(f.exists()){
					config.setCustomSmallImage(Activator.getImageFromLocal(smallNodeImagePath));
				}
			}
		}
	}
	public void storeToPreference(IPreferenceStore store){
		store.setValue(bdfVersionKey, bdfVersion);
		if(this.customBorderColor!=null){
			String customBorderColorStr=this.customBorderColor.red+","+this.customBorderColor.green+","+this.customBorderColor.blue;
			store.setValue(customBorderColorStoreKey, customBorderColorStr);
		}else{
			if(store.contains(customBorderColorStoreKey)){
				store.setValue(customBorderColorStoreKey, "");
			}
		}
		if(this.customBackgroundColor!=null){
			String customBackgroundColorStr=this.customBackgroundColor.red+","+this.customBackgroundColor.green+","+this.customBackgroundColor.blue;
			store.setValue(customBackgroundColorStoreKey, customBackgroundColorStr);
		}else{
			if(store.contains(customBackgroundColorStoreKey)){
				store.setValue(customBackgroundColorStoreKey, "");
			}
		}
		if(this.customTransitionColor!=null){
			String customTransitionColorStr=this.customTransitionColor.red+","+this.customTransitionColor.green+","+this.customTransitionColor.blue;
			store.setValue(customTransitionColorStoreKey, customTransitionColorStr);
		}else{
			if(store.contains(customTransitionColorStoreKey)){
				store.setValue(customTransitionColorStoreKey, "");
			}
		}
		for(NodeImageConfig config:this.nodeImageConfigs){
			String key=config.getNodeName()+this.nodeImageKey;
			if(config.getCustomImage()!=null && config.getCustomImagePath()!=null){
				store.setValue(key, config.getCustomImagePath());
			}else{
				if(store.contains(key)){
					store.setValue(key, "");
				}
			}
			key=config.getNodeName()+this.nodeSmallImageKey;
			if(config.getCustomSmallImage()!=null && config.getCustomSmallImagePath()!=null){
				store.setValue(key, config.getCustomSmallImagePath());
			}else{
				if(store.contains(key)){
					store.setValue(key, "");
				}
			}
		}
	}
	public NodeImageConfig getNodeImageConfigByName(String name){
		NodeImageConfig targetConfig=null;
		for(NodeImageConfig config:this.nodeImageConfigs){
			if(config.getNodeName().equals(name)){
				targetConfig=config;
				break;
			}
		}
		return targetConfig;
	}
}
