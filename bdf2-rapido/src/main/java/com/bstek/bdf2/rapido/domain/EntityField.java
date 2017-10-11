/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.domain;

import java.util.Collection;

public class EntityField {
	private String id;
	private String name;
	private boolean readOnly;
	private boolean submittable=true;
	private boolean primaryKey;
	private String entityId;
	private String desc;
	private Mapping mapping;
	private String tableName;
	private String metaDataId;
	private MetaData metaData;
	private String dataType;
	private String label;
	private boolean required;
	private String defaultValue;
	private String displayFormat;
	private KeyGenerateType keyGenerateType;
	private String keyGenerator;
	private Collection<Validator> validators;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public boolean isSubmittable() {
		return submittable;
	}
	public void setSubmittable(boolean submittable) {
		this.submittable = submittable;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Mapping getMapping() {
		return mapping;
	}
	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}
	public MetaData getMetaData() {
		return metaData;
	}
	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getMetaDataId() {
		return metaDataId;
	}
	public void setMetaDataId(String metaDataId) {
		this.metaDataId = metaDataId;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDisplayFormat() {
		return displayFormat;
	}
	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}
	public Collection<Validator> getValidators() {
		return validators;
	}
	public void setValidators(Collection<Validator> validators) {
		this.validators = validators;
	}
	public KeyGenerateType getKeyGenerateType() {
		return keyGenerateType;
	}
	public void setKeyGenerateType(KeyGenerateType keyGenerateType) {
		this.keyGenerateType = keyGenerateType;
	}
	public String getKeyGenerator() {
		return keyGenerator;
	}
	public void setKeyGenerator(String keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
}
