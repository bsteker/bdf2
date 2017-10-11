package com.bstek.bdf2.jbpm4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BDF2_JBPM4_INTERNAL_ASSIGNMENT")
public class InternalAssignment {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="ASSIGNMENT_DEF_ID_",length=60)
	private String assignmentDefId;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="VALUE_",length=60)
	private String value;
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
}
