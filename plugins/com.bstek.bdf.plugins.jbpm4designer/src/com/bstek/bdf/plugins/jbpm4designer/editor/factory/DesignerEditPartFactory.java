/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.CustomNode;
import com.bstek.bdf.plugins.jbpm4designer.model.DecisionNode;
import com.bstek.bdf.plugins.jbpm4designer.model.EndCancelNode;
import com.bstek.bdf.plugins.jbpm4designer.model.EndErrorNode;
import com.bstek.bdf.plugins.jbpm4designer.model.EndNode;
import com.bstek.bdf.plugins.jbpm4designer.model.ForeachNode;
import com.bstek.bdf.plugins.jbpm4designer.model.ForkNode;
import com.bstek.bdf.plugins.jbpm4designer.model.JoinNode;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.model.StartNode;
import com.bstek.bdf.plugins.jbpm4designer.model.StateNode;
import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessNode;
import com.bstek.bdf.plugins.jbpm4designer.model.TaskNode;
import com.bstek.bdf.plugins.jbpm4designer.model.Transition;
import com.bstek.bdf.plugins.jbpm4designer.model.TransitionLabel;
import com.bstek.bdf.plugins.jbpm4designer.part.ActionNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.DecisionNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.EndCancelNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.EndErrorNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.EndNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.ForeachNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.ForkNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.JoinNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.ProcessDefinitionEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.StartNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.StateNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.SubprocessNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.TaskNodeEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.TransitionEditPart;
import com.bstek.bdf.plugins.jbpm4designer.part.TransitionLabelEditPart;
/**
 * @author Jacky
 */
public class DesignerEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if(model instanceof ProcessDefinition){
			return new ProcessDefinitionEditPart((ProcessDefinition)model);
		}
		if(model instanceof TaskNode){
			return new TaskNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof StartNode){
			return new StartNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof EndNode){
			return new EndNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof EndErrorNode){
			return new EndErrorNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof EndCancelNode){
			return new EndCancelNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof StateNode){
			return new StateNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof CustomNode){
			return new ActionNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof ForkNode){
			return new ForkNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof JoinNode){
			return new JoinNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof DecisionNode){
			return new DecisionNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof ForeachNode){
			return new ForeachNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof SubprocessNode){
			return new SubprocessNodeEditPart((AbstractNodeElement)model);
		}
		if(model instanceof Transition){
			return new TransitionEditPart((Transition)model);
		}
		if(model instanceof TransitionLabel){
			return new TransitionLabelEditPart((TransitionLabel)model);
		}
		throw new RuntimeException("不支持的模型对象："+model);
	}
}
