/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.property.type;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.tabbed.ITypeMapper;
/**
 * @author Jacky
 */
public class NodeElementType implements ITypeMapper {
	@Override
	public Class<?> mapType(Object object) {
		Class<?> type=object.getClass();
		if(object instanceof EditPart){
			type=((EditPart)object).getModel().getClass();
		}
		return type;
	}

}
