/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.gef.commands.Command;

/**
 * @author Jacky
 */
public class PropertyChangeCommand extends Command {
	private Object target;
	private String propertyName;
	private Object newValue;
	private Object oldValue;
	
	public PropertyChangeCommand(Object node,String propertyName,Object newValue){
		this.target=node;
		this.propertyName=propertyName;
		this.newValue=newValue;
	}
	@Override
	public void execute() {
		try {
			oldValue=PropertyUtils.getProperty(target,propertyName);
			PropertyUtils.setProperty(target, propertyName, newValue);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean canUndo() {
		return oldValue!=null;
	}
	@Override
	public void undo() {
		try {
			PropertyUtils.setProperty(target, propertyName, newValue);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
