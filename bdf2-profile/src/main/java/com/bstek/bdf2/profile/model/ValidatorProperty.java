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
@Table(name="BDF2_PF_VALIDATOR_PROPERTY")
public class ValidatorProperty implements java.io.Serializable{
	private static final long serialVersionUID = -3399044775814129057L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="VALIDATOR_ID_",length=60)
	private String validatorId;
	@Column(name="NAME_",length=120)
	private String name;
	@Column(name="VALUE_",length=1200)
	private String value;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValidatorId() {
		return validatorId;
	}
	public void setValidatorId(String validatorId) {
		this.validatorId = validatorId;
	}
}
