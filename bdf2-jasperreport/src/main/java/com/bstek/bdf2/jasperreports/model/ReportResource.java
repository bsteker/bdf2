package com.bstek.bdf2.jasperreports.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="BDF2_REPORT_RESOURCE")
public class ReportResource  implements java.io.Serializable{
	private static final long serialVersionUID = 5175930661185334409L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="RESOURCE_FILE_",length=60)
	private String resourceFile;	
	@Transient
	private String resourceFileName;
	@Column(name="REPORT_DEFINITION_ID_",length=60)
	private String reportDefinitionId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResourceFile() {
		return resourceFile;
	}
	public void setResourceFile(String resourceFile) {
		this.resourceFile = resourceFile;
	}
	public String getResourceFileName() {
		return resourceFileName;
	}
	public void setResourceFileName(String resourceFileName) {
		this.resourceFileName = resourceFileName;
	}
	public String getReportDefinitionId() {
		return reportDefinitionId;
	}
	public void setReportDefinitionId(String reportDefinitionId) {
		this.reportDefinitionId = reportDefinitionId;
	}
}
