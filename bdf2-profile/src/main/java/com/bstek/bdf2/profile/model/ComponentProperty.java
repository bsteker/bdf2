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
@Table(name="BDF2_PF_COMPONENT_PROPERTY")
public class ComponentProperty implements java.io.Serializable{
	private static final long serialVersionUID = 2088781949616655824L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="NAME_",length=120)
	private String name;
	@Column(name="VALUE_",length=1200)
	private String value;
	@Column(name="COMPONENT_ID_",length=60)
	private String componentId;
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
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
}
