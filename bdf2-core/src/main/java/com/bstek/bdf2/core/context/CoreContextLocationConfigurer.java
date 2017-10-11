package com.bstek.bdf2.core.context;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.pkgs.PackageConfigurer;

/**
 * @author Jacky.gao
 * @since 2013年5月21日
 */
public class CoreContextLocationConfigurer implements PackageConfigurer {

	public String[] getPropertiesConfigLocations(ResourceLoader resourceLoader) throws Exception {
		return new String[]{"classpath:com/bstek/bdf2/core/config/bdf2.core.properties"};
	}

	public String[] getContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
		String contextCore="classpath:com/bstek/bdf2/core/config/bdf2-core-configs.xml";
		String contextSecurity="classpath:com/bstek/bdf2/core/config/bdf2-security-configs.xml";
		String dataSourceContextLocation=Configure.getString("bdf2.dataSourceContextLocation");
		if(StringUtils.isNotEmpty(dataSourceContextLocation)){
			return new String[]{contextCore,contextSecurity,dataSourceContextLocation};			
		}else{
			return new String[]{contextCore,contextSecurity};						
		}
	}

	public String[] getServletContextConfigLocations(ResourceLoader resourceLoader) throws Exception {
		return new String[]{"classpath:com/bstek/bdf2/core/config/bdf2-core-servlet-context.xml"};
	}
}
