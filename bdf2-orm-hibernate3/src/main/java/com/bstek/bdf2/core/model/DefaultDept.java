package com.bstek.bdf2.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IUser;
/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_DEPT")
public class DefaultDept implements IDept,java.io.Serializable{
	private static final long serialVersionUID = -7669420244144806105L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="DESC_",length=120)
	private String desc;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	@Column(name="CREATE_DATE_")
	private Date createDate;
	@Column(name="PARENT_ID_",length=60)
	private String parentId;
	
	@Transient
	private IDept parent;
	@Transient
	private List<IUser> users;
	@Transient
	private List<IDept> children;
	
	public DefaultDept(){}
	
	public DefaultDept(String deptId){
		this.id=deptId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public IDept getParent() {
		return parent;
	}
	public void setParent(IDept parent) {
		this.parent = parent;
	}
	public List<IUser> getUsers() {
		return users;
	}
	public void setUsers(List<IUser> users) {
		this.users = users;
	}
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<IDept> getChildren() {
		return children;
	}

	public void setChildren(List<IDept> children) {
		this.children = children;
	}
	
}
