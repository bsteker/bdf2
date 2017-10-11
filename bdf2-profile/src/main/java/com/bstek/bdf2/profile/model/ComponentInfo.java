package com.bstek.bdf2.profile.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Jacky.gao
 * @since 2013-2-26
 */
@Entity
@Table(name="BDF2_PF_COMPONENT")
public class ComponentInfo implements java.io.Serializable{
	private static final long serialVersionUID = -624288206190769010L;
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="URL_",length=120)
	private String url;
	@Column(name="CONTROL_ID_",length=120)
	private String controlId;
	@Column(name="ASSIGN_TARGET_ID_",length=60)
	private String assignTargetId;
	@Column(name="TYPE_",length=30)
	private String type;
	
	@Transient
	private List<ComponentProperty> properties;
	@Transient
	private List<ComponentEvent> events;
	@Transient
	private List<ComponentSort> sorts;
	@Transient
	private List<ComponentValidator> validators;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getControlId() {
		return controlId;
	}
	public void setControlId(String controlId) {
		this.controlId = controlId;
	}
	public String getAssignTargetId() {
		return assignTargetId;
	}
	public void setAssignTargetId(String assignTargetId) {
		this.assignTargetId = assignTargetId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ComponentProperty> getProperties() {
		return properties;
	}
	public void setProperties(List<ComponentProperty> properties) {
		this.properties = properties;
	}
	public List<ComponentEvent> getEvents() {
		return events;
	}
	public void setEvents(List<ComponentEvent> events) {
		this.events = events;
	}
	public List<ComponentSort> getSorts() {
		return sorts;
	}
	public void setSorts(List<ComponentSort> sorts) {
		this.sorts = sorts;
	}
	public List<ComponentValidator> getValidators() {
		return validators;
	}
	public void setValidators(List<ComponentValidator> validators) {
		this.validators = validators;
	}
}
