package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_page:主页面信息表
 */
@Entity
@Table(name = "bdf_r_page")
public class BdfRPage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:页面名称
	 */
	public String name;

	/**
	 * PACKAGE_ID_:所在包
	 */
	public String packageId;

	/**
	 * LAYOUT_:采用的布局
	 */
	public String layout;

	/**
	 * DESC_:描述
	 */
	public String desc;

	public BdfRPage() {
		super();
	}

	public BdfRPage(String id, String name, String packageId, String layout,
			String desc) {
		super();
		this.id = id;
		this.name = name;
		this.packageId = packageId;
		this.layout = layout;
		this.desc = desc;
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

	@Column(name = "NAME_", length = 100)
	public String getName() {
		return name;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_ID_", length = 50)
	public String getPackageId() {
		return packageId;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	@Column(name = "LAYOUT_", length = 20)
	public String getLayout() {
		return layout;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "DESC_", length = 50)
	public String getDesc() {
		return desc;
	}

	public String toString() {
		return "BdfRPage [id=" + id + ",name=" + name + ",packageId="
				+ packageId + ",layout=" + layout + ",desc=" + desc + "]";
	}

}
