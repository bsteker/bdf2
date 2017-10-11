package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_component_property:组件的属性信息
 */
@Entity
@Table(name = "bdf_r_component_property")
public class BdfRComponentProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:属性名称
	 */
	public String name;

	/**
	 * VALUE_:属性值
	 */
	public String value;

	/**
	 * COMPONENT_ID_:隶属组件
	 */
	public String componentId;

	public BdfRComponentProperty() {
		super();
	}

	public BdfRComponentProperty(String id, String name, String value,
			String componentId) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.componentId = componentId;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "NAME_", length = 100)
	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "VALUE_", length = 250)
	public String getValue() {
		return value;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Column(name = "COMPONENT_ID_", length = 50)
	public String getComponentId() {
		return componentId;
	}

	public String toString() {
		return "BdfRComponentProperty [id=" + id + ",name=" + name + ",value="
				+ value + ",componentId=" + componentId + "]";
	}

}
