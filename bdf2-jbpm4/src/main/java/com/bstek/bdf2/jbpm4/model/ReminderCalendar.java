package com.bstek.bdf2.jbpm4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-3-26
 */
@Entity
@Table(name="BDF2_JBPM4_REMINDER_CALENDAR")
public class ReminderCalendar {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="JOB_CALENDAR_ID_",length=60)
	private String jobCalendarId;
	@Column(name="TASK_REMINDER_ID_",length=60)
	private String taskReminderId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobCalendarId() {
		return jobCalendarId;
	}
	public void setJobCalendarId(String jobCalendarId) {
		this.jobCalendarId = jobCalendarId;
	}
	public String getTaskReminderId() {
		return taskReminderId;
	}
	public void setTaskReminderId(String taskReminderId) {
		this.taskReminderId = taskReminderId;
	}
}
