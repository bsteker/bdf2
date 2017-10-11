package com.bstek.bdf2.job.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="BDF2_JOB")
public class JobDefinition implements java.io.Serializable{
	private static final long serialVersionUID = -7764663310851067007L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="BEAN_ID_",length=60)
	private String beanId;
	@Column(name="CRON_EXPRESSION_",length=30)
	private String cronExpression;
	@Column(name="DESC_",length=120)
	private String desc;
	@Column(name="START_DATE")
	private Date startDate;
	@Column(name="END_DATE")
	private Date endDate;
	@Column(name="STATE_",length=12)
	@Enumerated(EnumType.STRING)
	private JobState state;
	@Transient
	private List<JobCalendar> calendars;
	@Transient
	private List<JobHistory> histories;
	@Transient
	private List<JobParameter> parameters;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public JobState getState() {
		return state;
	}
	public void setState(JobState state) {
		this.state = state;
	}
	public List<JobCalendar> getCalendars() {
		return calendars;
	}
	public void setCalendars(List<JobCalendar> calendars) {
		this.calendars = calendars;
	}
	public List<JobHistory> getHistories() {
		return histories;
	}
	public void setHistories(List<JobHistory> histories) {
		this.histories = histories;
	}
	public List<JobParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<JobParameter> parameters) {
		this.parameters = parameters;
	}
}
