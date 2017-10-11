package com.bstek.bdf2.rapido.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * bdf_r_page_component:主页面的组件集信息表
 */
@Entity
@Table(name = "bdf_r_page_component")
public class BdfRPageComponent implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID_:主键
	 */
	public String id;

	/**
	 * PAGE_ID_:所属主页面
	 */
	public String pageId;

	/**
	 * ORDER_:排序号
	 */
	public int order;

	/**
	 * COMPONENT_ID_:对应的组件ID
	 */
	public String componentId;

	/**
	 * READ_ONLY_:如果为只读，那么该组件及其下所有组件生成时都自动添加一个readOnly属性
	 */
	public String readOnly;

	public BdfRPageComponent() {
		super();
	}

	public BdfRPageComponent(String id, String pageId, int order,
			String componentId, String readOnly) {
		super();
		this.id = id;
		this.pageId = pageId;
		this.order = order;
		this.componentId = componentId;
		this.readOnly = readOnly;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	public String getId() {
		return id;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	@Column(name = "PAGE_ID_", length = 50)
	public String getPageId() {
		return pageId;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name = "ORDER_")
	public int getOrder() {
		return order;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Column(name = "COMPONENT_ID_", length = 50)
	public String getComponentId() {
		return componentId;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	@Column(name = "READ_ONLY_", length = 1)
	public String getReadOnly() {
		return readOnly;
	}

	public String toString() {
		return "BdfRPageComponent [id=" + id + ",pageId=" + pageId + ",order="
				+ order + ",componentId=" + componentId + ",readOnly="
				+ readOnly + "]";
	}

}
