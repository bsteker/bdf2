/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.bstek.bdf2.rapido.RapidoJdbcDao;
import com.bstek.bdf2.rapido.builder.IBuilder;
import com.bstek.bdf2.rapido.common.RuleSetHelper;
import com.bstek.bdf2.rapido.domain.PackageInfo;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.bdf2.rapido.manager.PackageManager;
import com.bstek.bdf2.rapido.manager.PageManager;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;

public class PageOutputer extends RapidoJdbcDao implements ApplicationContextAware {
	private PageManager pageManager;
	private String outputDir;
	private Collection<IBuilder> builders;
	private RuleSetHelper ruleSetHelper;
	private PackageManager packageManager;
	private String urlPrefix;
	public String createPage(String pageId,String pageName) throws Exception{
		PageInfo page=pageManager.loadPageForCreateXml(pageId);
		StringBuffer sb=new StringBuffer();
		buildOutputPath(page.getPackageId(),sb);
		String path=outputDir+"/"+urlPrefix+"/"+sb.toString();
		String fileName = generateView(page, path,pageName);
		return urlPrefix+"."+sb.toString().replace("/", ".")+fileName;
	}

	private String generateView(PageInfo page, String path,String pageName) throws Exception,
			IOException {
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule viewConfigRule=ruleSet.getRule("ViewConfig");
		BaseElement rootElement=new BaseElement(viewConfigRule.getNodeName());
		for(IBuilder builder:builders){
			rootElement.add(builder.build(page,ruleSet));
		}
		File f=new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		if(StringUtils.isEmpty(pageName)){
			pageName=page.getName();			
		}
		String targetFilePath=path+"/"+pageName+".view.xml";
		
		OutputStream out=new FileOutputStream(targetFilePath);
		Document document=DocumentHelper.createDocument(rootElement);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter xmlWriter=new XMLWriter(out, format);
		try{
			xmlWriter.write(document);			
		}finally{
			xmlWriter.close();		
			out.close();
		}
		return pageName;
	}

	
	private void buildOutputPath(String packageId,StringBuffer sb){
		if(packageId==null)return;
		PackageInfo packageInfo=packageManager.loadPackageInfo(packageId);
		String dir=sb.toString();
		sb.delete(0,sb.length());
		sb.append(packageInfo.getName()+"/"+dir);
		if(StringUtils.isNotEmpty(packageInfo.getParentId())){
			buildOutputPath(packageInfo.getParentId(),sb);
		}
	}
	
	public RuleSetHelper getRuleSetHelper() {
		return ruleSetHelper;
	}
	public void setRuleSetHelper(RuleSetHelper ruleSetHelper) {
		this.ruleSetHelper = ruleSetHelper;
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		builders=applicationContext.getBeansOfType(IBuilder.class).values();
		WebApplicationContext context=(WebApplicationContext)applicationContext;
		File f=new File(outputDir);
		if(!f.exists()){
			outputDir=context.getServletContext().getRealPath(outputDir);
		}
		File finalDir=new File(outputDir);
		if(!finalDir.exists()){
			finalDir.mkdirs();
		}
	}
	
	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	public PageManager getPageManager() {
		return pageManager;
	}
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public PackageManager getPackageManager() {
		return packageManager;
	}

	public void setPackageManager(PackageManager packageManager) {
		this.packageManager = packageManager;
	}
}
