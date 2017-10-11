package com.bstek.bdf2.jbpm4.model;

import java.util.Date;

import org.jbpm.api.task.Task;

/**
 * @author Jacky.gao
 * @since 2013-3-22
 */
public class TodoTask {
	private String id;
	private String name;
	private String desc;
	private Date createDate;
	private String principal;
	private TodoTaskType type;
	private String executionId;
	private String url;	
	private String businessId;	
	private Task task;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public TodoTaskType getType() {
		return type;
	}
	public void setType(TodoTaskType type) {
		this.type = type;
	}
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
}
