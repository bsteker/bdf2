package com.bstek.bdf2.jbpm4.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BDF2_JBPM4_REMINDER_JOB")
public class ReminderJob {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	
	@Column(name="EXECUTION_ID_",length=60)
	private String executionId;
	
	@Column(name="TASK_ID_",length=60)
	private String taskId;
	
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	
	@Column(name="OVERDUE_DAYS_")
	private int overdueDays;
	
	@Column(name="OVERDUE_METHOD_",length=12,nullable=false)
	@Enumerated(EnumType.STRING)
	private OverdueMethod overdueMethod;
	
	@Column(name="OVERDUE_CUSTOM_BEAN_PROCESSOR",length=120)
	private String overdueCustomBeanProcessor;
	
	@Column(name="TASK_REMINDER_ID_",length=60)
	private String taskReminderId;
	
	@Column(name="MESSAGE_SENDERS_",length=120)
	private String messageSenders;	
	
	@Column(name="CRON_EXPRESSION_",length=60)
	private String cronExpression;
	
	@Column(name="STATE_",length=10,nullable=false)
	@Enumerated(EnumType.STRING)
	private ReminderJobState state;
	
	@Column(name="MESSAGE_TEMPLATE_ID_",length=60)
	private String messageTemplateId;
	
	@Column(name="PRINCIPAL_",length=60)
	private String principal;
	
	@Column(name="CREATE_DATE_")
	private Date createDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public int getOverdueDays() {
		return overdueDays;
	}
	public void setOverdueDays(int overdueDays) {
		this.overdueDays = overdueDays;
	}
	public String getTaskReminderId() {
		return taskReminderId;
	}
	public void setTaskReminderId(String taskReminderId) {
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
	
	public OverdueMethod getOverdueMethod() {
		return overdueMethod;
	}
	public void setOverdueMethod(OverdueMethod overdueMethod) {
		this.overdueMethod = overdueMethod;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getMessageSenders() {
		return messageSenders;
	}
	public void setMessageSenders(String messageSenders) {
		this.messageSenders = messageSenders;
	}
	public String getOverdueCustomBeanProcessor() {
		return overdueCustomBeanProcessor;
	}
	public void setOverdueCustomBeanProcessor(String overdueCustomBeanProcessor) {
		this.overdueCustomBeanProcessor = overdueCustomBeanProcessor;
	}
}
