package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_component:Dorado7组件信息表
 */
@Entity
@Table(name = "bdf_r_component")
public class BdfRComponent implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * NAME_:组件名称
	 */
	public String name;

	/**
	 * DESC_:描述
	 */
	public String desc;

	/**
	 * CLASS_NAME_:组件实现类名
	 */
	public String className;

	/**
	 * ENTITY_ID_:采用的实体对象
	 */
	public String entityId;

	/**
	 * PARENT_ID_:通过该属性组件之间嵌套，比如AutoForm与其下的Element；Toolbar与其Toolbutton，Grid与其Column等
	 */
	public String parentId;

	/**
	 * LAYOUT_:采用的布局
	 */
	public String layout;

	/**
	 * ACTION_DEF_ID_:采用的动作定义ID
	 */
	public String actionDefId;

	/**
	 * CONTAINER_:1表示为容器型，0表示非容器型，容器型组件下可放其它组件
	 */
	public String container;

	/**
	 * PACKAGE_ID_:所在包
	 */
	public String packageId;

	/**
	 * ORDER_:排序号
	 */
	public int order;

	public BdfRComponent() {
		super();
	}

	public BdfRComponent(String id, String name, String desc, String className,
			String entityId, String parentId, String layout,
			String actionDefId, String container, String packageId, int order) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.className = className;
		this.entityId = entityId;
		this.parentId = parentId;
		this.layout = layout;
		this.actionDefId = actionDefId;
		this.container = container;
		this.packageId = packageId;
		this.order = order;
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

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "CLASS_NAME_", length = 250)
	public String getClassName() {
		return className;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Column(name = "ENTITY_ID_", length = 50)
	public String getEntityId() {
		return entityId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENT_ID_", length = 50)
	public String getParentId() {
		return parentId;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	@Column(name = "LAYOUT_", length = 20)
	public String getLayout() {
		return layout;
	}

	public void setActionDefId(String actionDefId) {
		this.actionDefId = actionDefId;
	}

	@Column(name = "ACTION_DEF_ID_", length = 50)
	public String getActionDefId() {
		return actionDefId;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	@Column(name = "CONTAINER_", length = 1)
	public String getContainer() {
		return container;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_ID_", length = 50)
	public String getPackageId() {
		return packageId;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name = "ORDER_")
	public int getOrder() {
		return order;
	}

	public String toString() {
		return "BdfRComponent [id=" + id + ",name=" + name + ",desc=" + desc
				+ ",className=" + className + ",entityId=" + entityId
				+ ",parentId=" + parentId + ",layout=" + layout
				+ ",actionDefId=" + actionDefId + ",container=" + container
				+ ",packageId=" + packageId + ",order=" + order + "]";
	}

}
