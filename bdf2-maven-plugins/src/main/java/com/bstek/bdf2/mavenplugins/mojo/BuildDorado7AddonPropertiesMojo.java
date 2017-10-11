package com.bstek.bdf2.mavenplugins.mojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
/**
 * @author Jacky.gao
 * @since 2013-7-18
 * @goal addonPropBuilder
 * @phase initialize
 */
public class BuildDorado7AddonPropertiesMojo extends AbstractMojo{
	/**
	 * @parameter expression="${addon.name}"
	 */
	private String name;
	
	/**
	 * @parameter expression="${project.version}"
	 */
	private String version;
	
	/**
	 * @parameter expression="${project.addonVersion}" default-value="2.0"
	 */
	private String addonVersion;
	
	/**
	 * @parameter expression="${addon.depends}" default-value="dorado-core"
	 */
	private String depends;
	
	/**
	 * @parameter expression="${addon.license}" default-value="Inherited"
	 */
	private String license;
	
	/**
	 * @parameter expression="${addon.loadUnlicensed}"  default-value="false"
	 */
	private String loadUnlicensed;
	
	/**
	 * @parameter expression="${addon.classifier}"  default-value="dorado7 addon"
	 */
	private String classifier;
	
	/**
	 * @parameter expression="${addon.homePage}" default-value="http://www.bstek.com"
	 */
	private String homePage;
	
	/**
	 * @parameter expression="${addon.description}" default-value="a standard dorado7 addon"
	 */
	private String description;
	
	/**
	 * @parameter expression="${addon.configurer}" default-value=""
	 */
	private String configurer;
	
	/**
	 * @parameter expression="${addon.contextConfigLocations}" default-value=""
	 */
	private String contextConfigLocations;
	
	/**
	 * @parameter expression="${addon.componentConfigLocations}" default-value=""
	 */
	private String componentConfigLocations;
	
	/**
	 * @parameter expression="${addon.servletContextConfigLocations}" default-value=""
	 */
	private String servletContextConfigLocations;
	
	
	/**
	 * @parameter expression="${addon.propertiesConfigLocations}" default-value=""
	 */
	private String propertiesConfigLocations;
	
	/**
	 * @parameter expression="${addon.propertiesRelativeLocation}" default-value="/src/main/resources/META-INF/dorado-package.properties"
	 */
	private String propertiesFileRelativeLocation;
	
	/**
	 * @parameter expression="${basedir}"
	 */
	private String targetDir;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		if(name==null || name.equals("")){
			return;
		}
		Properties prop=new Properties();
		prop.put("name",name);
		prop.put("version",version);
		prop.put("depends",depends);
		prop.put("license",license);
		prop.put("classifier",classifier);
		prop.put("loadUnlicensed",loadUnlicensed);
		prop.put("classifier",homePage);
		prop.put("description",description);
		if(configurer!=null && !configurer.equals("")){
			prop.put("configurer",configurer);			
		}
		if(addonVersion!=null && !addonVersion.equals("")){
			prop.put("addonVersion",addonVersion);			
		}
		if(contextConfigLocations!=null && !contextConfigLocations.equals("")){
			prop.put("contextConfigLocations",contextConfigLocations);			
		}
		if(componentConfigLocations!=null && !componentConfigLocations.equals("")){
			prop.put("componentConfigLocations",componentConfigLocations);			
		}
		if(servletContextConfigLocations!=null && !servletContextConfigLocations.equals("")){
			prop.put("servletContextConfigLocations",servletContextConfigLocations);			
		}
		if(propertiesConfigLocations!=null && !propertiesConfigLocations.equals("")){
			prop.put("propertiesConfigLocations",propertiesConfigLocations);			
		}
		String path=targetDir+propertiesFileRelativeLocation;
		File f=new File(path);
		FileOutputStream fout=null;
		try{
			fout=new FileOutputStream(f);
			prop.store(fout,null);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new MojoExecutionException(ex.getMessage());
		}finally{
			if(fout!=null)
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
