package com.bstek.bdf2.jbpm4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-6-28
 */
@Entity
@Table(name="BDF2_JBPM4_COMPONENT_CONTROL")
public class ComponentControl {
	@Id
	@Column(name="ID_",length=60)
	private String id;	
	@Column(name="URL_",length=120)
	private String url;
	@Column(name="COMPONENT_ID_",length=120)
	private String componentId;
	@Column(name="PROCESS_DEFINITION_ID_",length=120)
	private String processDefinitionId;
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	@Column(name="CONTROL_TYPE_",length=12)
	@Enumerated(EnumType.STRING)
	private ControlType controlType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
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
	public ControlType getControlType() {
		return controlType;
	}
	public void setControlType(ControlType controlType) {
		this.controlType = controlType;
	}
}
