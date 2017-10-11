/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.command;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
/**
 * @author Jacky
 */
public class CreateTransitionCommand extends Command {
	private AbstractNodeElement source;
	private AbstractNodeElement target;
	private Transition transition;
	public CreateTransitionCommand(AbstractNodeElement source){
		this.source=source;
	}
	@Override
	public boolean canExecute() {
		return super.canExecute();
	}

	@Override
	public void execute() {
		transition=new Transition(source,target);
		if(!hasTransition(source,target)){
			this.buildTransition(transition);
			transition.reconnection();
		}else{
			transition=null;
			MessageDialog.openWarning(null,"操作错误", "当前两节点上已有Transition连接！");
		}
	}
	
	private void buildTransition(Transition trans){
		trans.setTransitionLabel(new TransitionLabel("to "+target.getLabel()));
	}
	
	private boolean hasTransition(AbstractNodeElement source,AbstractNodeElement target){
		boolean flag=false;
		List<Transition> trans=source.getOutTransitions();
		for(Transition transition:trans){
			AbstractNodeElement compareNode=transition.getTarget();
			if(compareNode==target){
				flag=true;
				break;
			}				
		}
		return flag;
	}
	public void setTarget(AbstractNodeElement target) {
		this.target = target;
	}
	@Override
	public void undo() {
		if(transition!=null){
			transition.disconnection();
		}
	}
}
