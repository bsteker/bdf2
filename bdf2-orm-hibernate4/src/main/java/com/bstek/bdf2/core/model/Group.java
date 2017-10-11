package com.bstek.bdf2.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.ListUtils;

import com.bstek.bdf2.core.business.ICompany;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_GROUP")
public class Group implements ICompany,java.io.Serializable{
	private static final long serialVersionUID = 8338680274994080211L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="DESC_",length=120)
	private String desc;
	
	@Column(name="PARENT_ID_",length=60)
	private String parentId;
	
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;
	@Column(name="CREATE_DATE_")
	private Date createDate;
	@Transient
	private List<IUser> users;
	
	@Transient
	private List<IDept> depts;
	
	@Transient
	private List<IPosition> positions;

	public Group(){}
	public Group(String groupId){
		this.id=groupId;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@SuppressWarnings("unchecked")
	public List<IUser> getUsers() {
		if(users==null){
			return ListUtils.EMPTY_LIST;
		}
		return users;
	}
	public void setUsers(List<IUser> users) {
		this.users = users;
	}
	@SuppressWarnings("unchecked")
	public List<IDept> getDepts() {
		if(depts==null){
			return ListUtils.EMPTY_LIST;
		}
		return depts;
	}
	public void setDepts(List<IDept> depts) {
		this.depts = depts;
	}
	@SuppressWarnings("unchecked")
	public List<IPosition> getPositions() {
		if(positions==null){
			return ListUtils.EMPTY_LIST;
		}
		return positions;
	}
	public void setPositions(List<IPosition> positions) {
		this.positions = positions;
	}
}
