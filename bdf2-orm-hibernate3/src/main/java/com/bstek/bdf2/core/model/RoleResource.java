package com.bstek.bdf2.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BDF2_ROLE_RESOURCE")
public class RoleResource  implements java.io.Serializable{
	private static final long serialVersionUID = 2788321797016604980L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="ROLE_ID_",length=60)
	private String roleId;
	@Column(name="URL_ID_",length=60)
	private String urlId;
	@Column(name="PACKAGE_ID_",length=60)
	private String packageId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUrlId() {
		return urlId;
	}
	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
}
