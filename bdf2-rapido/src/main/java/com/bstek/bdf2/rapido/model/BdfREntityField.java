package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_entity_field:实体字段信息表
 */
@Entity
@Table(name = "bdf_r_entity_field")
public class BdfREntityField implements Serializable {

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
	 * READ_ONLY_:0表示只读，1表示非只读
	 */
	public String readOnly;

	/**
	 * SUBMITTABLE_:是否要提交数据到后台
	 */
	public String submittable;

	/**
	 * ENTITY_ID_:所属实体对象
	 */
	public String entityId;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * METADATA_ID_:采用的元数据对象
	 */
	public String metadataId;

	/**
	 * TABLE_NAME_:隶属表
	 */
	public String tableName;

	/**
	 * PRIMARY_KEY_:是否为主键
	 */
	public String primaryKey;

	/**
	 * KEY_GENERATE_TYPE_:生成方式有:custom、autoincrement、sequence
	 */
	public String keyGenerateType;

	/**
	 * KEY_GENERATOR_:可能是具体的EL表达式或一个具体的sequence对象
	 */
	public String keyGenerator;

	/**
	 * MAPPING_ID_:采用的Mapping
	 */
	public String mappingId;

	/**
	 * DATA_TYPE_:数据类型
	 */
	public String dataType;

	/**
	 * LABEL_:字段标题
	 */
	public String label;

	/**
	 * REQUIRED_:是否为必须
	 */
	public String required;

	/**
	 * DEFAULT_VALUE_:默认值
	 */
	public String defaultValue;

	/**
	 * DISPLAY_FORMAT_:显示格式
	 */
	public String displayFormat;

	public BdfREntityField() {
		super();
	}

	public BdfREntityField(String id, String name, String readOnly,
			String submittable, String entityId, String desc,
			String metadataId, String tableName, String primaryKey,
			String keyGenerateType, String keyGenerator, String mappingId,
			String dataType, String label, String required,
			String defaultValue, String displayFormat) {
		super();
		this.id = id;
		this.name = name;
		this.readOnly = readOnly;
		this.submittable = submittable;
		this.entityId = entityId;
		this.desc = desc;
		this.metadataId = metadataId;
		this.tableName = tableName;
		this.primaryKey = primaryKey;
		this.keyGenerateType = keyGenerateType;
		this.keyGenerator = keyGenerator;
		this.mappingId = mappingId;
		this.dataType = dataType;
		this.label = label;
		this.required = required;
		this.defaultValue = defaultValue;
		this.displayFormat = displayFormat;
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

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	@Column(name = "READ_ONLY_", length = 1)
	public String getReadOnly() {
		return readOnly;
	}

	public void setSubmittable(String submittable) {
		this.submittable = submittable;
	}

	@Column(name = "SUBMITTABLE_", length = 1)
	public String getSubmittable() {
		return submittable;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Column(name = "ENTITY_ID_", length = 50)
	public String getEntityId() {
		return entityId;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	@Column(name = "METADATA_ID_", length = 50)
	public String getMetadataId() {
		return metadataId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "TABLE_NAME_", length = 100)
	public String getTableName() {
		return tableName;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "PRIMARY_KEY_", length = 1)
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setKeyGenerateType(String keyGenerateType) {
		this.keyGenerateType = keyGenerateType;
	}

	@Column(name = "KEY_GENERATE_TYPE_", length = 20)
	public String getKeyGenerateType() {
		return keyGenerateType;
	}

	public void setKeyGenerator(String keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	@Column(name = "KEY_GENERATOR_", length = 100)
	public String getKeyGenerator() {
		return keyGenerator;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	@Column(name = "MAPPING_ID_", length = 50)
	public String getMappingId() {
		return mappingId;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "DATA_TYPE_", length = 50)
	public String getDataType() {
		return dataType;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "LABEL_", length = 50)
	public String getLabel() {
		return label;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	@Column(name = "REQUIRED_", length = 1)
	public String getRequired() {
		return required;
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

	public String toString() {
		return "BdfREntityField [id=" + id + ",name=" + name + ",readOnly="
				+ readOnly + ",submittable=" + submittable + ",entityId="
				+ entityId + ",desc=" + desc + ",metadataId=" + metadataId
				+ ",tableName=" + tableName + ",primaryKey=" + primaryKey
				+ ",keyGenerateType=" + keyGenerateType + ",keyGenerator="
				+ keyGenerator + ",mappingId=" + mappingId + ",dataType="
				+ dataType + ",label=" + label + ",required=" + required
				+ ",defaultValue=" + defaultValue + ",displayFormat="
				+ displayFormat + "]";
	}

}
