package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_validator_property:验证器属性信息表
 */
@Entity
@Table(name = "bdf_r_validator_property")
public class BdfRValidatorProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:属性名
	 */
	public String name;

	/**
	 * VALUE_:属性值
	 */
	public String value;

	/**
	 * VALIDATOR_ID_:隶属验证器
	 */
	public String validatorId;

	public BdfRValidatorProperty() {
		super();
	}

	public BdfRValidatorProperty(String id, String name, String value,
			String validatorId) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.validatorId = validatorId;
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

	@Column(name = "VALUE_", length = 200)
	public String getValue() {
		return value;
	}

	public void setValidatorId(String validatorId) {
		this.validatorId = validatorId;
	}

	@Column(name = "VALIDATOR_ID_", length = 50)
	public String getValidatorId() {
		return validatorId;
	}

	public String toString() {
		return "BdfRValidatorProperty [id=" + id + ",name=" + name + ",value="
				+ value + ",validatorId=" + validatorId + "]";
	}

}
