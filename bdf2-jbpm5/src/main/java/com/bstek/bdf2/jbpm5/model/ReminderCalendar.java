package com.bstek.bdf2.jbpm5.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-3-26
 */
@Entity
@Table(name="JBPM5_REMINDER_CALENDAR")
@SequenceGenerator(name="reminderCalendarSeqId",sequenceName="JBPM5_REMINDER_CALENDAR_ID_SEQ")
public class ReminderCalendar {
	@Id
	@Column(name="ID_")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="reminderCalendarSeqId")
	private long id;
	@Column(name="JOB_CALENDAR_ID_",length=60)
	private String jobCalendarId;
	@Column(name="TASK_REMINDER_ID_",length=60)
	private String taskReminderId;

	public long getId() {
		return id;
	}
	public void setId(long id) {
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
