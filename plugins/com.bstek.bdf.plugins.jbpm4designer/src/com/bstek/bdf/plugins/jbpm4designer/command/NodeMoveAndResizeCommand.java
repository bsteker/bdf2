/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
/**
 * @author Jacky
 */
public class NodeMoveAndResizeCommand extends Command {
	private Rectangle box;
	private AbstractNodeElement node;
	private int oldX;
	private int oldY;
	private int oldWidth;
	private int oldHeight;
	public NodeMoveAndResizeCommand(AbstractNodeElement node,Rectangle box){
		this.box=box;
		this.node=node;
	}
	@Override
	public boolean canExecute() {
		return super.canExecute();
	}

	@Override
	public void execute() {
		oldX=node.getX();
		oldY=node.getY();
		oldWidth=node.getWidth();
		oldHeight=node.getHeight();
		node.setHeight(box.height);
		node.setWidth(box.width);
		node.setX(box.x);
		node.setY(box.y);
	}
	@Override
	public void undo() {
		node.setHeight(oldHeight);
		node.setWidth(oldWidth);
		node.setX(oldX);
		node.setY(oldY);
	}
}
