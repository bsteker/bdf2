package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_layout_constraint_prop:组件约束属性信息表
 */
@Entity
@Table(name = "bdf_r_layout_constraint_prop")
public class BdfRLayoutConstraintProp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:约束属性名称
	 */
	public String name;

	/**
	 * VALUE_:约束属性值
	 */
	public String value;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * COMPONENT_ID_:该字段中存储的是BDF_R_COMPONENT表主键值或BDF_R_PAGE_COMPONENT表主键值或BDF_RG_PAGE_COMPONENT表主键值
	 */
	public String componentId;

	public BdfRLayoutConstraintProp() {
		super();
	}

	public BdfRLayoutConstraintProp(String id, String name, String value,
			String desc, String componentId) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.desc = desc;
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

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "VALUE_", length = 20)
	public String getValue() {
		return value;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Column(name = "COMPONENT_ID_", length = 50)
	public String getComponentId() {
		return componentId;
	}

	public String toString() {
		return "BdfRLayoutConstraintProp [id=" + id + ",name=" + name
				+ ",value=" + value + ",desc=" + desc + ",componentId="
				+ componentId + "]";
	}

}
