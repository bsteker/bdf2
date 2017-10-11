/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor;

import java.lang.reflect.Constructor;
import java.util.Random;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
import com.bstek.bdf.plugins.jbpm4designer.Constants;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.CustomNode;
import com.bstek.bdf.plugins.jbpm4designer.model.DecisionNode;
import com.bstek.bdf.plugins.jbpm4designer.model.EndCancelNode;
import com.bstek.bdf.plugins.jbpm4designer.model.EndErrorNode;
import com.bstek.bdf.plugins.jbpm4designer.model.EndNode;
import com.bstek.bdf.plugins.jbpm4designer.model.ForeachNode;
import com.bstek.bdf.plugins.jbpm4designer.model.ForkNode;
import com.bstek.bdf.plugins.jbpm4designer.model.JoinNode;
import com.bstek.bdf.plugins.jbpm4designer.model.StartNode;
import com.bstek.bdf.plugins.jbpm4designer.model.StateNode;
import com.bstek.bdf.plugins.jbpm4designer.model.SubprocessNode;
import com.bstek.bdf.plugins.jbpm4designer.model.TaskNode;
import com.bstek.bdf.plugins.jbpm4designer.preference.NodeImageConfig;
/**
 * @author Jacky
 */
public class GraphicalPalette extends PaletteRoot {
	private int increment=new Random().nextInt(100);
	public GraphicalPalette(){
		PaletteDrawer tools=new PaletteDrawer("节点列表");
		PanningSelectionToolEntry selectionTool=new PanningSelectionToolEntry();
		tools.add(selectionTool);
		this.setDefaultEntry(selectionTool);
		ImageDescriptor transitionDescriptor=ImageDescriptor.createFromImage(Activator.getImageFromPlugin(Constants.TRANSITION_NODE_ICON_SMALL));
		ConnectionCreationToolEntry connection=new ConnectionCreationToolEntry("Transition","Create a Transition",null,transitionDescriptor,transitionDescriptor);
		tools.add(connection);
		tools.add(this.createToolEntry(StartNode.class,"Start","Create a start node",70,40));
		tools.add(this.createToolEntry(EndNode.class,"End","Create a end node",70,40));
		tools.add(this.createToolEntry(TaskNode.class,"Task","Create a task node",80,40));
		tools.add(this.createToolEntry(ForkNode.class,"Fork","Create a fork node",80,40));
		tools.add(this.createToolEntry(JoinNode.class,"Join","Create a join node",80,40));
		tools.add(this.createToolEntry(ForeachNode.class,"Foreach","Create a foreach node",80,40));
		tools.add(this.createToolEntry(DecisionNode.class,"Decision","Create a decision node",80,40));
		tools.add(this.createToolEntry(SubprocessNode.class,"Subprocess","Create a subprocess node",100,40));
		tools.add(this.createToolEntry(EndCancelNode.class,"End Cancel","Create a end cancel node",100,40));
		tools.add(this.createToolEntry(EndErrorNode.class,"End Error","Create a end error node",100,40));
		tools.add(this.createToolEntry(StateNode.class,"State","Create a state node",80,40));
		tools.add(this.createToolEntry(CustomNode.class,"Custom","Create a custom node",80,40));
		this.add(tools);
	}
	
	private CreationToolEntry createToolEntry(final Class<?> nodeClass,final String name,String desc,final int width,final int height){
		SimpleFactory nodeFactory=new SimpleFactory(nodeClass){
			@Override
			public Object getNewObject() {
				AbstractNodeElement node=instanceNode(nodeClass,name,width,height);
				return node;
			}
		};
		String nodeName=instanceNode(nodeClass,name,width,height).nodeName();
		NodeImageConfig config=Activator.getPreference().getNodeImageConfigByName(nodeName);
		if(config==null){
			throw new RuntimeException("当前没有为名为"+nodeName+"的节点预定义配置信息！");
		}
		ImageDescriptor descriptor=ImageDescriptor.createFromImage(config.getSmallImage());
		return new CombinedTemplateCreationEntry(name,desc,nodeFactory,descriptor,descriptor);
	}
	private AbstractNodeElement instanceNode(final Class<?> nodeClass,final String name,final int width,final int height){
		AbstractNodeElement node=null;
		try{
			Constructor<?> constructor=nodeClass.getConstructor(String.class);
			String nodeName=name;
			if(nodeClass!=StartNode.class && nodeClass!=EndNode.class){
				nodeName+=increment;
			}
			node=(AbstractNodeElement)constructor.newInstance(nodeName);
			node.setWidth(width);
			node.setHeight(height);
			increment++;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return node;
	}
}
