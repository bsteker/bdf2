package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_field_metadata:字段元数据信息表
 */
@Entity
@Table(name = "bdf_r_field_metadata")
public class BdfRFieldMetadata implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:字段名
	 */
	public String name;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * DEFAULT_VALUE_:默认值
	 */
	public String defaultValue;

	/**
	 * DISPLAY_FORMAT_:显示格式
	 */
	public String displayFormat;

	/**
	 * REQUIRED_:是否为必须
	 */
	public String required;

	/**
	 * LABEL_:字段标题
	 */
	public String label;

	/**
	 * PACKAGE_ID_:所在包
	 */
	public String packageId;

	/**
	 * MAPPING_:数据映射
	 */
	public String mapping;

	public BdfRFieldMetadata() {
		super();
	}

	public BdfRFieldMetadata(String id, String name, String desc,
			String defaultValue, String displayFormat, String required,
			String label, String packageId, String mapping) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.defaultValue = defaultValue;
		this.displayFormat = displayFormat;
		this.required = required;
		this.label = label;
		this.packageId = packageId;
		this.mapping = mapping;
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

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name = "DEFAULT_VALUE_", length = 50)
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}

	@Column(name = "DISPLAY_FORMAT_", length = 50)
	public String getDisplayFormat() {
		return displayFormat;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	@Column(name = "REQUIRED_", length = 1)
	public String getRequired() {
		return required;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "LABEL_", length = 100)
	public String getLabel() {
		return label;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_ID_", length = 50)
	public String getPackageId() {
		return packageId;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	@Column(name = "MAPPING_", length = 50)
	public String getMapping() {
		return mapping;
	}

	public String toString() {
		return "BdfRFieldMetadata [id=" + id + ",name=" + name + ",desc="
				+ desc + ",defaultValue=" + defaultValue + ",displayFormat="
				+ displayFormat + ",required=" + required + ",label=" + label
				+ ",packageId=" + packageId + ",mapping=" + mapping + "]";
	}

}
