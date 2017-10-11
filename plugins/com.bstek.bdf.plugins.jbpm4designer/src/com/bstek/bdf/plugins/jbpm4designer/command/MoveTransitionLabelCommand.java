/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
/**
 * @author Jacky
 */
public class MoveTransitionLabelCommand extends Command {
	private Point point;
	private TransitionLabel label;
	private IFigure parentFigure;
	private Point oldOffset;
	public MoveTransitionLabelCommand(TransitionLabel label,IFigure parentFigure,Point point){
		this.point=point;
		this.label=label;
		this.parentFigure=parentFigure;
	}
	@Override
	public void execute() {
		Point offset=this.label.getOffset().getCopy();
		parentFigure.translateToAbsolute(offset);
		offset.translate(point);
		parentFigure.translateToRelative(offset);
		oldOffset=label.getOffset();
		label.setOffset(offset);
	}
	@Override
	public void undo() {
		label.setOffset(oldOffset);
	}
}
