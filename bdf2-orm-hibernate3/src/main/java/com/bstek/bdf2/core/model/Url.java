package com.bstek.bdf2.core.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf2.core.business.ICompany;

/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_URL")
public class Url implements ICompany,java.io.Serializable{
	private static final long serialVersionUID = -7769851883836480732L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60,nullable=false)
	private String name;
	@Column(name="DESC_",length=120)
	private String desc;
	@Column(name="URL_",length=120)
	private String url;
	@Column(name="FOR_NAVIGATION_",nullable=false)
	private boolean forNavigation=true;
	@Column(name="COMPANY_ID_",length=60,nullable=false)
	private String companyId;
	@Column(name="SYSTEM_ID_",length=60)
	private String systemId;
	
	@Column(name="ICON_",length=120)
	private String icon;
	
	@Column(name="ORDER_")
	private Integer order;
	
	@Column(name="PARENT_ID_",length=60)
	private String parentId;
	
	@Transient
	private boolean use;
	
	@Transient
	private List<Role> roles;
	
	@Transient
	private List<Url> children;
	
	public String getCompanyId() {
		return companyId;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public List<Url> getChildren() {
		return children;
	}

	public void setChildren(List<Url> children) {
		this.children = children;
	}

	public boolean isForNavigation() {
		return forNavigation;
	}

	public void setForNavigation(boolean forNavigation) {
		this.forNavigation = forNavigation;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
