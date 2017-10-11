package com.bstek.bdf2.jasperreports.component;

import com.bstek.bdf2.jasperreports.model.DataSourceType;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;

@Widget(name = "JasperreportsExporter",dependsPackage = "JasperreportsExporter",category="BDF2")
@ClientObject(prototype = "dorado.widget.JasperreportsExporter", shortTypeName = "JasperreportsExporter")
@XmlNode(nodeName="JasperreportsExporter")
public class JasperreportsExporterControl extends Control{
	private String reportFile;
	private String fileSource;
	private DataSourceType dataSourceType;
	private String dataSourceProvider;
	private String exportFileType="jrpxml";
	private boolean cache;
	private Object parameter;
	private String exportFileName;

	@ClientProperty
	@IdeProperty(highlight=1)
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	
	@ClientProperty(escapeValue="uploadedFile")
	@IdeProperty(highlight=1,enumValues="file,uploadedFile")
	public String getFileSource() {
		return fileSource;
	}
	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}
	@ClientProperty(escapeValue="Jdbc")
	@IdeProperty(highlight=1,enumValues="Jdbc,JavaBean,Map")
	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}
	public void setDataSourceType(DataSourceType dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
	
	@ClientProperty
	@IdeProperty(highlight=1)
	public String getDataSourceProvider() {
		return dataSourceProvider;
	}
	public void setDataSourceProvider(String dataSourceProvider) {
		this.dataSourceProvider = dataSourceProvider;
	}
	
	@ClientProperty(escapeValue="jrpxml")
	@IdeProperty(highlight=1,enumValues="jrpxml,pdf,rtf,xls,html,docx,csv,pptx")
	public String getExportFileType() {
		return exportFileType;
	}
	public void setExportFileType(String exportFileType) {
		this.exportFileType = exportFileType;
	}
	
	@ClientProperty(escapeValue="false")
	public boolean isCache() {
		return cache;
	}
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	
	@XmlProperty
	@ClientProperty(outputter = "spring:dorado.doradoMapPropertyOutputter")
	@IdeProperty(editor = "any")
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	
	@ClientProperty(escapeValue="report")
	public String getExportFileName() {
		return exportFileName;
	}
	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}
}
