package com.bstek.bdf2.core.view;

import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf2.core.model.AuthorityType;

/**
 * @author Jacky.gao
 * @since 2013-2-18
 */
public class ViewComponent {
	private String id;
	private String icon;
	private AuthorityType authorityType=AuthorityType.read;
	private String urlComponentId;
	private String desc;
	private boolean enabled=true;
	private boolean sortabled=false;
	private boolean use;
	private String name;
	private String componentInfoId;
	private List<ViewComponent> children=new ArrayList<ViewComponent>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isSortabled() {
		return sortabled;
	}
	public void setSortabled(boolean sortabled) {
		this.sortabled = sortabled;
	}
	public boolean isUse() {
		return use;
	}
	public void setUse(boolean use) {
		this.use = use;
	}
	public List<ViewComponent> getChildren() {
		return children;
	}
	public void addChildren(ViewComponent component) {
		this.children.add(component);
	}
	public void setChildren(List<ViewComponent> children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AuthorityType getAuthorityType() {
		return authorityType;
	}
	public void setAuthorityType(AuthorityType authorityType) {
		this.authorityType = authorityType;
	}
	public String getUrlComponentId() {
		return urlComponentId;
	}
	public void setUrlComponentId(String urlComponentId) {
		this.urlComponentId = urlComponentId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getComponentInfoId() {
		return componentInfoId;
	}
	public void setComponentInfoId(String componentInfoId) {
		this.componentInfoId = componentInfoId;
	}
}
