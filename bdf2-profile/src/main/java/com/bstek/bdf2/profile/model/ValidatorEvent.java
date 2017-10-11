package com.bstek.bdf2.profile.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
@Entity
@Table(name="BDF2_PF_VALIDATOR_EVENT")
public class ValidatorEvent implements java.io.Serializable{
	private static final long serialVersionUID = 7791744202922587344L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="VALIDATOR_ID_",length=60)
	private String validatorId;
	@Column(name="CONTENT_",length=3000)
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getValidatorId() {
		return validatorId;
	}
	public void setValidatorId(String validatorId) {
		this.validatorId = validatorId;
	}
}
