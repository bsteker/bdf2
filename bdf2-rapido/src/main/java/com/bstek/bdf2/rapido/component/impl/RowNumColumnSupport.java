package com.bstek.bdf2.rapido.component.impl;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.dorado.view.widget.grid.RowNumColumn;

public class RowNumColumnSupport extends AbstractSupport {

	public String getDisplayName() {
		return RowNumColumn.class.getSimpleName();
	}

	public String getFullClassName() {
		return RowNumColumn.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/RowNumColumn.gif";
	}

	public Collection<ISupport> getChildren() {
		return null;
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
}
