package com.bstek.bdf2.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author Jacky.gao
 * @since 2013-3-24
 */
@Entity
@Table(name="BDF2_MESSAGE_TEMPLATE")
public class MessageTemplate  implements java.io.Serializable{
	private static final long serialVersionUID = -5768973498123490724L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="TYPE_",length=60)
	private String type;
	@Column(name="COMPANY_ID_",length=60)
	private String companyId;	
	@Column(name="CONTENT_",length=1000)
	private String content;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
