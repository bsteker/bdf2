package com.bstek.bdf2.rapido.component.impl;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.dorado.view.widget.grid.ColumnGroup;

public class ColumnGroupSupport extends AbstractSupport {
	private Collection<ISupport> children;
	public String getDisplayName() {
		return ColumnGroup.class.getSimpleName();
	}

	public String getFullClassName() {
		return ColumnGroup.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/ColumnGroup.gif";
	}

	public Collection<ISupport> getChildren() {
		return children;
	}

	public boolean isSupportEntity() {
		return false;
	}

	public boolean isSupportLayout() {
		return false;
	}

	public boolean isSupportAction() {
		return false;
	}

	public boolean isContainer() {
		return false;
	}

	public boolean isAlone() {
		return false;
	}

	public void setChildren(Collection<ISupport> children) {
		this.children = children;
	}
}
