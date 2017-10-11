package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_validator:字段验证器信息表
 */
@Entity
@Table(name = "bdf_r_validator")
public class BdfRValidator implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:验证器名称
	 */
	public String name;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * FIELD_ID_:可能是Mapping表的ID，也可能是实体字段表的ID
	 */
	public String fieldId;

	public BdfRValidator() {
		super();
	}

	public BdfRValidator(String id, String name, String desc, String fieldId) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.fieldId = fieldId;
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

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	@Column(name = "FIELD_ID_", length = 50)
	public String getFieldId() {
		return fieldId;
	}

	public String toString() {
		return "BdfRValidator [id=" + id + ",name=" + name + ",desc=" + desc
				+ ",fieldId=" + fieldId + "]";
	}

}
