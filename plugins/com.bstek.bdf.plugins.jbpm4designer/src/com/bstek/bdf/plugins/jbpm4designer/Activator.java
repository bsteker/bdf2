/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.bstek.bdf.plugins.jbpm4designer.preference.NodeImageConfig;
import com.bstek.bdf.plugins.jbpm4designer.preference.Preference;

/**
 * @author Jacky
 */
public class Activator extends AbstractUIPlugin {
	private static Map<String,Image> imageMap=new HashMap<String,Image>();
	// The plug-in ID
	public static final String PLUGIN_ID = "com.bstek.bdf.plugins.jbpm4designer"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private static Preference preference;
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.initPreference();
	}
	
	private void initPreference(){
		preference=new Preference();
		List<NodeImageConfig> configs=new ArrayList<NodeImageConfig>();
		configs.add(this.buildNodeImageConfig("start", Constants.START_NODE_ICON, Constants.START_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("end", Constants.END_NODE_ICON, Constants.END_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("foreach", Constants.FOREACH_NODE_ICON, Constants.FOREACH_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("end-cancel", Constants.ENDCANCEL_NODE_ICON, Constants.ENDCANCEL_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("end-error", Constants.ENDERROR_NODE_ICON, Constants.ENDERROR_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("fork", Constants.FORK_NODE_ICON, Constants.FORK_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("join", Constants.JOIN_NODE_ICON, Constants.JOIN_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("task", Constants.TASK_NODE_ICON, Constants.TASK_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("decision", Constants.DECISION_NODE_ICON, Constants.DECISION_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("sub-process", Constants.SUBPROCESS_NODE_ICON, Constants.SUBPROCESS_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("state", Constants.STATE_NODE_ICON, Constants.STATE_NODE_ICON_SMALL));
		configs.add(this.buildNodeImageConfig("custom", Constants.ACTION_NODE_ICON, Constants.ACTION_NODE_ICON_SMALL));
		preference.setNodeImageConfigs(configs);
		preference.initFromPreferenceStore(getPreferenceStore());
	}
	private NodeImageConfig buildNodeImageConfig(String nodeName,String imagePath,String smallImagePath){
		NodeImageConfig config=new NodeImageConfig();
		config.setNodeName(nodeName);
		config.setDefaultImage(getImageFromPlugin(imagePath));
		config.setDefaultSmallImage(getImageFromPlugin(smallImagePath));
		return config;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		for(Image img:imageMap.values()){
			img.dispose();
		}
		imageMap.clear();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static Preference getPreference(){
		return preference;
	}
	
	public static ImageDescriptor getImageDescriptorFromPlugin(String imageFilePath){
		return imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
	}
	public static Image getImageFromPlugin(String imageFilePath){
		if(imageMap.containsKey(imageFilePath)){
			return imageMap.get(imageFilePath);
		}else{
			Image image=getImageDescriptorFromPlugin(imageFilePath).createImage();
			imageMap.put(imageFilePath, image);
			return image;
		}
	}
	public static Image getImageFromLocal(String imageFilePath){
		if(imageMap.containsKey(imageFilePath)){
			return imageMap.get(imageFilePath);
		}else{
			Image image=new Image(PlatformUI.createDisplay(),imageFilePath);
			imageMap.put(imageFilePath, image);
			return image;
		}
	}
}
