package com.bstek.bdf2.jbpm4.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Jacky.gao
 * @since 2013-6-3
 */
@Entity
@Table(name="BDF2_JBPM4_TOOLBAR_CONFIG")
public class ToolbarConfig {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="TOOLBAR_POSITION_",length=20)
	@Enumerated(EnumType.STRING)
	private ToolbarPosition toolbarPosition;
	@Column(name="PROCESS_DEFINITION_ID_",length=120)
	private String processDefinitionId;
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	@Transient
	private boolean useToolbar;
	@Transient
	private Collection<ToolbarContent> contents;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ToolbarPosition getToolbarPosition() {
		return toolbarPosition;
	}
	public void setToolbarPosition(ToolbarPosition toolbarPosition) {
		this.toolbarPosition = toolbarPosition;
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
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public boolean isUseToolbar() {
		return useToolbar;
	}
	public void setUseToolbar(boolean useToolbar) {
		this.useToolbar = useToolbar;
	}
	public Collection<ToolbarContent> getContents() {
		return contents;
	}
	public void setContents(Collection<ToolbarContent> contents) {
		this.contents = contents;
	}	
}
