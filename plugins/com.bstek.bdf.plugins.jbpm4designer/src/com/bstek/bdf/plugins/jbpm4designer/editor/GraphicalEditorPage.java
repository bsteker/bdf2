/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventObject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bstek.bdf.plugins.jbpm4designer.action.CopyAction;
import com.bstek.bdf.plugins.jbpm4designer.action.CutAction;
import com.bstek.bdf.plugins.jbpm4designer.action.PasteAction;
import com.bstek.bdf.plugins.jbpm4designer.editor.factory.DesignerEditPartFactory;
import com.bstek.bdf.plugins.jbpm4designer.editor.factory.DesignerTreeEditPartFactory;
import com.bstek.bdf.plugins.jbpm4designer.model.AbstractNodeElement;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
import com.bstek.bdf.plugins.jbpm4designer.model.ProcessFactory;
/**
 * @author Jacky
 */
public class GraphicalEditorPage extends GraphicalEditorWithFlyoutPalette implements ITabbedPropertySheetPageContributor{
	private ProcessDefinition graphData;
	private GraphicalViewer graphicalViewer;
	private IPropertySheetPage propertySheetPage;
	private StructuredTextEditor xmlEditor;
	public GraphicalEditorPage(StructuredTextEditor xmlEditor){
		this.setEditDomain(new DefaultEditDomain(this));
		this.xmlEditor=xmlEditor;
	}
	@Override
	public GraphicalViewer getGraphicalViewer(){
		return super.getGraphicalViewer();
	}
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateActions(this.getSelectionActions());
	}
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		graphicalViewer=this.getGraphicalViewer();
		graphicalViewer.setEditPartFactory(new DesignerEditPartFactory());
		graphicalViewer.setRootEditPart(new ScalableFreeformRootEditPart());
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, false);
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, false);
		FileEditorInput input=(FileEditorInput)this.getEditorInput();
		IFile file=input.getFile();
		InputStream in=null;
		try {
			in=file.getContents();
			graphData=parseProcessDefinitionXml(in);
			graphData.buildInTransitions();
			graphicalViewer.setContents(graphData);
			graphicalViewer.setContextMenu(this.createContextMenuManager());
		} catch (CoreException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in!=null)in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private MenuManager createContextMenuManager(){
		return new ContextMenuProvider(graphicalViewer){
			@Override
			public void buildContextMenu(IMenuManager menu) {
				menu.add(getActionRegistry().getAction(ActionFactory.DELETE.getId()));
			}
		};
	}
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer viewer=this.getGraphicalViewer();
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		this.getActionRegistry().registerAction(new ToggleGridAction(viewer));
	}
	public ProcessDefinition parseProcessDefinitionXml(InputStream in){
		ProcessDefinition processDefinition=null;
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc=builder.parse(in);
			processDefinition=this.buildProcessDefinition(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processDefinition;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		super.createActions();
		Action action=new PasteAction((IWorkbenchPart) this);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		action=new CopyAction((IWorkbenchPart) this);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		action=new CutAction((IWorkbenchPart) this);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
	}
	public ProcessDefinition buildProcessDefinition(Document doc){
		Node node=doc.getDocumentElement();
		ProcessDefinition processDefinition=new ProcessDefinition();
		boolean support=processDefinition.support(node);
		if(support){
			processDefinition.parseXml(node);
		}
		ProcessFactory processFactory=ProcessFactory.getInstance();
		NodeList list=node.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node cnode=list.item(i);
			for(AbstractNodeElement nodeElement:processFactory.getAllAvaliableNodes()){
				if(nodeElement.support(cnode)){
					nodeElement.parseXml(cnode);
					processDefinition.addNode(nodeElement);
					break;
				}
			}			
		}
		return processDefinition;
	}
	
	public Document buildDocument(){
		Document doc=null;
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder=factory.newDocumentBuilder();
			doc=builder.newDocument();
			doc.setXmlVersion("1.0");
			Element processDefinition=this.graphData.parseModel(doc);
			for(AbstractNodeElement node:this.graphData.getNodes()){
				processDefinition.appendChild(node.parseModel(doc));
			}
			doc.appendChild(processDefinition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public ProcessDefinition getProcessDefinition(){
		return graphData;
	}
	
	@Override
	protected PaletteRoot getPaletteRoot() {
		return new GraphicalPalette();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		synchroGraphicalToXml();
		xmlEditor.doSave(monitor);
		this.getCommandStack().markSaveLocation();
	}
	
	public void synchroGraphicalToXml(){
		Document doc=this.buildDocument();
		if(doc==null)return;
		TransformerFactory factory=TransformerFactory.newInstance();
		try{
			Transformer transformer=factory.newTransformer();
			transformer.setOutputProperty("encoding","utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");				
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			transformer.transform(new DOMSource(doc),new StreamResult(out));
			xmlEditor.getDocumentProvider().getDocument(xmlEditor.getEditorInput()).set(out.toString("utf-8"));
			out.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean isDirty() {
		return super.isDirty();
	}
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		this.firePropertyChange(PROP_DIRTY);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter==IPropertySheetPage.class){
			this.propertySheetPage=new GraphicalPropertySheetPage(this, getCommandStack());
			return this.propertySheetPage;
		}
		if(adapter==IContentOutlinePage.class){
			OutlinePage page=new OutlinePage(new TreeViewer()); 
			return page;
		}
		return super.getAdapter(adapter);
	}
	
	public void setGraphicalContent(ProcessDefinition processDefinition){
		this.graphData=processDefinition;
		graphicalViewer.setContents(graphData);
		this.graphicalViewer.flush();
	}
	
	class OutlinePage extends ContentOutlinePage{
		public OutlinePage(EditPartViewer viewer) {
			super(viewer);
		}
		@Override
		public void createControl(Composite parent) {
			EditPartViewer viewer=this.getViewer();
			viewer.createControl(parent);
			viewer.setEditDomain(getEditDomain());
			viewer.setEditPartFactory(new DesignerTreeEditPartFactory());
			viewer.setContents(graphData);
			getSelectionSynchronizer().addViewer(viewer);
			super.createControl(parent);
		}
	}

	@Override
	public String getContributorId() {
		return "com.bstek.bdf.designer.jbpm4.editor.Jbpm4Editor";
	}
}
