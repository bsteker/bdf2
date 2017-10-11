package com.bstek.bdf2.jbpm4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-3-25
 */
@Entity
@Table(name="BDF2_JBPM4_TASK_CONFIG")
public class TaskConfig {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="PROCESS_DEFINITION_ID_",length=120)
	private String processDefinitionId;
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	@Column(name="URL_",length=120)
	private String url;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
