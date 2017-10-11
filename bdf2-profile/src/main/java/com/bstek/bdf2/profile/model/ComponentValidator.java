package com.bstek.bdf2.profile.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="BDF2_PF_COMPONENT_VALIDATOR")
public class ComponentValidator implements java.io.Serializable{
	private static final long serialVersionUID = 8011049620204640408L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="COMPONENT_ID_",length=60)
	private String componentId;
	
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,targetEntity=ValidatorDef.class)
	@JoinColumn(name="VALIDATOR_ID_")
	private ValidatorDef validator;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public ValidatorDef getValidator() {
		return validator;
	}
	public void setValidator(ValidatorDef validator) {
		this.validator = validator;
	}
}
