package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_package_info:包信息表
 */
@Entity
@Table(name = "bdf_r_package_info")
public class BdfRPackageInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:目录名称
	 */
	public String name;

	/**
	 * PARENT_ID_:上级目录ID
	 */
	public String parentId;

	/**
	 * TYPE_:page用于存放主页面；subpage用于存放子页面；component用于存放组件；action用于存放动作;entity用于存储实体;parameter用于存储参数;metadata用于存储元数据
	 */
	public String type;

	/**
	 * DESC_:描述用于显示
	 */
	public String desc;

	/**
	 * COMPANY_ID_:
	 */
	public String companyId;

	public BdfRPackageInfo() {
		super();
	}

	public BdfRPackageInfo(String id, String name, String parentId,
			String type, String desc, String companyId) {
		super();
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.type = type;
		this.desc = desc;
		this.companyId = companyId;
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

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENT_ID_", length = 50)
	public String getParentId() {
		return parentId;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "TYPE_", length = 10)
	public String getType() {
		return type;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "COMPANY_ID_", length = 50)
	public String getCompanyId() {
		return companyId;
	}

	public String toString() {
		return "BdfRPackageInfo [id=" + id + ",name=" + name + ",parentId="
				+ parentId + ",type=" + type + ",desc=" + desc + ",companyId="
				+ companyId + "]";
	}

}
