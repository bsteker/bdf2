package com.bstek.bdf2.profile.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Jacky.gao
 * @since 2013-3-4
 */
@Entity
@Table(name="BDF2_PF_COMPONENT_SORT")
public class ComponentSort implements java.io.Serializable{
	private static final long serialVersionUID = 7780768761959526348L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="PARENT_COMPONENT_ID_",length=60)
	private String parentComponentId;	
	@Column(name="CONTROL_ID_",length=60)
	private String controlId;
	@Column(name="ORDER_")
	private int order;
	@Transient
	private String icon;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getParentComponentId() {
		return parentComponentId;
	}
	public void setParentComponentId(String parentComponentId) {
		this.parentComponentId = parentComponentId;
	}

	public String getControlId() {
		return controlId;
	}
	public void setControlId(String controlId) {
		this.controlId = controlId;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
