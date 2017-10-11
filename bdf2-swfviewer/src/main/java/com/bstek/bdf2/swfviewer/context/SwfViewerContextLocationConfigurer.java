package com.bstek.bdf2.swfviewer.context;

import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.pkgs.PackageConfigurer;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public class SwfViewerContextLocationConfigurer implements PackageConfigurer {

	public String[] getPropertiesConfigLocations(ResourceLoader resourceLoader) throws Exception {
		return new String[]{"classpath:com/bstek/bdf2/swfviewer/config/bdf2.swfviewer.properties"};
	}

	public String[] getContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
		return null;
	}

	public String[] getServletContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
		return null;
	}

}
