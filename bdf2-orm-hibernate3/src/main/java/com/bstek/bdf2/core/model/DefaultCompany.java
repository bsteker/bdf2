package com.bstek.bdf2.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @since 2013-1-27
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_COMPANY")
public class DefaultCompany implements java.io.Serializable{
	private static final long serialVersionUID = -6326282268106128108L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="DESC_",length=120)
	private String desc;
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
}
