/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bstek.bdf2.rapido.domain.MetaData;
import com.bstek.bdf2.rapido.domain.PackageInfo;
import com.bstek.bdf2.rapido.domain.PackageType;
import com.bstek.bdf2.rapido.domain.Validator;
import com.bstek.bdf2.rapido.domain.ValidatorProperty;
import com.bstek.bdf2.rapido.manager.MetadataManager;
import com.bstek.bdf2.rapido.manager.PackageManager;
import com.bstek.bdf2.rapido.manager.ValidatorManager;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.web.DoradoContext;

public class MetadataPR {
	private MetadataManager metadataManager;
	private PackageManager packageManager;
	private ValidatorManager validatorManager;
	
	@DataProvider
	public void loadMetadata(Page<MetaData> page){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId",DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"packageId"));
		metadataManager.loadMetadataByPage(page, map);
	}
	
	@DataProvider
	public Collection<PackageInfo> loadMappingPackages(String parentId) throws Exception{
		Collection<PackageInfo> result=packageManager.loadPackage(PackageType.mapping, parentId);
		for(PackageInfo p:result){
			p.setIcon("dorado/res/icons/package.png");
		}
		return result;
	}
	
	@DataProvider
	public Collection<Validator> loadValidatorsByFieldId(String fieldId){
		return validatorManager.loadValidators(fieldId);
	}
	
	@DataResolver
	public void saveMetadata(Collection<MetaData> metadatas){
		for(MetaData metadata : metadatas){
			EntityState state=EntityUtils.getState(metadata);
			if(state.equals(EntityState.NEW)){
				metadata.setPackageId((String)DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"packageId"));
				metadataManager.insertMetadata(metadata);
			}
			if(state.equals(EntityState.MODIFIED)){
				metadataManager.updateMetadata(metadata);
			}
			if(state.equals(EntityState.DELETED)){
				metadataManager.deleteMetadata(metadata.getId());
			}
			if(metadata.getValidators() != null){
				this.saveValidators(metadata);
			}
		}
	}
	
	@DataResolver
	public void saveValidators(MetaData metadata){
		for (Validator validator : metadata.getValidators()) {
			EntityState state = EntityUtils.getState(validator);
			if (state.equals(EntityState.NEW)) {
				validator.setFieldId(metadata.getId());
				validatorManager.insertValidator(validator);
			}
			if (state.equals(EntityState.MODIFIED)) {
				validatorManager.updateValidator(validator);
			}
			if (state.equals(EntityState.DELETED)) {
				validatorManager.deleteValidator(validator.getId());
			}

			if (validator.getProperties() != null) {
				for (ValidatorProperty vp : validator.getProperties()) {
					vp.setValidatorId(validator.getId());
					EntityState entityState = EntityUtils.getState(vp);
					if (entityState.equals(EntityState.NEW)) {
						validatorManager.insertValidatorProperty(vp);
					}
					if (entityState.equals(EntityState.MODIFIED)) {
						validatorManager.updateValidatorProperty(vp);
					}
					if (entityState.equals(EntityState.DELETED)) {
						validatorManager.deleteValidatorProperty(vp.getId());
					}
				}
			}
		}
	}
	
	public MetadataManager getMetadataManager() {
		return metadataManager;
	}

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}
	public PackageManager getPackageManager() {
		return packageManager;
	}
	public void setPackageManager(PackageManager packageManager) {
		this.packageManager = packageManager;
	}

	public ValidatorManager getValidatorManager() {
		return validatorManager;
	}

	public void setValidatorManager(ValidatorManager validatorManager) {
		this.validatorManager = validatorManager;
	}
}
