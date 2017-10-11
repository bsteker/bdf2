package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_mapping:值映射信息表
 */
@Entity
@Table(name = "bdf_r_mapping")
public class BdfRMapping implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:名称
	 */
	public String name;

	/**
	 * SOURCE_:custom表示用户自定义；table表示数据库中的表
	 */
	public String source;

	/**
	 * VALUE_FIELD_:用于显示的字段名
	 */
	public String valueField;

	/**
	 * KEY_FIELD_:用于实际值的字段名
	 */
	public String keyField;

	/**
	 * QUERY_SQL_:查询表中键值所用SQL
	 */
	public String querySql;

	/**
	 * CUSTOM_KEY_VALUE_:格式为：key1=value1;key2=value2
	 */
	public String customKeyValue;

	/**
	 * PACKAGE_ID_:所在包
	 */
	public String packageId;

	/**
	 * PROPERTY_:下拉框值回填属性名
	 */
	public String property;

	public BdfRMapping() {
		super();
	}

	public BdfRMapping(String id, String name, String source,
			String valueField, String keyField, String querySql,
			String customKeyValue, String packageId, String property) {
		super();
		this.id = id;
		this.name = name;
		this.source = source;
		this.valueField = valueField;
		this.keyField = keyField;
		this.querySql = querySql;
		this.customKeyValue = customKeyValue;
		this.packageId = packageId;
		this.property = property;
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

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "SOURCE_", length = 10)
	public String getSource() {
		return source;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	@Column(name = "VALUE_FIELD_", length = 100)
	public String getValueField() {
		return valueField;
	}

	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

	@Column(name = "KEY_FIELD_", length = 100)
	public String getKeyField() {
		return keyField;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	@Column(name = "QUERY_SQL_", length = 250)
	public String getQuerySql() {
		return querySql;
	}

	public void setCustomKeyValue(String customKeyValue) {
		this.customKeyValue = customKeyValue;
	}

	@Column(name = "CUSTOM_KEY_VALUE_", length = 250)
	public String getCustomKeyValue() {
		return customKeyValue;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_ID_", length = 50)
	public String getPackageId() {
		return packageId;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Column(name = "PROPERTY_", length = 100)
	public String getProperty() {
		return property;
	}

	public String toString() {
		return "BdfRMapping [id=" + id + ",name=" + name + ",source=" + source
				+ ",valueField=" + valueField + ",keyField=" + keyField
				+ ",querySql=" + querySql + ",customKeyValue=" + customKeyValue
				+ ",packageId=" + packageId + ",property=" + property + "]";
	}

}
