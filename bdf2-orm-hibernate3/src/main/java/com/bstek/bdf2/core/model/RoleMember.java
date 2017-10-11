package com.bstek.bdf2.core.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
/**
 * @since 2013-1-24
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_ROLE_MEMBER")
public class RoleMember implements java.io.Serializable{
	private static final long serialVersionUID = 4592886341415257236L;

	@Id
	@Column(name="ID_",length=60)
	private String id;
	
	@Column(name="USERNAME_",length=60)
	private String username;
	
	@Column(name="DEPT_ID_",length=60)
	private String deptId;
	
	@Column(name="POSITION_ID_",length=60)
	private String positionId;
	
	@Column(name="CREATE_DATE_")
	private Date createDate;
	
	@Column(name="ROLE_ID_",length=60)
	private String roleId;
	
	@Column(name="GRANTED_")
	private boolean granted;

	@Transient
	private IUser user;

	@Transient
	private IDept dept;
	
	@Transient
	private IPosition position;
	
	@ManyToOne(cascade=CascadeType.MERGE,targetEntity=Group.class,fetch=FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="GROUP_ID_")
	private Group group;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
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
	public boolean isGranted() {
		return granted;
	}
	public void setGranted(boolean granted) {
		this.granted = granted;
	}
}
