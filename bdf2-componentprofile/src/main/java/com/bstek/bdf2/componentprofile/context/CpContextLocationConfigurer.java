/*
 * @author Bing
 * @since 2013-03-05
 */
package com.bstek.bdf2.componentprofile.context;

import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.pkgs.PackageConfigurer;

/**
 * @author Bing
 *
 */
public class CpContextLocationConfigurer implements PackageConfigurer {

	public String[] getPropertiesConfigLocations(ResourceLoader resourceLoader) throws Exception {
		return new String[] { "classpath:com/bstek/bdf2/componentprofile/config/bdf2.componentprofile.properties" };
	}

	public String[] getContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
		return new String[] { "classpath:com/bstek/bdf2/componentprofile/config/bdf2-componentprofile-configs.xml" };
	}

	public String[] getServletContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
