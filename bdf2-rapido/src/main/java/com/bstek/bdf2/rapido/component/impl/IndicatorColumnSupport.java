package com.bstek.bdf2.rapido.component.impl;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.dorado.view.widget.grid.IndicatorColumn;

public class IndicatorColumnSupport extends AbstractSupport {

	public String getDisplayName() {
		return IndicatorColumn.class.getSimpleName();
	}

	public String getFullClassName() {
		return IndicatorColumn.class.getName();
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/IndicatorColumn.gif";
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
