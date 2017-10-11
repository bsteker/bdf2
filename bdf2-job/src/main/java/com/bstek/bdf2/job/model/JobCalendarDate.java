package com.bstek.bdf2.job.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BDF2_JOB_CALENDAR_DATE")
public class JobCalendarDate  implements java.io.Serializable{
	private static final long serialVersionUID = -9078588793964776579L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="CALENDAR_ID_",length=60)
	private String calendarId;
	@Column(name="CALENDAR_DATE_")
	private Date calendarDate;
	@Column(name="DAY_OF_MONTH_")
	private int dayOfMonth;
	@Column(name="MONTH_OF_YEAR_")
	private int monthOfYear;
	@Column(name="DAY_OF_WEEK_")
	private int dayOfWeek;
	
	
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
	public String getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}
	public Date getCalendarDate() {
		return calendarDate;
	}
	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getMonthOfYear() {
		return monthOfYear;
	}
	public void setMonthOfYear(int monthOfYear) {
		this.monthOfYear = monthOfYear;
	}
	
}
