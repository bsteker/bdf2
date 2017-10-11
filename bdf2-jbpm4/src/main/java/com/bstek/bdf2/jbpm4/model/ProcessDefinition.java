package com.bstek.bdf2.jbpm4.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BDF2_JBPM4_PROCESSDEFINITION")
public class ProcessDefinition {
	@Id
	@Column(name="ID_",length=120)
	private String id;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	@Column(name="CREATE_DATE_")
	private Date createDate;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
