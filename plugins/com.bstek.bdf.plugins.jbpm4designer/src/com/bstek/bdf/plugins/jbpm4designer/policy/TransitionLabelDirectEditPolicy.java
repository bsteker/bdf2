/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.swt.widgets.Text;

import com.bstek.bdf.plugins.jbpm4designer.command.TransitionLabelDirectEditCommand;
import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
/**
 * @author Jacky
 */
public class TransitionLabelDirectEditPolicy extends DirectEditPolicy {
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		Text text=(Text)request.getCellEditor().getControl();
		TransitionLabel transitionLabel=(TransitionLabel)(this.getHost().getModel());
		return new TransitionLabelDirectEditCommand(transitionLabel,text.getText());
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {

	}
}
