package com.bstek.bdf2.jbpm4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
@Entity
@Table(name="BDF2_JBPM4_TASK_ASSIGNMENT")
public class TaskAssignment {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="ASSIGNMENT_DEF_ID_",length=60)
	private String assignmentDefId;
	@Column(name="PROCESS_DEFINITION_ID_",length=120)
	private String processDefinitionId;
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	@Transient
	private String assignmentDefName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAssignmentDefId() {
		return assignmentDefId;
	}
	public void setAssignmentDefId(String assignmentDefId) {
		this.assignmentDefId = assignmentDefId;
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
	public String getAssignmentDefName() {
		return assignmentDefName;
	}
	public void setAssignmentDefName(String assignmentDefName) {
		this.assignmentDefName = assignmentDefName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}
