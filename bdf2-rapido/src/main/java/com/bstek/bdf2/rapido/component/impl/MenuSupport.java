package com.bstek.bdf2.rapido.component.impl;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.dorado.view.widget.base.menu.Menu;

public class MenuSupport extends AbstractSupport {
	private Collection<ISupport> children;
	public String getDisplayName() {
		return Menu.class.getSimpleName();
	}

	public String getFullClassName() {
		return Menu.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/Menu.gif";
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
		return true;
	}

	public void setChildren(Collection<ISupport> children) {
		this.children = children;
	}
}
