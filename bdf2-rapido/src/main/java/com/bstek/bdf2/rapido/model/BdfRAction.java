package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_action:具体动作信息表
 */
@Entity
@Table(name = "bdf_r_action")
public class BdfRAction implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:名称
	 */
	public String name;

	/**
	 * BEAN_ID_:定义在Spring当中的BeanID
	 */
	public String beanId;

	public BdfRAction() {
		super();
	}

	public BdfRAction(String id, String name, String beanId) {
		super();
		this.id = id;
		this.name = name;
		this.beanId = beanId;
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

	@Column(name = "NAME_", length = 250)
	public String getName() {
		return name;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	@Column(name = "BEAN_ID_", length = 100)
	public String getBeanId() {
		return beanId;
	}

	public String toString() {
		return "BdfRAction [id=" + id + ",name=" + name + ",beanId=" + beanId
				+ "]";
	}

}
