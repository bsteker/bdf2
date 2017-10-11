/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bstek.bdf2.rapido.domain.Mapping;
import com.bstek.bdf2.rapido.manager.MappingManager;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.web.DoradoContext;

public class MappingPR {
	private MappingManager mappingManager;
	@DataProvider
	public Collection<Mapping> loadMappings(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId",DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"packageId"));
		return mappingManager.loadMappings(map);
	}
	@DataProvider
	public Collection<Mapping> loadMappingsByPackageId(Map<String,Object> map){
		Collection<Mapping> mappings = mappingManager.loadMappings(map);
		if(mappings != null){
			for(Mapping mapping : mappings){
				mapping.setIcon("dorado/res/icons/table_relationship.png");
			}
		}
		return mappings;
	}
	@DataResolver
	public void saveMappings(Collection<Mapping> mappings){
		for(Mapping mapping:mappings){
			EntityState state=EntityUtils.getState(mapping);
			if(state.equals(EntityState.NEW)){
				mappingManager.insertMapping(mapping);
			}
			if(state.equals(EntityState.MODIFIED)){
				mappingManager.updateMapping(mapping);
			}
			if(state.equals(EntityState.DELETED)){
				mappingManager.deleteMapping(mapping.getId());
			}
		}
	}
	
	public MappingManager getMappingManager() {
		return mappingManager;
	}

	public void setMappingManager(MappingManager mappingManager) {
		this.mappingManager = mappingManager;
	}
}
