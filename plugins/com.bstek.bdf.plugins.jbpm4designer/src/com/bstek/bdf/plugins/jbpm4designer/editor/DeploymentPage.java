/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.editor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
/**
 * @author Jacky
 */
public class DeploymentPage extends FormPage{
	private Text fileLocationText;
	private Button exportButton;
	private Text serverNameText;
	private Text contextPathText;
	private Text portText;
	private Text serverAddressText;
	private String buildFileLocation=null;
	private String deploymentURL="http://localhost:8080/bdf-demo";
	private GraphicalViewer graphicalViewer;
	private String bdf1TargetProcessor="bdf.jbpm4.jbpm4Controller.deployProcessDefinitionForDesigner.c";
	private String bdf2TargetProcessor="dorado/bdf2/jbpm4/deploy.processdefinition";
	String BOUNDARY = "---------------------------7d4a6d158c9";
	public DeploymentPage(FormEditor editor,GraphicalViewer graphicalViewer,String title) {
		super(editor,DeploymentPage.class.getName(), title);
		this.graphicalViewer=graphicalViewer;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit=managedForm.getToolkit();
		ScrolledForm form=managedForm.getForm();
		form.setText("流程模版导出与部署");
		ColumnLayout layout=new ColumnLayout();
		layout.topMargin=0;
		layout.bottomMargin=0;
		layout.leftMargin=0;
		layout.rightMargin=0;
		layout.maxNumColumns=1;
		layout.horizontalSpacing=5;
		layout.verticalSpacing=5;
		form.getBody().setLayout(layout);
		this.createExportSection(toolkit, form);
		this.createOnlineDeploymentSection(toolkit, form);
	}
	
