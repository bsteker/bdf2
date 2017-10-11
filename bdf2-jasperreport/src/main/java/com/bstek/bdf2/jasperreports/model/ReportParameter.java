package com.bstek.bdf2.jasperreports.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BDF2_REPORT_PARAMETER")
public class ReportParameter implements java.io.Serializable{
	private static final long serialVersionUID = -2846142980459601449L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="VALUE_",length=120)
	private String value;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReportDefinitionId() {
		return reportDefinitionId;
	}
	public void setReportDefinitionId(String reportDefinitionId) {
		this.reportDefinitionId = reportDefinitionId;
	}
}
