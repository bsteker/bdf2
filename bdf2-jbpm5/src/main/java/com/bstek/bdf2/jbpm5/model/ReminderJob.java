package com.bstek.bdf2.jbpm5.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="JBPM5_REMINDER_JOB")
@SequenceGenerator(name="reminderJobSeqId",sequenceName="JBPM5_REMINDER_JOB_ID_SEQ")
public class ReminderJob {
	@Id
	@Column(name="ID_",length=60)
	@GeneratedValue(strategy=GenerationType.AUTO,generator="reminderJobSeqId")
	private long id;
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	@Column(name="PROCESS_INSTANCE_ID_")
	private long processInstanceId;
	@Column(name="TASK_ID_")
	private long taskId;
	//link to TaskReminder
	@Column(name="TASK_REMINDER_ID_",length=60)
	private long taskReminderId;
	@Column(name="MESSAGE_SENDERS_",length=120)
	private String messageSenders;	
	@Column(name="CRON_EXPRESSION_",length=60)
	private String cronExpression;
	@Column(name="STATE_")
	private ReminderJobState state;
	@Column(name="MESSAGE_TEMPLATE_ID_",length=60)
	private String messageTemplateId;
	@Column(name="PRINCIPAL_",length=60)
	private String principal;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public ReminderJobState getState() {
		return state;
	}
	public void setState(ReminderJobState state) {
		this.state = state;
	}
	public long getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public long getTaskReminderId() {
		return taskReminderId;
	}
	public void setTaskReminderId(long taskReminderId) {
		this.taskReminderId = taskReminderId;
	}
	public String getMessageTemplateId() {
		return messageTemplateId;
	}
	public void setMessageTemplateId(String messageTemplateId) {
		this.messageTemplateId = messageTemplateId;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getMessageSenders() {
		return messageSenders;
	}
	public void setMessageSenders(String messageSenders) {
		this.messageSenders = messageSenders;
	}
}
