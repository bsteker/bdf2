package com.bstek.bdf2.jbpm4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Jacky.gao
 * @since 2013-6-3
 */
@Entity
@Table(name="BDF2_JBPM4_TOOLBAR_CONTENT")
public class ToolbarContent {
	@Id
	@Column(name="ID_",length=60)
	private String id;
	@Column(name="TOOLBAR_CONFIG_ID_",length=60)
	private String toolbarConfigId;
	@Column(name="TOOLBAR_CONTENT_PROVIDER_",length=120)
	private String toolbarContentProvider;
	@Column(name="ORDER_")
	private int order;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToolbarConfigId() {
		return toolbarConfigId;
	}
	public void setToolbarConfigId(String toolbarConfigId) {
		this.toolbarConfigId = toolbarConfigId;
	}
	
	public String getToolbarContentProvider() {
		return toolbarContentProvider;
	}
	public void setToolbarContentProvider(String toolbarContentProvider) {
		this.toolbarContentProvider = toolbarContentProvider;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
