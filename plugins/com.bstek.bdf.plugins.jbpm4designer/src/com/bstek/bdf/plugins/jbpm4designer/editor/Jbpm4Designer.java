/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.Document;

import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;

/**
 * @author Jacky
 */
public class Jbpm4Designer extends FormEditor implements ITabbedPropertySheetPageContributor{
	private GraphicalEditorPage graphicalEditorPage;
	private StructuredTextEditor xmlEditor;
	private DeploymentPage deploymentPage;
	@Override
	protected void addPages() {
		try {
			this.xmlEditor=new StructuredTextEditor();
			graphicalEditorPage=new GraphicalEditorPage(xmlEditor);
			this.addPage(0, graphicalEditorPage,this.getEditorInput());
			this.setPageText(0, "流程图");
			this.addPage(1, xmlEditor,this.getEditorInput());
			deploymentPage=new DeploymentPage(this,graphicalEditorPage.getGraphicalViewer(),"部署");
			this.addPage(2, deploymentPage);
			this.setPageText(1, "源码");
			this.setActivePage(0);
			this.setPartName(this.getEditorInput().getName());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("保存流程模版", IProgressMonitor.UNKNOWN);
		if(graphicalEditorPage.isDirty()){
			graphicalEditorPage.doSave(monitor);
		}
		if(this.xmlEditor.isDirty()){
			xmlEditor.doSave(monitor);
			updateGraphical();
		}
		monitor.done();
	}
	
	private void updateGraphical(){
		Document doc=getDocumentFromSource();
		if(doc!=null){
			ProcessDefinition processDefinition=graphicalEditorPage.buildProcessDefinition(doc);
			processDefinition.buildInTransitions();
			graphicalEditorPage.setGraphicalContent(processDefinition);
		}
	}
	
	@Override
	protected void pageChange(int newPageIndex) {
		if(newPageIndex==0){
			if(!this.xmlEditor.isDirty()){
				this.updateGraphical();
			}
		}
		if(newPageIndex==1){
			if(this.graphicalEditorPage.isDirty()){
				graphicalEditorPage.synchroGraphicalToXml();
			}
		}
		super.pageChange(newPageIndex);
	}
	
	private Document getDocumentFromSource(){
		Document document=(Document)this.xmlEditor.getAdapter(Document.class);
		return document;
	}
	
	@Override
	public void doSaveAs() {
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public String getContributorId() {
		return this.getSite().getId();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter==IPropertySheetPage.class){
			return this.getActiveEditor().getAdapter(IPropertySheetPage.class);
		}
		if(adapter==ActionRegistry.class){
			return this.getActiveEditor().getAdapter(ActionRegistry.class);
		}
		return super.getAdapter(adapter);
	}
}
