package com.bstek.bdf.plugins.databasetool.wizard.pages.provider;


public class TableTreeNode {

	public String name;
	public boolean isTable;
	public TableTreeNode parent;
	public TableTreeNode[] children = new TableTreeNode[] {};

	public String getName() {
		return name;
	}

	public boolean isTable() {
		return isTable;
	}

	public TableTreeNode getParent() {
		return parent;
	}

	public TableTreeNode[] getChildren() {
		return children;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTable(boolean isTable) {
		this.isTable = isTable;
	}

	public void setParent(TableTreeNode parent) {
		this.parent = parent;
	}

	public void setChildren(TableTreeNode[] children) {
		this.children = children;
	}

}
