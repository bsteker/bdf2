/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
/**
 * @author Jacky
 */
public class Transition{
	private String expression;
	private boolean connected;
	private TransitionLabel transitionLabel;
	private AbstractNodeElement source;
	private AbstractNodeElement target;
	private String sourceNodeName;
	private String targetNodeName;
	private List<Point> bendpoints=new ArrayList<Point>();
	private PropertyChangeSupport listeners=new PropertyChangeSupport(this);
	public static final String ADD_BENDPOINT="ADD_BENDPOINT";
	public static final String REMOVE_BENDPOINT="REMOVE_BENDPOINT";
	public static final String ADD_TRANSITION_LABEL="ADD_TRANSITION_LABEL";
	public Transition(){
	}
	
	public Transition(AbstractNodeElement source,AbstractNodeElement target){
		this.source=source;
		this.target=target;
	}
	public void reconnection(){
		if(!this.connected){
			this.source.addTransition(this);
			this.target.addTransition(this);
			this.connected=true;
		}
	}
	public void disconnection(){
		this.connected=false;
		this.source.removeTransition(this);
		this.target.removeTransition(this);
	}
	public void addBendpoint(int index,Point point) {
		this.bendpoints.add(index,point);
		this.firePropertyChangeListeners(ADD_BENDPOINT, null, point);
	}
	public void addBendpoint(Point point) {
		this.bendpoints.add(point);
	}
	public void removeBendpoint(int index) {
		this.bendpoints.remove(index);
		this.firePropertyChangeListeners(REMOVE_BENDPOINT, null,null);
	}
	public List<Point> getBendpoints() {
		return bendpoints;
	}
	public AbstractNodeElement getSource() {
		return source;
	}
	public void setSource(AbstractNodeElement source) {
		this.source = source;
	}
	public AbstractNodeElement getTarget() {
		return target;
	}
	public void setTarget(AbstractNodeElement target) {
		this.target = target;
	}
	public TransitionLabel getTransitionLabel() {
		return transitionLabel;
	}
	public void setTransitionLabel(TransitionLabel transitionLabel) {
		this.transitionLabel = transitionLabel;
		this.listeners.firePropertyChange(ADD_TRANSITION_LABEL, null, null);
	}
	public void firePropertyChangeListeners(String eventName,Object oldValue,Object newValue){
		this.listeners.firePropertyChange(eventName, oldValue, newValue);
	}
	public void addPropertyChangeListener(PropertyChangeListener listener){
		this.listeners.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener){
		this.listeners.removePropertyChangeListener(listener);
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String getSourceNodeName() {
		return sourceNodeName;
	}

	public void setSourceNodeName(String sourceNodeName) {
		this.sourceNodeName = sourceNodeName;
	}

	public String getTargetNodeName() {
		return targetNodeName;
	}

	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
	}
}
