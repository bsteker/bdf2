package com.bstek.bdf2.profile.model;

/**
 * @author Jacky.gao
 * @since 2013-2-26
 */
public class UrlDefinition implements java.io.Serializable{
	private static final long serialVersionUID = 3613659977166869169L;
	private String id;
	private String url;
	private String name;
	private String parentId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
