/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.jbpm4designer.preference;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.bstek.bdf.plugins.jbpm4designer.Activator;
/**
 * @author Jacky
 */
public class DesignerPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	private ColorSelector borderColorSelector;
	private ColorSelector backgroundColorSelector;
	private ColorSelector transitionColorSelector;
	private TableViewer tableViewer;
	public DesignerPreferencePage(){}

	@Override
	public void init(IWorkbench workbench) {
		this.setTitle("BDF jBPM4流程设计器配置");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void performDefaults() {
		super.performDefaults();
		List<NodeImageConfig> configs=(List<NodeImageConfig>)tableViewer.getInput();
		for(NodeImageConfig config:configs){
			config.setCustomImage(null);
			config.setCustomSmallImage(null);
		}
		Activator.getPreference().setCustomBorderColor(null);
		Activator.getPreference().setCustomBackgroundColor(null);
		Activator.getPreference().setCustomTransitionColor(null);
		borderColorSelector.setColorValue(Activator.getPreference().getBorderColor());
		backgroundColorSelector.setColorValue(Activator.getPreference().getBackgroundColor());
		transitionColorSelector.setColorValue(Activator.getPreference().getTransitionColor());
		tableViewer.refresh();
	}

	@Override
	public boolean performOk() {
		IPreferenceStore store=Activator.getDefault().getPreferenceStore();
		Activator.getPreference().storeToPreference(store);
		return super.performOk();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite=new Composite(parent,SWT.NONE);
		GridData gridData=new GridData();
		gridData.grabExcessHorizontalSpace=true;
		gridData.grabExcessVerticalSpace=true;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2,false));
		this.createBorderColorSelectorField(composite);
		this.createNodeImageConfigTable(composite);
		return composite;
	}
	
	private void createBorderColorSelectorField(Composite composite){
		Label bdfVersionLabel=new Label(composite,SWT.NONE);
		bdfVersionLabel.setText("设置应用中采用的BDF版本：");
		Composite versionComposite=new Composite(composite,SWT.NONE);
		GridLayout layout=new GridLayout(2,true);
		versionComposite.setLayout(layout);
		Button bdf1Button=new Button(versionComposite,SWT.RADIO);
		bdf1Button.setText("V1");
		bdf1Button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Activator.getPreference().setBdfVersion(1);
			}
		});
		Button bdf2Button=new Button(versionComposite,SWT.RADIO);
		bdf2Button.setSelection(true);
		bdf2Button.setText("V2");
		bdf2Button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Activator.getPreference().setBdfVersion(2);
			}
		});
		
		Label selectBorderColorLabel=new Label(composite,SWT.NONE);
		selectBorderColorLabel.setText("设置图形边框色：");
		borderColorSelector=new ColorSelector(composite);
		borderColorSelector.setColorValue(Activator.getPreference().getBorderColor());
		borderColorSelector.addListener(new IPropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				RGB rgb=borderColorSelector.getColorValue();
				Activator.getPreference().setCustomBorderColor(rgb);
			}
		});
		Label selectBackgroundColorLabel=new Label(composite,SWT.NONE);
		selectBackgroundColorLabel.setText("设置图形背景色：");
		backgroundColorSelector=new ColorSelector(composite);
		backgroundColorSelector.setColorValue(Activator.getPreference().getBackgroundColor());
		backgroundColorSelector.addListener(new IPropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				RGB rgb=backgroundColorSelector.getColorValue();
				Activator.getPreference().setCustomBackgroundColor(rgb);
			}
		});
		
		Label selectTransitionColorLabel=new Label(composite,SWT.NONE);
		selectTransitionColorLabel.setText("设置图形连接线颜色：");
		transitionColorSelector=new ColorSelector(composite);
		transitionColorSelector.setColorValue(Activator.getPreference().getTransitionColor());
		transitionColorSelector.addListener(new IPropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				RGB rgb=transitionColorSelector.getColorValue();
				Activator.getPreference().setCustomTransitionColor(rgb);
			}
		});
	}
	
	private void createNodeImageConfigTable(Composite composite){
		GridData data=new GridData(GridData.FILL_BOTH);
		data.horizontalSpan=2;
		Group group=new Group(composite,SWT.NONE);
		group.setText("设置流程图及工具栏图标");
		group.setLayout(new GridLayout(2,false));
		group.setLayoutData(data);
		tableViewer=new TableViewer(group);
		Table table=tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(data);
		TableColumn nameCol=new TableColumn(table,SWT.NONE);
		nameCol.setText("节点名称");
		nameCol.setWidth(200);
		
		TableColumn imgCol=new TableColumn(table,SWT.NONE);
		imgCol.setText("流程图标");
		imgCol.setWidth(100);
		TableColumn samallImgCol=new TableColumn(table,SWT.NONE);
		samallImgCol.setText("工具栏图标");
		samallImgCol.setWidth(100);
		
		tableViewer.setLabelProvider(new NodeConfigTableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(Activator.getPreference().getNodeImageConfigs());
		
		Menu menu=new Menu(table);
		MenuItem changeImage=new MenuItem(menu,SWT.NONE);
		changeImage.setText("更换图标");
		changeImage.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeImage(true);
			}
		});
		MenuItem changeSmallImage=new MenuItem(menu,SWT.NONE);
		changeSmallImage.setText("更换工具栏图标");
		changeSmallImage.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeImage(false);
			}
		});
		
		MenuItem useDefaultImage=new MenuItem(menu,SWT.NONE);
		useDefaultImage.setText("重置图标");
		useDefaultImage.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetImage(true);
			}
		});
		
		MenuItem	defaultSmallImage=new MenuItem(menu,SWT.NONE);
		defaultSmallImage.setText("重置工具栏图标");
		defaultSmallImage.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetImage(false);
			}
		});
		table.setMenu(menu);
		
	}
	private NodeImageConfig getCurrentNodeConfig(){
		IStructuredSelection selection=(IStructuredSelection)tableViewer.getSelection();
		if(selection.isEmpty()){
			MessageDialog.openError(getShell(), "操作错误", "请先选中一个要操作的节点!");
			return null;
		}
		return (NodeImageConfig)selection.getFirstElement();
	} 
	private void resetImage(boolean big){
		NodeImageConfig currentNodeConfig=this.getCurrentNodeConfig();
		if(currentNodeConfig==null)return;
		if(big){
			currentNodeConfig.setCustomImage(null);
			currentNodeConfig.setCustomImagePath(null);
		}else{
			currentNodeConfig.setCustomSmallImage(null);		
			currentNodeConfig.setCustomSmallImagePath(null);
		}
		tableViewer.refresh();
	}
	private void changeImage(boolean big){
		NodeImageConfig currentNodeConfig=this.getCurrentNodeConfig();
		if(currentNodeConfig==null)return;
		FileDialog dialog=new FileDialog(getShell());
		if(big){
			dialog.setText("请选择一张22*22px大小的png格式图标!");
		}else{
			dialog.setText("请选择一张16*16px大小的png格式图标!");			
		}
		dialog.setFilterExtensions(new String[]{"*.png"});
		String fileName=dialog.open();
		if(fileName==null){
			return;
		}
		Image img=Activator.getImageFromLocal(fileName);
		if(big){
			currentNodeConfig.setCustomImage(img);
			currentNodeConfig.setCustomImagePath(fileName);
		}else{
			currentNodeConfig.setCustomSmallImage(img);
			currentNodeConfig.setCustomSmallImagePath(fileName);
		}
		tableViewer.refresh();
	}
}

class NodeConfigTableLabelProvider extends LabelProvider implements ITableLabelProvider{

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		NodeImageConfig config=(NodeImageConfig)element;
		Image img=null;
		switch(columnIndex){
		case 1:
			img=config.getImage();
			break;
		case 2:
			img=config.getSmallImage();
			break;
		}
		return img;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		NodeImageConfig config=(NodeImageConfig)element;
		if(columnIndex==0){
			return config.getNodeName();
		}
		return null;
	}
	
}