/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.MetaData;
import com.bstek.bdf2.rapido.domain.Validator;
import com.bstek.bdf2.rapido.domain.ValidatorProperty;
import com.bstek.dorado.data.provider.Page;

public class MetadataManager extends RapidoJdbcDao{
	
	private MappingManager mappingManager;
	private ValidatorManager validatorManager;
	public MetaData loadMetadataById(String id){
		String querySql= "select ID_,NAME_,DESC_,DEFAULT_VALUE_,DISPLAY_FORMAT_,REQUIRED_,LABEL_,PACKAGE_ID_,MAPPING_ from BDF_R_FIELD_METADATA";
		List<MetaData> list=this.getJdbcTemplate().query(querySql+" where ID_=?",new Object[]{id},new MetadataMapper());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public void loadMetadataByPage(Page<MetaData> page, Map<String,Object> map){
		String querySql= "select ID_,NAME_,DESC_,DEFAULT_VALUE_,DISPLAY_FORMAT_,REQUIRED_,LABEL_,PACKAGE_ID_,MAPPING_ from BDF_R_FIELD_METADATA where 1=1 ";
		//String countSql = "select count(*) from BDF_R_FIELD_METADATA where 1=1 ";
		StringBuilder whereSql = new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		if(map.containsKey("packageId")){
			whereSql.append(" and PACKAGE_ID_=?");
			params.add(map.get("packageId"));
		}
		
		this.pagingQuery(page,querySql + whereSql.toString(), params.toArray(),new MetadataMapper());
		/*Pagination<MetaData> pagination = new Pagination<MetaData>(page.getPageNo(),page.getPageSize());
		this.paginationQuery(querySql + whereSql.toString(), countSql + whereSql.toString(), params.toArray(), pagination, new MetadataMapper());
		page.setEntities(pagination.getResults());
		page.setEntityCount(pagination.getTotalCount());*/
	}
	
	public void deleteMetadataByPackageId(String packageId){
		Map<String,Object> map=new HashMap<String,Object>();
		Page<MetaData> page=new Page<MetaData>(1000000000,1);
		map.put("packageId", packageId);
		this.loadMetadataByPage(page, map);
		for(MetaData md:page.getEntities()){
			this.deleteMetadata(md.getId());
		}
	}
	
	public void insertMetadata(MetaData metadata){
		metadata.setId(UUID.randomUUID().toString());
		String insertSql = "insert into BDF_R_FIELD_METADATA(ID_,NAME_,DESC_,DEFAULT_VALUE_,DISPLAY_FORMAT_,REQUIRED_,LABEL_,PACKAGE_ID_,MAPPING_)"
							+ " values(?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(insertSql,new Object[]{metadata.getId(), metadata.getName(), metadata.getDesc(), metadata.getDefaultValue(),
							metadata.getDisplayFormat(),metadata.getRequired() == false?"0":"1",
							metadata.getLabel(),metadata.getPackageId(),(metadata.getMapping() == null) ? null : metadata.getMapping().getId()});
	}
	
	public void updateMetadata(MetaData metadata){
		String updateSql = "update BDF_R_FIELD_METADATA set NAME_ = ?,DESC_ = ?,DEFAULT_VALUE_ = ?,DISPLAY_FORMAT_ = ?,REQUIRED_ = ?,LABEL_ = ?,PACKAGE_ID_ = ?,MAPPING_ = ? where ID_ = ?";
		this.getJdbcTemplate().update(updateSql,new Object[]{metadata.getName(), metadata.getDesc(), metadata.getDefaultValue(),
				metadata.getDisplayFormat(),metadata.getRequired() == false?"0":"1",
						metadata.getLabel(),metadata.getPackageId(),metadata.getMapping() == null? null : metadata.getMapping().getId(),metadata.getId()});
	}
	
	public void deleteMetadata(String id){
		for(Validator v:this.validatorManager.loadValidators(id)){
			for(ValidatorProperty vp:this.validatorManager.loadValidatorProperties(v.getId())){
				this.validatorManager.deleteValidatorProperty(vp.getId());
			}
			this.validatorManager.deleteValidator(v.getId());
		}
		String deleteSql = "delete from BDF_R_FIELD_METADATA where ID_ = ?";
		this.getJdbcTemplate().update(deleteSql,new Object[]{id});
	}
	
	
	public MappingManager getMappingManager() {
		return mappingManager;
	}
	
	public void setMappingManager(MappingManager mappingManager) {
		this.mappingManager = mappingManager;
	}
	
	class MetadataMapper implements RowMapper<MetaData>{
		public MetaData mapRow(ResultSet rs, int rowNum) throws SQLException {
			MetaData m=new MetaData();
			m.setId(rs.getString("ID_"));
			m.setName(rs.getString("NAME_"));
			m.setDesc(rs.getString("DESC_"));
			m.setDefaultValue(rs.getString("DEFAULT_VALUE_"));
			m.setLabel(rs.getString("LABEL_"));
			m.setPackageId(rs.getString("PACKAGE_ID_"));
			m.setDisplayFormat(rs.getString("DISPLAY_FORMAT_"));
			m.setRequired((rs.getString("REQUIRED_") == null || rs.getString("REQUIRED_").equals("0"))?false:true);
			String mappingId=rs.getString("MAPPING_");
			if(StringUtils.hasText(mappingId)){
				m.setMapping(mappingManager.loadMapping(mappingId));
			}
			return m;
		}
		
	}

	public ValidatorManager getValidatorManager() {
		return validatorManager;
	}

	public void setValidatorManager(ValidatorManager validatorManager) {
		this.validatorManager = validatorManager;
	}
}
