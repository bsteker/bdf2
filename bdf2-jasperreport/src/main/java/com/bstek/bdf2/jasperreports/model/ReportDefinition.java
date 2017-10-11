package com.bstek.bdf2.jasperreports.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Jacky.gao
 * @since 2013-5-1
 */
@Entity
@Table(name="BDF2_REPORT_DEFINITION")
public class ReportDefinition  implements java.io.Serializable{
	private static final long serialVersionUID = 6918565845295039760L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="REPORT_FILE_",length=120)
	private String reportFile;
	@Transient
	private String reportFileName;
	@Column(name="DATA_SOURCE_",length=60)
	private String dataSource;
	@Column(name="DATA_SOURCE_TYPE_",length=20)
	@Enumerated(EnumType.STRING)
	private DataSourceType dataSourceType;
	@Column(name="CREATE_DATE_")
	private Date createDate;
	@Transient
	private Collection<ReportParameter> parameters;
	@Transient
	private Collection<ReportResource> resources;
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
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	
	public String getReportFileName() {
		return reportFileName;
	}
	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}
	public void setDataSourceType(DataSourceType dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Collection<ReportParameter> getParameters() {
		return parameters;
	}
	public void setParameters(Collection<ReportParameter> parameters) {
		this.parameters = parameters;
	}
	public Collection<ReportResource> getResources() {
		return resources;
	}
	public void setResources(Collection<ReportResource> resources) {
		this.resources = resources;
	}
}
