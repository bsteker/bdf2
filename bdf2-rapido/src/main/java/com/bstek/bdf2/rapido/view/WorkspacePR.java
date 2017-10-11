/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bstek.bdf2.rapido.domain.PackageInfo;
import com.bstek.bdf2.rapido.domain.PackageType;
import com.bstek.bdf2.rapido.manager.PackageManager;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;

public class WorkspacePR {
	private PackageManager packageManager;
	
	@DataProvider
	public Collection<PackageInfo> loadPackages(Map<String,Object> parameter) throws Exception{
		if(parameter==null){
			return buildRootPackage();
		}else{
			String type=(String)parameter.get("type");
			String parentId=(String)parameter.get("parentId");
			Collection<PackageInfo> result=packageManager.loadPackage(PackageType.valueOf(type), parentId);
			for(PackageInfo p:result){
				p.setIcon("dorado/res/icons/package.png");
			}
			return result;
		}
	}
	@DataProvider
	public Collection<Map<String,Object>> loadLayouts() throws Exception{
		Collection<Map<String,Object>> layouts=new ArrayList<Map<String,Object>>();
		Map<String,Object> layout=new HashMap<String,Object>();
		layout.put("key", "dock");
		layout.put("value", "dock");
		layouts.add(layout);
		
		layout=new HashMap<String,Object>();
		layout.put("key", "hbox");
		layout.put("value", "hbox");
		layouts.add(layout);
		
		layout=new HashMap<String,Object>();
		layout.put("key", "vbox");
		layout.put("value", "vbox");
		layouts.add(layout);
		
		layout=new HashMap<String,Object>();
		layout.put("key", "native");
		layout.put("value", "native");
		layouts.add(layout);
		
		layout=new HashMap<String,Object>();
		layout.put("key", "form");
		layout.put("value", "form");
		layouts.add(layout);
		
		layout=new HashMap<String,Object>();
		layout.put("key", "anchor");
		layout.put("value", "anchor");
		layouts.add(layout);
		return layouts;
	}
	
	@DataResolver
	public void savePackage(Collection<PackageInfo> packages) throws Exception{
		for(PackageInfo p:packages){
			EntityState state=EntityUtils.getState(p);
			if(state.equals(EntityState.NEW)){
				String[] names=p.getName().split("\\u002E");
				p.setName(names[0]);
				int i=0;
				String parentId=null;
				for(String name:names){
					PackageInfo subpack=new PackageInfo();
					if(parentId!=null && i>0){
						subpack.setParentId(parentId);
					}else{
						subpack.setParentId(p.getParentId());						
					}
					subpack.setDesc(p.getDesc());
					subpack.setType(p.getType());
					subpack.setName(name);
					packageManager.insertPackage(subpack);
					parentId=subpack.getId();
					if(i==0){
						p.setId(parentId);
					}
					i++;
				}
			}
			if(state.equals(EntityState.MODIFIED)){
				packageManager.updatePackage(p);
			}
			if(state.equals(EntityState.DELETED)){
				packageManager.deletePackage(p.getId());
			}
			if(p.getChildren()!=null){
				this.savePackage(p.getChildren());
			}
		}
	}
	

	private void setPackageIcon(PackageInfo p) {
		if(p.getType().equals(PackageType.action)){
			p.setIcon("dorado/res/icons/lightning.png");
		}else if(p.getType().equals(PackageType.component)){
			p.setIcon("dorado/res/icons/plugin.png");
		}else if(p.getType().equals(PackageType.entity)){
			p.setIcon("dorado/res/icons/table.png");
		}else if(p.getType().equals(PackageType.metadata)){
			p.setIcon("dorado/res/icons/brick.png");
		}else if(p.getType().equals(PackageType.page)){
			p.setIcon("dorado/res/icons/page_world.png");
		}else if(p.getType().equals(PackageType.parameter)){
			p.setIcon("dorado/res/icons/text_allcaps.png");
		}else if(p.getType().equals(PackageType.mapping)){
			p.setIcon("dorado/res/icons/table_relationship.png");
		}
	}
	
	private Collection<PackageInfo> buildRootPackage(){
		Collection<PackageInfo> result=new ArrayList<PackageInfo>();
		PackageInfo p=new PackageInfo();
		p.setType(PackageType.page);
		p.setName("页面");
		p.setDesc("页面");
		setPackageIcon(p);
		result.add(p);
		
		
		p=new PackageInfo();
		p.setType(PackageType.component);
		p.setName("组件");
		p.setDesc("组件");
		setPackageIcon(p);
		result.add(p);
		
		p=new PackageInfo();
		p.setType(PackageType.action);
		p.setName("动作");
		p.setDesc("动作");
		setPackageIcon(p);
		result.add(p);
		
		p=new PackageInfo();
		p.setType(PackageType.entity);
		p.setName("实体");
		p.setDesc("实体");
		setPackageIcon(p);
		result.add(p);
		
		p=new PackageInfo();
		p.setType(PackageType.mapping);
		p.setName("实体映射");
		p.setDesc("实体映射");
		setPackageIcon(p);
		result.add(p);
		
		p=new PackageInfo();
		p.setType(PackageType.metadata);
		p.setName("元数据");
		p.setDesc("元数据");
		setPackageIcon(p);
		result.add(p);
		
		return result;
	}
	
	public PackageManager getPackageManager() {
		return packageManager;
	}

	public void setPackageManager(PackageManager packageManager) {
		this.packageManager = packageManager;
	}
}
