/*
 * This file is part of BDF
 * BDF£¬Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.viewers.TextCellEditor;

import com.bstek.bdf.plugins.jbpm4designer.manager.TransitionLabelCellEditorLocator;
import com.bstek.bdf.plugins.jbpm4designer.manager.TransitionLabelDirectEditManager;
import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
import com.bstek.bdf.plugins.jbpm4designer.policy.TransitionLabelDirectEditPolicy;
import com.bstek.bdf.plugins.jbpm4designer.policy.TransitionLabelMoveEditPolicy;
/**
 * @author Jacky
 */
public class TransitionLabelEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener{
	public TransitionLabelEditPart(TransitionLabel transitionLabel){
		this.setModel(transitionLabel);
		transitionLabel.addPropertyChangeListener(this);
	}
	@Override
	protected IFigure createFigure() {
		TransitionLabel transitionName=(TransitionLabel)this.getModel();
		final Label label=new Label(transitionName.getText());
		return label;
	}
	
	@Override
	public void performRequest(Request req) {
		if(req.getType().equals(REQ_DIRECT_EDIT)){
			TransitionLabelDirectEditManager manager=new TransitionLabelDirectEditManager(this,TextCellEditor.class,new TransitionLabelCellEditorLocator(this));
			manager.show();
		}
		super.performRequest(req);
	}
	@Override
	public DragTracker getDragTracker(Request request) {
		return new DragEditPartsTracker(this) {
			protected EditPart getTargetEditPart() {
				return getParent();
			}
		};
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new TransitionLabelMoveEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new TransitionLabelDirectEditPolicy());
	}
	@Override
	protected void refreshVisuals() {
		TransitionLabel transitionLabel = (TransitionLabel)getModel();
        Label figure = (Label) getFigure();
        figure.setText(transitionLabel.getText());
        Point offset=transitionLabel.getOffset();
        AbstractConnectionEditPart parent = (AbstractConnectionEditPart) getParent();
        PolylineConnection connection = (PolylineConnection) parent.getFigure();
        if(offset==null){
        	offset=this.calculateInitialOffset(connection);
        	transitionLabel.setOffset(offset);
        }
        TransitionLabelLocator constraint = new TransitionLabelLocator(transitionLabel.getText(),offset,connection);
        parent.setLayoutConstraint(this, getFigure(), constraint);
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(TransitionLabel.TRANSITION_LABEL_CHANGED)){
			this.refreshVisuals();
		}
	}
	
	private Point calculateInitialOffset(PolylineConnection polyline) {
		Point result = new Point(5, -10);
		Point start = polyline.getStart();
		Point end = polyline.getEnd();
		Point mid = start.getNegated().getTranslated(end).getScaled(0.5);
		if (mid.x < -10) {
			result.y = 10;
		}
		return result;
	}
}