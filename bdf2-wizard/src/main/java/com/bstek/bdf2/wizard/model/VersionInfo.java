package com.bstek.bdf2.wizard.model;

public class VersionInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int index;
	private String name;
	private boolean lastVersion;
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(boolean lastVersion) {
		this.lastVersion = lastVersion;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
