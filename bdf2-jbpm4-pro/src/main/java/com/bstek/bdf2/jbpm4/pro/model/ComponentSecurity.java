package com.bstek.bdf2.jbpm4.pro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDF2_ComponentSecurity")
public class ComponentSecurity {
	@Id
	@Column(name = "id_", length = 50)
	private String id;
	@Column(name = "pdId_", length = 50)
	private String pdId;
	@Column(name = "taskName_", length = 50)
	private String taskName;
	@Column(name = "tag_", length = 50)
	private String tag;
	@Column(name = "type_")
	private int type;
	@Column(name = "priority_")
	private int priority;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPdId() {
		return pdId;
	}

	public void setPdId(String pdId) {
		this.pdId = pdId;
	}

	public String getTag() {
		return tag;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
