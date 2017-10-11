package com.bstek.bdf2.jbpm5.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf2.job.model.JobCalendar;

/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
@Entity
@Table(name="JBPM5_TASK_REMINDER")
@SequenceGenerator(name="taskReminderSeqId",sequenceName="JBPM5_TASK_REMINDER_ID_SEQ")
public class TaskReminder {
	@Id
	@Column(name="ID_")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="taskReminderSeqId")
	private long id;
	@Column(name="EVERY_DAY_")
	private int everyDay;
	@Column(name="EVERY_HOUR_")
	private int everyHour;
	@Column(name="MESSAGE_TEMPLATE_ID_",length=60)
	private String messageTemplateId;
	@Column(name="PROCESS_ID_")
	private String processId;
	@Column(name="TASK_NAME_",length=120)
	private String taskName;
	@Column(name="REMINDER_TYPE_",length=30)
	private ReminderType reminderType;
	
	@Column(name="MESSAGE_SENDERS_",length=120)
	private String messageSenders;
	
	@Transient
	private String messageTemplateName;	
	@Transient
	private Collection<JobCalendar> calendars;

	public long getId() {
		return id;
	}
	public void setId(long id) {
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

	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
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
}
