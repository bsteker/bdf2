package com.bstek.bdf2.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.bstek.bdf2.core.business.ICompany;
/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_ROLE")
public class Role implements GrantedAuthority,ICompany,java.io.Serializable{
	public static final String NORMAL_TYPE="normal";
	private static final long serialVersionUID = 4795171480171541919L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="DESC_",length=120)
	private String desc;
	@Column(name="TYPE_",length=10)
	private String type;
	
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;

	@Column(name="PARENT_ID_",length=60)
	private String parentId;
	
	@Column(name="CREATE_DATE_")
	private Date createDate;
	
	@Column(name="CREATE_USER_",length=60)
	private String createUser;
	
	@Column(name="ENABLED_")
	private boolean enabled=true;
	
	@Transient
	private List<Role> children;
	
	@Transient
	private List<Url> urls;
	@Transient
	private List<UrlComponent> urlComponents;
	@Transient
	private List<RoleMember> roleMembers;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public List<Url> getUrls() {
		return urls;
	}
	public void setUrls(List<Url> urls) {
		this.urls = urls;
	}

	public List<UrlComponent> getUrlComponents() {
		return urlComponents;
	}
	public void setUrlComponents(List<UrlComponent> urlComponents) {
		this.urlComponents = urlComponents;
	}
	public List<Role> getChildren() {
		return children;
	}
	public void setChildren(List<Role> children) {
		this.children = children;
	}
	public List<RoleMember> getRoleMembers() {
		return roleMembers;
	}
	public void setRoleMembers(List<RoleMember> roleMembers) {
		this.roleMembers = roleMembers;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getAuthority() {
		return getId();
	}
	
	public String toString(){
		return "companyId:"+companyId+", name:"+name + ", type:"+type + ", enabled:"+ enabled;
	}
}
