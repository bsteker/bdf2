/*
 * This file is part of BDF
 * BDF��Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.KeyGenerateType;
import com.bstek.bdf2.rapido.domain.Validator;

public class EntityFieldManager extends RapidoJdbcDao{
	private MetadataManager metadataManager;
	private MappingManager mappingManager;
	private ValidatorManager validatorManager;
	private String querySQL="select ID_,NAME_,READ_ONLY_,SUBMITTABLE_,DESC_,METADATA_ID_,ENTITY_ID_,PRIMARY_KEY_,DATA_TYPE_,LABEL_,REQUIRED_,DEFAULT_VALUE_,DISPLAY_FORMAT_,KEY_GENERATE_TYPE_,KEY_GENERATOR_,MAPPING_ID_,TABLE_NAME_ from BDF_R_ENTITY_FIELD";
	public Collection<EntityField> loadFields(String entityId){
		return this.getJdbcTemplate().query(querySQL+" where ENTITY_ID_=?", new Object[]{entityId},new EntityFieldMapper());
	}
	
	public void insertEntityField(EntityField field){
		String sql="insert into BDF_R_ENTITY_FIELD(ID_,NAME_,READ_ONLY_,SUBMITTABLE_,DESC_,METADATA_ID_,ENTITY_ID_,PRIMARY_KEY_,DATA_TYPE_,LABEL_,REQUIRED_,DEFAULT_VALUE_,DISPLAY_FORMAT_,KEY_GENERATE_TYPE_,KEY_GENERATOR_,MAPPING_ID_,TABLE_NAME_) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		field.setId(UUID.randomUUID().toString());
		this.getJdbcTemplate().update(sql,new Object[]{field.getId(),field.getName(),field.isReadOnly()?"1":"0",field.isSubmittable()?"1":"0",field.getDesc(),field.getMetaData()!=null?field.getMetaData().getId():null,field.getEntityId(),field.isPrimaryKey()?"1":"0",field.getDataType(),field.getLabel(),field.isRequired()?"1":"0",field.getDefaultValue(),field.getDisplayFormat(),field.getKeyGenerateType()!=null?field.getKeyGenerateType().toString():null,field.getKeyGenerator(),field.getMapping()!=null?field.getMapping().getId():null,field.getTableName()});
	}
	
	public void updateEntityField(EntityField field){
		String sql="update BDF_R_ENTITY_FIELD set NAME_=?,READ_ONLY_=?,SUBMITTABLE_=?,DESC_=?,METADATA_ID_=?,ENTITY_ID_=?,PRIMARY_KEY_=?,DATA_TYPE_=?,LABEL_=?,REQUIRED_=?,DEFAULT_VALUE_=?,DISPLAY_FORMAT_=?,KEY_GENERATE_TYPE_=?,KEY_GENERATOR_=?,MAPPING_ID_=?,TABLE_NAME_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{field.getName(),field.isReadOnly()?"1":"0",field.isSubmittable()?"1":"0",field.getDesc(),field.getMetaData()!=null?field.getMetaData().getId():null,field.getEntityId(),field.isPrimaryKey()?"1":"0",field.getDataType(),field.getLabel(),field.isRequired()?"1":"0",field.getDefaultValue(),field.getDisplayFormat(),field.getKeyGenerateType()!=null?field.getKeyGenerateType().toString():null,field.getKeyGenerator(),field.getMapping()!=null?field.getMapping().getId():null,field.getTableName(),field.getId()});
	}
	
	public void deleteEntityFieldsByEntityId(String entityId){
		for(EntityField ef:this.loadFields(entityId)){
			this.deleteEntityField(ef.getId());
		}
	}
	
	public void deleteEntityField(String id){
		for(Validator v:this.validatorManager.loadValidators(id)){
			this.validatorManager.deleteValidator(v.getId());
		}
		String sql="delete from BDF_R_ENTITY_FIELD where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public MetadataManager getMetadataManager() {
		return metadataManager;
	}
	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}
	public MappingManager getMappingManager() {
		return mappingManager;
	}

	public void setMappingManager(MappingManager mappingManager) {
		this.mappingManager = mappingManager;
	}

	public ValidatorManager getValidatorManager() {
		return validatorManager;
	}

	public void setValidatorManager(ValidatorManager validatorManager) {
		this.validatorManager = validatorManager;
	}



	class EntityFieldMapper implements RowMapper<EntityField>{
		public EntityField mapRow(ResultSet rs, int rowNum) throws SQLException {
			EntityField f=new EntityField();
			f.setId(rs.getString(1));
			f.setName(rs.getString(2));
			f.setReadOnly((rs.getString(3)==null || rs.getString(3).equals("0"))?false:true);
			f.setSubmittable((rs.getString(4)==null || rs.getString(4).equals("0"))?false:true);
			f.setDesc(rs.getString(5));
			String metaDataId=rs.getString(6);
			if(StringUtils.hasText(metaDataId)){
				f.setMetaData(metadataManager.loadMetadataById(metaDataId));
			}
			f.setEntityId(rs.getString(7));
			f.setPrimaryKey((rs.getString(8)==null || rs.getString(8).equals("0"))?false:true);
			f.setDataType(rs.getString(9));
			f.setLabel(rs.getString(10));
			f.setRequired((rs.getString(11)==null || rs.getString(11).equals("0"))?false:true);
			f.setDefaultValue(rs.getString(12));
			f.setDisplayFormat(rs.getString(13));
			if(rs.getString(14)!=null){
				f.setKeyGenerateType(KeyGenerateType.valueOf(rs.getString(14)));				
			}
			f.setKeyGenerator(rs.getString(15));
			String mappingId=rs.getString(16);
			if(StringHelper.isNotEmpty(mappingId)){
				f.setMapping(mappingManager.loadMapping(mappingId));
			}
			f.setTableName(rs.getString(17));
			return f;
		}
	}
}
