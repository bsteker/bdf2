package com.bstek.bdf2.job.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-3-13
 */
@Entity
@Table(name="BDF2_JOB_PARAMETER")
public class JobParameter implements java.io.Serializable{
	private static final long serialVersionUID = -3993527989521294208L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="JOB_ID_",length=60)
	private String jobId;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="VALUE_",length=120)
	private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
