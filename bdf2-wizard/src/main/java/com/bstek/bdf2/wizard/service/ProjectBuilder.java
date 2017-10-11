package com.bstek.bdf2.wizard.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.bstek.bdf2.wizard.model.AddonInfo;
import com.bstek.bdf2.wizard.model.Dependence;
import com.bstek.bdf2.wizard.model.Wizard;
import com.bstek.bdf2.wizard.utils.VelocityUtils;
import com.bstek.bdf2.wizard.utils.WizardFileUtils;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Service(ProjectBuilder.BEAN_ID)
public class ProjectBuilder implements ServletContextAware, InitializingBean {
	public static final String BEAN_ID = "bdf2.wizard.projectBuilder";

	private ServletContext servletContext;
	private String mavenProjectVmFile;
	private String dynamicWebProjectVmFile;
	private String wstCommonComponentVmFile;
	private String mavenPomVmFile;
	private String mavenTemplatePath;
	private String dynamicWebTemplatePath;
	private String pomBuildVmFile;
	private String doradoWebXmlPath;

	@Resource(name = DependenceComputer.BEAN_ID)
	private DependenceComputer dependenceComputer;
	
	@Resource(name = MavenCompiler.BEAN_ID)
	private MavenCompiler mavenCompiler;
	
	private SAXReader saxReader = new SAXReader();

	public String execute(Wizard wizard) throws Exception {
		String projectName = wizard.getProjectName();
		String wizardType = wizard.getWizardType();
		String fileVmid = WizardFileUtils.generateFileVMID();
		File tempFile = new File(WizardFileUtils.getTempFilePath() + fileVmid);
		if (!tempFile.exists()) {
			tempFile.mkdir();
		}
		String rootPath = tempFile.getAbsolutePath();
		WizardFileUtils.copyDirectiory(mavenTemplatePath, rootPath + File.separator + projectName);
		VelocityContext context = new VelocityContext();
		context.put("projectName", projectName);
		String destProjectFilePath = rootPath + File.separator + projectName + File.separator + ".project";
		VelocityUtils.velocityEvaluate(context, mavenProjectVmFile, destProjectFilePath);
		List<Dependence> dependencies = dependenceComputer.execute(wizard);
		context.put("dependencies", dependencies);
		context.put("groupId", wizard.getGroupId());
		context.put("version", wizard.getVersion());
		context.put("artifactId", wizard.getArtifactId());
		String pomPath = rootPath + File.separator + projectName + File.separator + "pom.xml";
		VelocityUtils.velocityEvaluate(context, mavenPomVmFile, pomPath);
		if (wizardType.equals(Constants.DYNAMIC_WEB)) {
			this.processDynamicWebProject(context, rootPath, projectName);
		}
		processWebXml(wizard, wizardType, rootPath, projectName);
		String zipFileName = rootPath + File.separator + projectName + ".zip";
		WizardFileUtils.generateZipFile(zipFileName, rootPath + File.separator + projectName);
		return fileVmid;
	}

	private void processWebXml(Wizard wizard, String wizardType, String rootPath, String projectName) throws Exception {
		boolean containsBdf2Core = false;
		for (AddonInfo addonInfo : wizard.getAddonInfos()) {
			if (addonInfo.getArtifactId().startsWith("bdf2-core")) {
				containsBdf2Core = true;
				break;
			}
		}
		if (!containsBdf2Core) {
			String targetFilePath = null;
			if (wizardType.equals(Constants.DYNAMIC_WEB)) {
				targetFilePath = rootPath + File.separator + projectName + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "web.xml";
			} else if (wizardType.equals(Constants.MAVEN_WEB)) {
				targetFilePath = rootPath + File.separator + projectName + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator
						+ "web.xml";
			}
			if (targetFilePath != null) {
				WizardFileUtils.copyFile(new File(doradoWebXmlPath), new File(targetFilePath));
			}
		}
	}

	private void processDynamicWebProject(VelocityContext context, String rootPath, String projectName) throws Exception {
		File file = new File(rootPath + File.separator + projectName);
		file.renameTo(new File(rootPath + File.separator + projectName + "-bak"));

		WizardFileUtils.copyDirectiory(dynamicWebTemplatePath, rootPath + File.separator + projectName);
		String destProjectFilePath = rootPath + File.separator + projectName + File.separator + ".project";

		VelocityUtils.velocityEvaluate(context, dynamicWebProjectVmFile, destProjectFilePath);
		String wstCommonComponentFilePath = rootPath + File.separator + projectName + File.separator + ".settings" + File.separator + "org.eclipse.wst.common.component";
		VelocityUtils.velocityEvaluate(context, wstCommonComponentVmFile, wstCommonComponentFilePath);

		StringWriter writer = new StringWriter();
		String outputDirectory = rootPath + File.separator + projectName + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "lib";
		context.put("outputDirectory", outputDirectory);
		VelocityUtils.velocityEvaluate(context, pomBuildVmFile, writer);

		String pomPath = rootPath + File.separator + projectName + "-bak" + File.separator + "pom.xml";
		this.addPomFileBuildElement(writer.toString(), pomPath);
		mavenCompiler.execute(pomPath);
	}

	private void addPomFileBuildElement(String buildText, String pomPath) throws Exception {
		Element buildElement = DocumentHelper.parseText(buildText).getRootElement();
		XMLWriter xmlWriter = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(pomPath);
			Document document = saxReader.read(fileInputStream);
			document.getRootElement().appendContent(buildElement);
			xmlWriter = new XMLWriter(new FileWriter(pomPath));
			xmlWriter.write(document);
		} finally {
			xmlWriter.flush();
			xmlWriter.close();
			fileInputStream.close();
		}
	}

	public void setServletContext(ServletContext sc) {
		this.servletContext = sc;
	}

	public void afterPropertiesSet() throws Exception {
		mavenProjectVmFile = servletContext.getRealPath("/WEB-INF/vm/maven-project.vm");
		dynamicWebProjectVmFile = servletContext.getRealPath("/WEB-INF/vm/dynamic-web-project.vm");
		wstCommonComponentVmFile = servletContext.getRealPath("/WEB-INF/vm/org.eclipse.wst.common.component.vm");
		mavenPomVmFile = servletContext.getRealPath("/WEB-INF/vm/pom.vm");
		pomBuildVmFile = servletContext.getRealPath("/WEB-INF/vm/pom-build.vm");
		mavenTemplatePath = servletContext.getRealPath("/WEB-INF/maven-template");
		doradoWebXmlPath = servletContext.getRealPath("/WEB-INF/vm/web.xml");
		dynamicWebTemplatePath = servletContext.getRealPath("/WEB-INF/dynamic-web-template");
	}
}
