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

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.domain.PackageInfo;
import com.bstek.bdf2.rapido.domain.PackageType;

/**
 * 实现对BDF_R_PACKAGE_INFO表数据维护
 * @author jacky.gao@bstek.com
 * @since 2012-6-3
 */
public class PackageManager extends RapidoJdbcDao{
	private ComponentManager componentManager;
	private PageManager pageManager;
	private EntityManager entityManager;
	private ActionDefManager actionDefManager;
	private MappingManager mappingManager;
	private MetadataManager metadataManager;
	/**
	 * @param type
	 * @param parentId
	 * @return 返回取到的PackageInfo对象
	 */
	public Collection<PackageInfo> loadPackage(PackageType type,String parentId){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();
		}
		String sql="select ID_,NAME_,PARENT_ID_,TYPE_,DESC_ from BDF_R_PACKAGE_INFO where COMPANY_ID_=? and TYPE_=?";
		if(parentId!=null){
			sql+=" and PARENT_ID_ = ?";
			return this.getJdbcTemplate().query(sql, new Object[]{companyId,type.toString(),parentId}, new PackageMapper());
		}else {
			sql+=" and PARENT_ID_ is null";			
			return this.getJdbcTemplate().query(sql, new Object[]{companyId,type.toString()}, new PackageMapper());
		}
	}
	public Collection<PackageInfo> loadPackageByParentId(String parentId){
		String sql="select ID_,NAME_,PARENT_ID_,TYPE_,DESC_ from BDF_R_PACKAGE_INFO where PARENT_ID_=?";
		return this.getJdbcTemplate().query(sql, new Object[]{parentId}, new PackageMapper());
	}
	
	public PackageInfo loadPackageInfo(String id){
		String sql="select ID_,NAME_,PARENT_ID_,TYPE_,DESC_ from BDF_R_PACKAGE_INFO where ID_=?";
		return this.getJdbcTemplate().queryForObject(sql, new Object[]{id}, new PackageMapper());
	}
	
	/**
	 * @param p 要插入的Package对象
	 * @return
	 */
	public String insertPackage(PackageInfo p){
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first!");
		}
		String id=UUID.randomUUID().toString();
		p.setId(id);			
		String sql="insert into BDF_R_PACKAGE_INFO(ID_,NAME_,PARENT_ID_,TYPE_,DESC_,COMPANY_ID_) values(?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, new Object[]{id,p.getName(),p.getParentId(),p.getType().toString(),p.getDesc(),user.getCompanyId()});
		return id;
	}
	

	public void updatePackage(PackageInfo p){
		String sql="update BDF_R_PACKAGE_INFO set NAME_=?,PARENT_ID_=?,DESC_=? where ID_=?";
		this.getJdbcTemplate().update(sql, new Object[]{p.getName(),p.getParentId(),p.getDesc(),p.getId()});
	}
	

	public void deletePackage(String id){
		entityManager.deleteEntityByPackageId(id);
		pageManager.deletePageByPackageId(id);
		componentManager.deleteComponentsByPackageId(id);
		actionDefManager.deleteActionDefByPackageId(id);
		mappingManager.deleteMappingByPackageId(id);
		metadataManager.deleteMetadataByPackageId(id);
		for(PackageInfo p:this.loadPackageByParentId(id)){
			this.deletePackage(p.getId());
		}
		String sql="delete from BDF_R_PACKAGE_INFO where ID_=?";
		this.getJdbcTemplate().update(sql,new Object[]{id});
	}
	
	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ActionDefManager getActionDefManager() {
		return actionDefManager;
	}

	public void setActionDefManager(ActionDefManager actionDefManager) {
		this.actionDefManager = actionDefManager;
	}

	public MappingManager getMappingManager() {
		return mappingManager;
	}

	public void setMappingManager(MappingManager mappingManager) {
		this.mappingManager = mappingManager;
	}

	public MetadataManager getMetadataManager() {
		return metadataManager;
	}
	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	class PackageMapper implements RowMapper<PackageInfo>{
		public PackageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			PackageInfo p=new PackageInfo();
			p.setId(rs.getString(1));
			p.setName(rs.getString(2));
			p.setParentId(rs.getString(3));
			p.setType(PackageType.valueOf(rs.getString(4)));
			p.setDesc(rs.getString(5));
			return p;
		}
	}
}
