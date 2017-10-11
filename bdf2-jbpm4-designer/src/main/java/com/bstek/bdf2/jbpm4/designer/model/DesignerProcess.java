package com.bstek.bdf2.jbpm4.designer.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-6-24
 */
@Entity
@Table(name="BDF2_JBPM4_DESIGNER_PROCESS")
public class DesignerProcess {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="VERSION_")
	private int version;	
	@Column(name="DEPLOYMENT_ID_",length=120)
	private String deploymentId;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="LOB_ID_",length=60)
	private String lobId;
	@Column(name="CREATE_DATE_")
	private Date createDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getLobId() {
		return lobId;
	}
	public void setLobId(String lobId) {
		this.lobId = lobId;
	}
}
