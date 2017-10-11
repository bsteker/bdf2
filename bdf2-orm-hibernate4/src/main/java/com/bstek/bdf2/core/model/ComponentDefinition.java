package com.bstek.bdf2.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @since 2013-1-28
 * @author Jacky.gao
 */
@Entity
@Table(name="BDF2_COMPONENT")
public class ComponentDefinition implements java.io.Serializable{
	private static final long serialVersionUID = 4314003600468849873L;

	@Id
	@Column(name="ID_",length=60)
	private String id;
	
	@Column(name="COMPONENT_ID_",length=60,nullable=false)
	private String componentId;
	
	@Column(name="DESC_",length=120)
	private String desc;
	
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
