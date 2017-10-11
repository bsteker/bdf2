package com.bstek.bdf2.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf2.core.business.IPosition;
import com.bstek.bdf2.core.business.IUser;
/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_POSITION")
public class DefaultPosition implements IPosition,java.io.Serializable{
	private static final long serialVersionUID = 1372378627072030656L;
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
	@Transient
	private List<IUser> users;
	
	public DefaultPosition(){}
	public DefaultPosition(String positionId){
		this.id=positionId;
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
	public List<IUser> getUsers() {
		return users;
	}
	public void setUsers(List<IUser> users) {
		this.users = users;
	}
}
