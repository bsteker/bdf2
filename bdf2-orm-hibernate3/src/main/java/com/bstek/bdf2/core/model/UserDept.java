package com.bstek.bdf2.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @since 2013-1-26
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_USER_DEPT")
public class UserDept implements java.io.Serializable{
	private static final long serialVersionUID = 4775581196397644523L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="USERNAME_",length=60,nullable=false)
	private String username;
	@Column(name="DEPT_ID_",length=60,nullable=false)
	private String deptId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
}
