package com.bstek.bdf2.wizard.model;

import java.util.ArrayList;
import java.util.List;

public class Wizard {
	private String projectName;
	private String wizardType;
	private String ormType;
	private String groupId;
	private String artifactId;
	private String version;
	private String versionType;
	private List<AddonInfo> addonInfos = new ArrayList<AddonInfo>();

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getWizardType() {
		return wizardType;
	}

	public void setWizardType(String wizardType) {
		this.wizardType = wizardType;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOrmType() {
		return ormType;
	}

	public void setOrmType(String ormType) {
		this.ormType = ormType;
	}

	public List<AddonInfo> getAddonInfos() {
		return addonInfos;
	}

	public void setAddonInfos(List<AddonInfo> addonInfos) {
		this.addonInfos = addonInfos;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

}
