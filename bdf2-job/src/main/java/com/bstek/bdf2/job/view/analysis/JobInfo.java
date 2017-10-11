package com.bstek.bdf2.job.view.analysis;

import java.util.Date;

public class JobInfo {
	private String name;
	private String group;
	private JobType type;
	private String triggerInfo;
	private String calendarInfo;
	private String targetJobInfo;
	private Date nextFireDate;
	private Date previousFireDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public JobType getType() {
		return type;
	}
	public void setType(JobType type) {
		this.type = type;
	}
	public String getTargetJobInfo() {
		return targetJobInfo;
	}
	public void setTargetJobInfo(String targetJobInfo) {
		this.targetJobInfo = targetJobInfo;
	}
	public String getTriggerInfo() {
		return triggerInfo;
	}
	public void setTriggerInfo(String triggerInfo) {
		this.triggerInfo = triggerInfo;
	}
	public String getCalendarInfo() {
		return calendarInfo;
	}
	public void setCalendarInfo(String calendarInfo) {
		this.calendarInfo = calendarInfo;
	}
	public Date getNextFireDate() {
		return nextFireDate;
	}
	public void setNextFireDate(Date nextFireDate) {
		this.nextFireDate = nextFireDate;
	}
	public Date getPreviousFireDate() {
		return previousFireDate;
	}
	public void setPreviousFireDate(Date previousFireDate) {
		this.previousFireDate = previousFireDate;
	}
	
}

