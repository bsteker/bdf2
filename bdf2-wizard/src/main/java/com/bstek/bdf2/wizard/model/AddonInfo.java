package com.bstek.bdf2.wizard.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddonInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Date date;
	private String groupId;
	private String artifactId;
	private String lastVersion;
	private String lastUpdated;
	private String desc;
	private List<VersionInfo> versions = new ArrayList<VersionInfo>();
	private List<Dependence> dependencies = new ArrayList<Dependence>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<VersionInfo> getVersions() {
		return versions;
	}

	public void setVersions(List<VersionInfo> versions) {
		this.versions = versions;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<Dependence> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Dependence> dependencies) {
		this.dependencies = dependencies;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
