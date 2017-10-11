package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_entity:对应数据库中定义的表或视图
 */
@Entity
@Table(name = "bdf_r_entity")
public class BdfREntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:实体名称
	 */
	public String name;

	/**
	 * TABLE_NAME_:表示该实体对象要操作的主表名称
	 */
	public String tableName;

	/**
	 * RECURSIVE_:是否为递归结构
	 */
	public String recursive;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * QUERY_SQL_:查询用的SQL
	 */
	public String querySql;

	/**
	 * PACKAGE_ID_:所在包
	 */
	public String packageId;

	/**
	 * PAGE_SIZE_:每页显示记录数
	 */
	public int pageSize;

	/**
	 * PARENT_ID_:隶属实体对象ID
	 */
	public String parentId;

	public BdfREntity() {
		super();
	}

	public BdfREntity(String id, String name, String tableName,
			String recursive, String desc, String querySql, String packageId,
			int pageSize, String parentId) {
		super();
		this.id = id;
		this.name = name;
		this.tableName = tableName;
		this.recursive = recursive;
		this.desc = desc;
		this.querySql = querySql;
		this.packageId = packageId;
		this.pageSize = pageSize;
		this.parentId = parentId;
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

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "TABLE_NAME_", length = 100)
	public String getTableName() {
		return tableName;
	}

	public void setRecursive(String recursive) {
		this.recursive = recursive;
	}

	@Column(name = "RECURSIVE_", length = 1)
	public String getRecursive() {
		return recursive;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	@Column(name = "QUERY_SQL_", length = 1000)
	public String getQuerySql() {
		return querySql;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_ID_", length = 50)
	public String getPackageId() {
		return packageId;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Column(name = "PAGE_SIZE_")
	public int getPageSize() {
		return pageSize;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENT_ID_", length = 50)
	public String getParentId() {
		return parentId;
	}

	public String toString() {
		return "BdfREntity [id=" + id + ",name=" + name + ",tableName="
				+ tableName + ",recursive=" + recursive + ",desc=" + desc
				+ ",querySql=" + querySql + ",packageId=" + packageId
				+ ",pageSize=" + pageSize + ",parentId=" + parentId + "]";
	}

}