	private void createExportSection(FormToolkit toolkit,ScrolledForm form){
		Section section=toolkit.createSection(form.getBody(),Section.TWISTIE|Section.TITLE_BAR|Section.EXPANDED);
		section.setText("导出流程模版");
		section.setExpanded(true);
		Composite composite=toolkit.createComposite(section);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3,false));
		toolkit.createLabel(composite, "文件存储位置：");
		fileLocationText=toolkit.createText(composite, "", SWT.BORDER|SWT.FILL|SWT.READ_ONLY);
		GridData data=new GridData();
		data.grabExcessHorizontalSpace=true;
		data.horizontalAlignment=GridData.FILL;
		fileLocationText.setLayoutData(data);
		fileLocationText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				buildFileLocation=fileLocationText.getText();
				if(fileLocationText.getText()!=null && fileLocationText.getText().length()>0){
					exportButton.setEnabled(true);					
				}else{
					exportButton.setEnabled(false);										
				}
			}
		});
		Button buttonBrowser=toolkit.createButton(composite,"选择目录...",SWT.PUSH);
		buttonBrowser.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog=new DirectoryDialog(getSite().getShell());
				dialog.setText("选择导出文件存储路径");
				String fileName=dialog.open();
				if(fileName==null || fileName.length()<1){
					return;
				}
				fileLocationText.setText(fileName);
			}
		});
		exportButton=toolkit.createButton(composite, "导出模版...", SWT.PUSH);
		exportButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String storePath=buildFileLocation.replace("\\", "/")+"/"+getInputFileName()+".zip";
				Job job=new Job("导出模版"){
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("模版导出中", IProgressMonitor.UNKNOWN);
						Throwable throwable=buildDeploymentZipFile(storePath);
						monitor.done();
						if(throwable!=null){
							return new Status(Status.ERROR,Activator.PLUGIN_ID,throwable.getMessage(),throwable);
						}else{
							return new Status(Status.OK,Activator.PLUGIN_ID,"模版导出操作成功！");
						}
					}
				};
				job.setUser(true);
				job.addJobChangeListener(new JobChangeAdapter(){
					@Override
					public void done(IJobChangeEvent event) {
						IStatus status=event.getResult();
						if(status.isOK()){
							MessageDialog.openInformation(PlatformUI.createDisplay().getActiveShell(), "操作提示", "模版导出操作成功！");
						}
					}
				});
				job.schedule();
			}
		});
		data=new GridData();
		data.horizontalSpan=3;
		data.horizontalAlignment=GridData.END;
		exportButton.setLayoutData(data);
		exportButton.setEnabled(false);
	}
	private void createOnlineDeploymentSection(FormToolkit toolkit,ScrolledForm form){
		Section section=toolkit.createSection(form.getBody(),Section.TWISTIE|Section.TITLE_BAR);
		section.setText("在线部署流程模版");
		section.setExpanded(true);
		Composite composite=toolkit.createComposite(section);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3,false));
		toolkit.createLabel(composite, "服务器IP或名称：");
		serverNameText=toolkit.createText(composite, "localhost", SWT.BORDER);
		serverNameText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				buildServerAddress();
			}
		});
		GridData data=new GridData();
		data.grabExcessHorizontalSpace=true;
		data.horizontalAlignment=GridData.FILL;
		data.horizontalSpan=2;
		serverNameText.setLayoutData(data);
		serverNameText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				buildServerAddress();
			}
		});
		toolkit.createLabel(composite, "端口号：");
		portText=toolkit.createText(composite, "8080");
		portText.setLayoutData(data);
		portText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				buildServerAddress();
			}
		});
		toolkit.createLabel(composite, "应用的ContextPath：");
		contextPathText=toolkit.createText(composite, "bdf-demo");
		contextPathText.setLayoutData(data);
		contextPathText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				buildServerAddress();
			}
		});
		toolkit.createLabel(composite, "当前采用的地址：");
		serverAddressText=toolkit.createText(composite, "http://localhost:8080/bdf-demo",SWT.BORDER);
		serverAddressText.setEnabled(false);
		serverAddressText.setLayoutData(data);
		Button testServerButton=toolkit.createButton(composite, "测试服务是否可用", SWT.PUSH);
		GridData testButtonData=new GridData();
		testButtonData.horizontalSpan=3;
		testButtonData.horizontalAlignment=GridData.END;
		testServerButton.setLayoutData(testButtonData);
		testServerButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Job job=new Job("连接服务器测试"){
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("服务器连接中", IProgressMonitor.UNKNOWN);
						Throwable throwable=testServer();
						monitor.done();
						if(throwable==null){
							return new Status(Status.OK,Activator.PLUGIN_ID,"服务连接成功！");
						}else{
							return new Status(Status.ERROR,Activator.PLUGIN_ID,throwable.getMessage(),throwable);
						}
					}
					
				};
				job.setUser(true);
				job.schedule();
				job.addJobChangeListener(new JobChangeAdapter(){
					@Override
					public void done(IJobChangeEvent event) {
						IStatus status=event.getResult();
						if(status.isOK()){
							MessageDialog.openInformation(PlatformUI.createDisplay().getActiveShell(), "操作提示", "服务连接成功！");							
						}
					}
				});
			}
		});
		Button deploymentButton=toolkit.createButton(composite, "发布模版...", SWT.PUSH);
		GridData deployButtonData=new GridData();
		deployButtonData.horizontalSpan=3;
		deployButtonData.horizontalAlignment=GridData.END;
		deploymentButton.setLayoutData(deployButtonData);
		deploymentButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Job job=new Job("在线发布流程模版"){
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("开始发布流程模版", IProgressMonitor.UNKNOWN);
						Throwable throwable=deployProcessToServer();
						monitor.done();
						if(throwable==null){
							return new Status(Status.OK,Activator.PLUGIN_ID,"发布成功！");
						}else{
							return new Status(Status.ERROR,Activator.PLUGIN_ID,"发布失败！",throwable);							
						}
					}
				};
				job.setUser(true);
				job.addJobChangeListener(new JobChangeAdapter(){
					@Override
					public void done(IJobChangeEvent event) {
						if(event.getResult().isOK()){
							MessageDialog.openInformation(PlatformUI.createDisplay().getActiveShell(), "提示", "流程模版发布成功 ！");
						}
					}					
				});
				job.schedule();
			}
		});
	}
	
	private Throwable testServer(){
		Throwable throwable=null;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection=this.buildHttpURLConnection();  
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();
			InputStream is = urlConnection.getInputStream();  
            byte[] inbuf = new byte[1024];  
            int rn;  
            while((rn=is.read(inbuf,0,1024))>0){  
                System.out.write(inbuf,0,rn);  
            }  
            is.close();
		} catch (Exception e) {
			throwable=e;
		}
		return throwable;
	}
	
	private Throwable deployProcessToServer() {
		Throwable throwable = null;
		try {
			HttpURLConnection conn = this.buildHttpURLConnection();
			StringBuffer sb = new StringBuffer();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"processzipfile\"; filename=\"processzipfile.zip\"\r\n");
			sb.append("Content-Type:application/x-zip-compressed\r\n\r\n");
			byte[] data = sb.toString().getBytes();
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			OutputStream os = conn.getOutputStream();
			os.write(data);
			String tempProcessZipFile=this.buildTempDeploymentZipFile();
			FileInputStream fis=new FileInputStream(tempProcessZipFile);
			int rn2;  
            byte[] buf2 = new byte[1024];  
            while((rn2=fis.read(buf2, 0, 1024))>0){     
                os.write(buf2,0,rn2);  
            }
            os.write(end_data);  
            os.flush();  
            os.close();  
            fis.close();
            InputStream is = conn.getInputStream();  
            byte[] inbuf = new byte[1024];  
            int rn;  
            while((rn=is.read(inbuf,0,1024))>0){  
                System.out.write(inbuf,0,rn);  
            }  
            is.close();  
            File file=new File(tempProcessZipFile);
            if(file.exists())file.delete();
		} catch (Exception e) {
			throwable = e;
		}
		return throwable;
	}
	
	private String buildTempDeploymentZipFile(){
		File file=Activator.getDefault().getStateLocation().toFile();
		String filePath=file.getAbsolutePath()+"/process-images";
		file=new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath+="/process.zip";
		Throwable throwable=this.buildDeploymentZipFile(filePath);
		if(throwable!=null){
			throw new RuntimeException(throwable);
		}
		return filePath;
	}
	
	private HttpURLConnection buildHttpURLConnection() throws Exception{
		URL url=null;
		if(Activator.getPreference().getBdfVersion()==1){
			url=new URL(deploymentURL+"/"+bdf1TargetProcessor);
		}else if(Activator.getPreference().getBdfVersion()==2){
			url=new URL(deploymentURL+"/"+bdf2TargetProcessor);;
		}
		HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("POST");  
		urlConnection.setDoOutput(true);  
		urlConnection.setDoInput(true);
		urlConnection.setRequestProperty("Content-Type","multipart/form-data;boundary="+BOUNDARY);
		urlConnection.setRequestProperty("connection", "Keep-Alive");
		urlConnection.setRequestProperty("Charsert", "UTF-8");
		urlConnection.setUseCaches(false); 
		return urlConnection;
	}

	private void buildServerAddress(){
		deploymentURL="http://"+serverNameText.getText()+":"+portText.getText()+"/"+contextPathText.getText();
		serverAddressText.setText(deploymentURL);
	}
	
	private Throwable buildDeploymentZipFile(String storePath){
		Throwable throwable=null;
		FileOutputStream out=null;
		try{
			out=new FileOutputStream(new File(storePath));
			FileEditorInput input=(FileEditorInput)this.getEditorInput();
			File[] files=new File[2];
			files[0]=new File(input.getPath().toOSString());
			files[1]=new File(this.generateProcessImage());
			createZipFile(storePath,files);
		}catch(Exception ex){
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			throwable=ex;
		}finally{
			try{
				out.flush();
				out.close();				
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return throwable;
	}
	private String generateProcessImage(){
		File file=Activator.getDefault().getStateLocation().toFile();
		String filePath=file.getAbsolutePath()+"/process-images";
		if(this.graphicalViewer.getRootEditPart() instanceof ScalableFreeformRootEditPart){
			ScalableFreeformRootEditPart editPart=(ScalableFreeformRootEditPart)this.graphicalViewer.getRootEditPart();
			IFigure figure=editPart.getLayer(ScalableFreeformRootEditPart.PRINTABLE_LAYERS);
			Rectangle rect=figure.getBounds();
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			Image img=null;
			GC gc=null;
			Graphics graphics=null;
			try{
				img=new Image(null,rect.width,rect.height);
				gc=new GC(img);
				graphics=new SWTGraphics(gc);
				figure.paint(graphics);
				ImageLoader loader=new ImageLoader();
				loader.data=new ImageData[]{img.getImageData()};
				loader.save(out, SWT.IMAGE_PNG);
			}finally{
				if(graphics!=null)graphics.dispose();
				if(gc!=null)gc.dispose();
				if(img!=null)img.dispose();
			}
			file=new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			String fileName=filePath+"/"+this.getInputFileName()+".png";
			FileOutputStream fileOutputStream=null;
			try{
				file=new File(fileName);
				if(file.exists())file.delete();
				fileOutputStream=new FileOutputStream(file);	
				out.writeTo(fileOutputStream);
			}catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}finally{
				if(fileOutputStream!=null){
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			return fileName;
		}
		return null;		
	}
	private String getInputFileName(){
		String xmlFileName=this.getEditorInput().getName();
		int pos=xmlFileName.indexOf(".");
		if(pos!=-1){
			xmlFileName=xmlFileName.substring(0, pos);
		}
		return xmlFileName;
	}
	byte[] buffer = new byte[1024];
	private void createZipFile(String targetOutZipFileName, File[] inputFiles){
		try{
			ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(targetOutZipFileName));  
			for(File inputFile:inputFiles){
				zip(zos,inputFile);  				
			}
			zos.closeEntry();
			zos.close();		
		}catch(Exception ex){
			ex.printStackTrace();
		}
    }  
    private void zip(ZipOutputStream zos, File file) throws Exception{
    	zos.putNextEntry(new ZipEntry(file.getName()));
    	FileInputStream in=new FileInputStream(file);  
    	int len;  
    	while((len=in.read(buffer))!=-1){  
    		zos.write(buffer,0,len);
    	}
    	in.close();
    }  
}
