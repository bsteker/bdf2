package com.bstek.bdf2.profile.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="BDF2_PF_VALIDATOR")
public class ValidatorDef implements java.io.Serializable{
	private static final long serialVersionUID = -6861836476796716424L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=60)
	private String name;
	@Column(name="DESC_",length=120)
	private String desc;
	@Column(name="TYPE_",length=60)
	private String type;
	
	@Transient
	private List<ValidatorEvent> events;
	
	@Transient
	private List<ValidatorProperty> properties;
	
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<ValidatorEvent> getEvents() {
		return events;
	}
	public void setEvents(List<ValidatorEvent> events) {
		this.events = events;
	}
	public List<ValidatorProperty> getProperties() {
		return properties;
	}
	public void setProperties(List<ValidatorProperty> properties) {
		this.properties = properties;
	}
}
