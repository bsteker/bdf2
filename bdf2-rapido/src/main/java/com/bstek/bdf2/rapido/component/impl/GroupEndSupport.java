package com.bstek.bdf2.rapido.component.impl;

import java.util.Collection;

import com.bstek.bdf2.rapido.component.AbstractSupport;
import com.bstek.bdf2.rapido.component.ISupport;

public class GroupEndSupport extends AbstractSupport {

	public String getDisplayName() {
		return "GroupStart";
	}

	public String getFullClassName() {
		return "GroupStart";
	}

	public String getIcon() {
		return "com/bstek/bdf2/rapido/icons/GroupEnd.gif";
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
		return true;
	}

}
