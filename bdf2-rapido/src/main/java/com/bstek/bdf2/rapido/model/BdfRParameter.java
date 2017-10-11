package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_parameter:查询更新参数信息表
 */
@Entity
@Table(name = "bdf_r_parameter")
public class BdfRParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:参数名
	 */
	public String name;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * VALUE_:参数值可以是一个固定的值，也可以是一个EL表达式，如${abc},或者以#{开头，表示一个BSH表达式；或者为空，为空表示加载数据时从前台传入的参数中取
	 */
	public String value;

	/**
	 * TYPE_:query表示查询，update表示更新，insert表示新增
	 */
	public String type;

	/**
	 * PACKAGE_ID_:所在包
	 */
	public String packageId;

	public BdfRParameter() {
		super();
	}

	public BdfRParameter(String id, String name, String desc, String value,
			String type, String packageId) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.value = value;
		this.type = type;
		this.packageId = packageId;
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

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "VALUE_", length = 100)
	public String getValue() {
		return value;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "TYPE_", length = 10)
	public String getType() {
		return type;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_ID_", length = 50)
	public String getPackageId() {
		return packageId;
	}

	public String toString() {
		return "BdfRParameter [id=" + id + ",name=" + name + ",desc=" + desc
				+ ",value=" + value + ",type=" + type + ",packageId="
				+ packageId + "]";
	}

}
