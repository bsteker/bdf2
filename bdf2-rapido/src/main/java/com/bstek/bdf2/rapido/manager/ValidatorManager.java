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
import java.util.Collection;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.Validator;
import com.bstek.bdf2.rapido.domain.ValidatorProperty;

public class ValidatorManager extends RapidoJdbcDao{
	public Collection<Validator> loadValidators(final String fieldId){
		String sql="select ID_,NAME_,DESC_ from BDF_R_VALIDATOR where FIELD_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{fieldId},new RowMapper<Validator>(){
			public Validator mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Validator v=new Validator();
				v.setId(rs.getString(1));
				v.setName(rs.getString(2));
				v.setDesc(rs.getString(3));
				v.setFieldId(fieldId);
				return v;
			}
		});
	}
	
	public void insertValidator(Validator v){
		v.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_VALIDATOR(ID_,NAME_,DESC_,FIELD_ID_) values(?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{v.getId(),v.getName(),v.getDesc(),v.getFieldId()});
	}
	public void updateValidator(Validator v){
		String sql="update BDF_R_VALIDATOR set NAME_=?,DESC_=?,FIELD_ID_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{v.getName(),v.getDesc(),v.getFieldId(),v.getId()});
	}
	public void deleteValidator(String id){
		String sql="delete from BDF_R_VALIDATOR_PROPERTY where VALIDATOR_ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
		sql="delete from BDF_R_VALIDATOR where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	public void deleteValidatorProperty(String id){
		String sql="delete from BDF_R_VALIDATOR_PROPERTY where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public Collection<ValidatorProperty> loadValidatorProperties(final String validatorId){
		String sql="select ID_,NAME_,VALUE_ from BDF_R_VALIDATOR_PROPERTY where VALIDATOR_ID_=?";
		return this.getJdbcTemplate().query(sql,new Object[]{validatorId},new RowMapper<ValidatorProperty>(){
			public ValidatorProperty mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				ValidatorProperty vp=new ValidatorProperty();
				vp.setId(rs.getString(1));
				vp.setName(rs.getString(2));
				vp.setValue(rs.getString(3));
				vp.setValidatorId(validatorId);
				return vp;
			}
		});
	}
	public void updateValidatorProperty(ValidatorProperty vp){
		String sql="update BDF_R_VALIDATOR_PROPERTY set NAME_=?,VALUE_=? where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{vp.getName(),vp.getValue(),vp.getId()});
	}
	public void insertValidatorProperty(ValidatorProperty vp){
		vp.setId(UUID.randomUUID().toString());
		String sql="insert into BDF_R_VALIDATOR_PROPERTY(ID_,NAME_,VALUE_,VALIDATOR_ID_) values(?,?,?,?)";
		this.getJdbcTemplate().update(sql,new Object[]{vp.getId(),vp.getName(),vp.getValue(),vp.getValidatorId()});
	}
}
