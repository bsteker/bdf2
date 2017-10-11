package com.bstek.bdf2.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;

@Entity
@Table(name="BDF2_GROUP_MEMBER")
public class GroupMember implements java.io.Serializable{
	private static final long serialVersionUID = -557520158476738610L;

	@Id
	@Column(name="ID_",length=60)
	private String id;
	
	@Column(name="GROUP_ID_",length=60)
	private String groupId;

	@Column(name="USERNAME_",length=60)
	private String username;
	
	@Column(name="DEPT_ID_",length=60)
	private String deptId;
	
	@Column(name="POSITION_ID_",length=60)
	private String positionId;
	@Transient
	private IUser user;
	@Transient
	private IDept dept;
	@Transient
	private IPosition position;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	public IDept getDept() {
		return dept;
	}

	public void setDept(IDept dept) {
		this.dept = dept;
	}

	public IPosition getPosition() {
		return position;
	}

	public void setPosition(IPosition position) {
		this.position = position;
	}
}
