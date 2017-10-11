package com.bstek.bdf2.authoritydelegation.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.bstek.bdf2.core.business.ICompany;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.model.Group;
import com.bstek.bdf2.core.model.Url;

@Entity
@Table(name = "BDF2_RESOURCE_OWNER")
public class ResourceOwner implements ICompany,java.io.Serializable{
	private static final long serialVersionUID = 5184293480715578523L;
	@Id
	@Column(name = "USERNAME_", length = 60)
	private String username;
	@Column(name = "CREATE_USER_", length = 60)
	private String createUser;
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE_")
	private Date createDate;
	@Column(name = "COMPANY_ID_", length = 60)
	private String companyId;
	@Transient
	private List<IUser> users;
	@Transient
	private List<IDept> depts;
	@Transient
	private List<IPosition> positions;
	@Transient
	private List<Group> groups;
	@Transient
	private List<Url> urls;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<IUser> getUsers() {
		return users;
	}

	public void setUsers(List<IUser> users) {
		this.users = users;
	}

	public List<IDept> getDepts() {
		return depts;
	}

	public void setDepts(List<IDept> depts) {
		this.depts = depts;
	}

	public List<IPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<IPosition> positions) {
		this.positions = positions;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Url> getUrls() {
		return urls;
	}

	public void setUrls(List<Url> urls) {
		this.urls = urls;
	}
}