/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.wizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Document;

import com.bstek.bdf.plugins.jbpm4designer.model.ProcessDefinition;
/**
 * @author Jacky
 */
public class NewProcessWizard extends Wizard implements INewWizard {
	private WizardNewFileCreationPage page;
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page=new WizardNewFileCreationPage("选择存储目录",selection);
		page.setFileName("process.jpdl.xml");
		page.setTitle("流程模版存储目录选择");
		page.setDescription("选择新建的流程模版文件存放目录");
		this.addPage(page);
	}

	@Override
	public boolean performFinish() {
		Display.getDefault().syncExec(new Runnable(){
			@Override
			public void run() {
				try{
					IFile ifile=page.createNewFile();
					String fileName=page.getFileName();
					IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
					DocumentBuilder builder=factory.newDocumentBuilder();
					Document doc=builder.newDocument();
					doc.setXmlVersion("1.0");
					ProcessDefinition pd=new ProcessDefinition();
					pd.setName(fileName.substring(0, fileName.indexOf(".")));
					doc.appendChild(pd.parseModel(doc));
					Transformer transformer=TransformerFactory.newInstance().newTransformer();
					transformer.setOutputProperty("encoding","utf-8");
					transformer.setOutputProperty(OutputKeys.INDENT,"yes");				
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					transformer.transform(new DOMSource(doc),new StreamResult(out));
					ByteArrayInputStream inputStream=new ByteArrayInputStream(out.toByteArray());
					ifile.setContents(inputStream, true, false,null);
					inputStream.close();
					out.close();
					FileEditorInput fileEditInput=new FileEditorInput(ifile);
					window.getActivePage().openEditor(fileEditInput, "com.bstek.bdf.designer.jbpm4.editor.Jbpm4Designer");
					PlatformUI.getWorkbench().showPerspective("com.bstek.bdf.plugins.jbpm4designer.Jbpm4Perspective", window);
				}catch(Exception ex){
					ex.printStackTrace();
					MessageDialog.openError(getShell(), "创建文件出错",ex.getMessage());
				}
			}
		});
		return true;
	}
}
