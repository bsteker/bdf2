package com.bstek.bdf2.jbpm5.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * @author Jacky.gao
 * @since 2013-4-9
 */
@Entity
@Table(name="JBPM5_TASK_LOG")
@SequenceGenerator(name="taskLogSeqId",sequenceName="JBPM5_TASK_LOG_ID_SEQ",allocationSize=1)
public class TaskLog {
	@Id
	@Column(name="ID_")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="taskLogSeqId")
	private long id;

	@Column(name="TASK_ID_",nullable=false,unique=true)
	private long taskId;
	
	@Column(name="NAME_",length=60,nullable=false)
	private String name;
	
	@Column(name="BUSINESS_ID_",length=60)
	private String businessId;
	
	@Column(name="TYPE_")
	private TaskType type;
	
	@Column(name="PRIORITY_")
	private int priority;
	
	//任务所有人
	@Column(name="OWNER_",length=60)
	private String owner;
	
	//任务处理人
	@Column(name="ACTOR_",length=60)
	private String actor;
	
	@Column(name="CMNT_")
	private String cmnt;
	
	@Column(name="PROCESS_ID_")
	private String processId;
	
	@Column(name="WORK_ITEM_ID_")
	private long workItemId;
	
	@Column(name="PROCESS_INSTANCE_ID_")
	private long processInstanceId;
	
	@Column(name="SESSION_ID_")
	private int sessionId;
	
	@Column(name="STATUS_")
	private TaskStatus status;
	
	@Column(name="PREVIOUS_STATUS_")
	private TaskStatus previousStatus = null;
	
	@Column(name="START_")
	private Date start;
	
	@Column(name="END_")
	private Date end;
	
	@Column(name="CREATE_")
	private Date create;
	
	@Column(name="PARENT_ID_")
	private long parentId=-1;
	
	@Column(name="SKIPPABLE_")
	private boolean skippable;
	
	//仅在将任务状态设置为Failed时需要填充该消息
	@Column(name="FAULT_MESSAGE_")
	private String faultMessage;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getCmnt() {
		return cmnt;
	}

	public void setCmnt(String cmnt) {
		this.cmnt = cmnt;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(long workItemId) {
		this.workItemId = workItemId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public TaskStatus getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(TaskStatus previousStatus) {
		this.previousStatus = previousStatus;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public boolean isSkippable() {
		return skippable;
	}

	public void setSkippable(boolean skippable) {
		this.skippable = skippable;
	}

	public String getFaultMessage() {
		return faultMessage;
	}

	public void setFaultMessage(String faultMessage) {
		this.faultMessage = faultMessage;
	}
}
