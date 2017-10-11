package com.bstek.bdf2.jbpm4.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf2.job.model.JobCalendar;

/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
@Entity
@Table(name="BDF2_JBPM4_TASK_REMINDER")
public class TaskReminder {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	
	@Column(name="EVERY_DAY_")
	private int everyDay;
	
	@Column(name="EVERY_HOUR_")
	private int everyHour;
	
	@Column(name="MESSAGE_TEMPLATE_ID_",length=60)
	private String messageTemplateId;
	
	@Column(name="PROCESS_DEFINITION_ID_",length=120)
	private String processDefinitionId;
	
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	
	@Column(name="REMINDER_TYPE_",length=12)
	@Enumerated(EnumType.STRING)
	private ReminderType reminderType;
	
	@Column(name="OVERDUE_METHOD_",length=12)
	@Enumerated(EnumType.STRING)
	private OverdueMethod overdueMethod;
	
	@Column(name="OVERDUE_CUSTOM_BEAN_PROCESSOR",length=120)
	private String overdueCustomBeanProcessor;
	
	@Column(name="OVERDUE_DAYS_")
	private int overdueDays;
	
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	
	@Column(name="REMINDER_CATEGORY_",length=30)
	@Enumerated(EnumType.STRING)
	private ReminderCategory category;
	
	@Column(name="MESSAGE_SENDERS_",length=120)
	private String messageSenders;
	
	@Transient
	private String messageTemplateName;	
	
	@Transient
	private Collection<JobCalendar> calendars;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getEveryDay() {
		return everyDay;
	}
	public void setEveryDay(int everyDay) {
		this.everyDay = everyDay;
	}
	public int getEveryHour() {
		return everyHour;
	}
	public void setEveryHour(int everyHour) {
		this.everyHour = everyHour;
	}
	public String getMessageTemplateId() {
		return messageTemplateId;
	}
	public void setMessageTemplateId(String messageTemplateId) {
		this.messageTemplateId = messageTemplateId;
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
	public String getMessageTemplateName() {
		return messageTemplateName;
	}
	public void setMessageTemplateName(String messageTemplateName) {
		this.messageTemplateName = messageTemplateName;
	}
	public ReminderType getReminderType() {
		return reminderType;
	}
	public void setReminderType(ReminderType reminderType) {
		this.reminderType = reminderType;
	}
	public Collection<JobCalendar> getCalendars() {
		return calendars;
	}
	public void setCalendars(Collection<JobCalendar> calendars) {
		this.calendars = calendars;
	}
	public String getMessageSenders() {
		return messageSenders;
	}
	public void setMessageSenders(String messageSenders) {
		this.messageSenders = messageSenders;
	}
	public ReminderCategory getCategory() {
		return category;
	}
	public void setCategory(ReminderCategory category) {
		this.category = category;
	}
	public int getOverdueDays() {
		return overdueDays;
	}
	public void setOverdueDays(int overdueDays) {
		this.overdueDays = overdueDays;
	}
	public OverdueMethod getOverdueMethod() {
		return overdueMethod;
	}
	public void setOverdueMethod(OverdueMethod overdueMethod) {
		this.overdueMethod = overdueMethod;
	}
	public String getOverdueCustomBeanProcessor() {
		return overdueCustomBeanProcessor;
	}
	public void setOverdueCustomBeanProcessor(String overdueCustomBeanProcessor) {
		this.overdueCustomBeanProcessor = overdueCustomBeanProcessor;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
