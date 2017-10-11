package com.bstek.bdf2.job.daemon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
@Entity
@Table(name="BDF2_JOB_HEARTBEAT")
public class Heartbeat implements java.io.Serializable{
	private static final long serialVersionUID = -1697046501724302398L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="INSTANCE_NAME_",length=60)
	private String instanceName;
	@Column(name="DATE_")
	private Date date;
	@Column(name="APPLICATION_NAME_",length=60)
	private String applicationName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
