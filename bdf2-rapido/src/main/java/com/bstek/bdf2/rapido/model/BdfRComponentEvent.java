package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_component_event:组件的事件信息表
 */
@Entity
@Table(name = "bdf_r_component_event")
public class BdfRComponentEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:事件名称
	 */
	public String name;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * SCRIPT_:事件脚本内容
	 */
	public String script;

	/**
	 * COMPONENT_ID_:所属组件
	 */
	public String componentId;

	public BdfRComponentEvent() {
		super();
	}

	public BdfRComponentEvent(String id, String name, String desc,
			String script, String componentId) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.script = script;
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

	@Column(name = "NAME_", length = 50)
	public String getName() {
		return name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Column(name = "SCRIPT_", length = 2000)
	public String getScript() {
		return script;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Column(name = "COMPONENT_ID_", length = 50)
	public String getComponentId() {
		return componentId;
	}

	public String toString() {
		return "BdfRComponentEvent [id=" + id + ",name=" + name + ",desc="
				+ desc + ",script=" + script + ",componentId=" + componentId
				+ "]";
	}

}
