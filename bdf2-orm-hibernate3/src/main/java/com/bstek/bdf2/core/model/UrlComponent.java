package com.bstek.bdf2.core.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
@Entity
@Table(name="BDF2_URL_COMPONENT")
public class UrlComponent implements java.io.Serializable{
	private static final long serialVersionUID = 1784126739852822273L;

	@Id
	@Column(name="ID_",length=60)
	private String id;
	
	@Column(name="URL_ID_",length=60)
	private String urlId;
	
	@Column(name="ROLE_ID_",length=60)
	private String roleId;
	
	@Column(name="AUTHORITY_TYPE_",length=10,nullable=false)
	@Enumerated(EnumType.STRING)
	private AuthorityType authorityType;	
	
	@ManyToOne(cascade=CascadeType.ALL,targetEntity=ComponentDefinition.class,fetch=FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="COMPONENT_ID_")
	private ComponentDefinition component;
	
	@Transient
	private Url url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public AuthorityType getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(AuthorityType authorityType) {
		this.authorityType = authorityType;
	}

	public ComponentDefinition getComponent() {
		return component;
	}

	public void setComponent(ComponentDefinition component) {
		this.component = component;
	}

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}
}
