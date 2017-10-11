package com.bstek.bdf2.job.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Jacky.gao
 * @since 2013-3-10
 */
@Entity
@Table(name="BDF2_JOB_CALENDAR")
public class JobCalendar implements java.io.Serializable{
	private static final long serialVersionUID = 2579525564734297159L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="DESC_",length=120)
	private String desc;
	@Column(name="TYPE_",length=12)
	@Enumerated(EnumType.STRING)
	private CalendarType type;
	
	@Transient
	private List<JobCalendarDate> calendarDates;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public CalendarType getType() {
		return type;
	}
	public void setType(CalendarType type) {
		this.type = type;
	}
	public List<JobCalendarDate> getCalendarDates() {
		return calendarDates;
	}
	public void setCalendarDates(List<JobCalendarDate> calendarDates) {
		this.calendarDates = calendarDates;
	}
}
