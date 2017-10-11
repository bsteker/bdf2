package com.bstek.bdf2.wizard.model;

import java.util.ArrayList;
import java.util.List;

public class Dependence {
	private String groupId;
	private String artifactId;
	private String version;
	private String classifier;
	private List<Exclusion> exclusions = new ArrayList<Exclusion>();

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

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public List<Exclusion> getExclusions() {
		return exclusions;
	}

	public void setExclusions(List<Exclusion> exclusions) {
		this.exclusions = exclusions;
	}
}
