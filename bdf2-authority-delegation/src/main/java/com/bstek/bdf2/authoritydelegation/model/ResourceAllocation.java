package com.bstek.bdf2.authoritydelegation.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BDF2_RESOURCE_ALLOCATION")
public class ResourceAllocation implements java.io.Serializable{
	private static final long serialVersionUID = 6419810166747151874L;
	@Id
	@Column(name = "ID_", length = 60)
	private String id;
	@Column(name = "RESOURCE_TYPE_")
	private String resourceType;//

	/**
	 * U表示URL,M表示Module，W表示Webservice,E表示User，D表示Dept，G表示Group,P表示Position
	 */
	public enum ResourceType {
		U, M, W, E, D, G, P;
		public static boolean contain(String resType) {
			ResourceType[] resTypeList = ResourceType.values();
			for (ResourceType rT : resTypeList) {
				if (rT.name().equals(resType)) {
					return true;
				}
			}
			return false;
		}
	}

	@Column(name = "RESOURCE_ID_", length = 60)
	private String resourceId;
	@Column(name = "RESOURCE_OWNER_", length = 60)
	private String resourceOwner;
	@Column(name = "CREATE_USER_", length = 60)
	private String createUser;
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE_")
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceOwner() {
		return resourceOwner;
	}

	public void setResourceOwner(String resourceOwner) {
		this.resourceOwner = resourceOwner;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
