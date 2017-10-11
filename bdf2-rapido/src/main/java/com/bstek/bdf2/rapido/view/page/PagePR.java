/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.page;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.rapido.component.ISupport;
import com.bstek.bdf2.rapido.domain.ComponentInfo;
import com.bstek.bdf2.rapido.domain.ComponentProperty;
import com.bstek.bdf2.rapido.domain.LayoutConstraintProperty;
import com.bstek.bdf2.rapido.domain.LayoutProperty;
import com.bstek.bdf2.rapido.domain.PackageInfo;
import com.bstek.bdf2.rapido.domain.PackageType;
import com.bstek.bdf2.rapido.domain.PageInfo;
import com.bstek.bdf2.rapido.manager.LayoutPropertyManager;
import com.bstek.bdf2.rapido.manager.PackageManager;
import com.bstek.bdf2.rapido.manager.PageManager;
import com.bstek.bdf2.rapido.output.PageOutputer;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.web.DoradoContext;

public class PagePR implements ApplicationContextAware{
	private PageManager pageManager;
	private PackageManager packageManager;
	private PageOutputer pageOutputer;
	private Collection<ISupport> componentSupports;
	private LayoutPropertyManager layoutPropertyManager;
	@DataProvider
	public Collection<PageInfo> loadPages() throws Exception{
		String packageId=(String)DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"packageId");
		Collection<PageInfo> pages=pageManager.loadPageByPackageId(packageId);
		for(PageInfo page:pages){
			if(page.getComponents()==null)continue;
			for(ComponentInfo component:page.getComponents()){
				ISupport support=retriveSupport(component.getClassName());
				component.setIcon("dorado/res/"+support.getIcon());
			}
		}
		return pages;
	}
	@DataProvider
	public Collection<ComponentProperty> loadPageProperties(String pageId){
		return pageManager.loadPageProperties(pageId);
	}
	
	
	private ISupport retriveSupport(String className){
		ISupport support=null;
		for(ISupport s:componentSupports){
			if(className.equals(s.getFullClassName())){
				support=s;
				break;
			}
		}
		return support;
	}

	
	@DataProvider
	public Collection<PackageInfo> loadComponentPackages(String parentId) throws Exception{
		Collection<PackageInfo> result=packageManager.loadPackage(PackageType.component, parentId);
		for(PackageInfo p:result){
			p.setIcon("dorado/res/icons/package.png");
		}
		return result;
	}
	
	@DataProvider
	public Collection<ComponentInfo> loadComponents(Map<String,Object> parameter) throws Exception{
		if(parameter==null)parameter=new HashMap<String,Object>();
		Collection<ComponentInfo> components=pageManager.getComponentManager().loadComponents(parameter);
		for(ComponentInfo component:components){
			ISupport support=retriveSupport(component.getClassName());
			component.setIcon("dorado/res/"+support.getIcon());
		}
		return components;
	}
	
	@DataResolver
	public void savePages(Collection<PageInfo> pages) throws Exception{
		for(PageInfo page:pages){
			EntityState state=EntityUtils.getState(page);
			if(state.equals(EntityState.NEW)){
				pageManager.insertPage(page);
			}else if(state.equals(EntityState.MODIFIED)){
				pageManager.updatePage(page);
			}else if(state.equals(EntityState.DELETED)){
				pageManager.deletePage(page.getId());
			}
			if(page.getLayoutProperties()!=null){
				this.savePageLayoutProperties(page);
			}
			if(page.getComponents()!=null){
				saveComponents(page);
			}
			if(page.getProperties()!=null){
				savePageProperties(page);
			}
		}
	}
	
	private void savePageProperties(PageInfo page){
		for(ComponentProperty cp:page.getProperties()){
			EntityState state=EntityUtils.getState(cp);
			if(state.equals(EntityState.NEW)){
				cp.setComponentId(page.getId());
				pageManager.insertPageProperty(cp);
			}
			if(state.equals(EntityState.DELETED)){
				pageManager.deletePageProperty(cp.getId());
			}
			if(state.equals(EntityState.MODIFIED)){
				pageManager.updatePageProperty(cp);
			}
		}
	}
	
	private void saveComponents(PageInfo page){
		for(ComponentInfo component:page.getComponents()){
			EntityState state=EntityUtils.getState(component);
			if(state.equals(EntityState.NEW)){
				pageManager.insertPageComponent(page.getId(), component);
			}
			if(state.equals(EntityState.DELETED)){
				pageManager.deletePageComponents(page.getId(), component.getId());
			}
			if(state.equals(EntityState.MOVED)){
				pageManager.updatePageComponent(page.getId(), component);
			}
			if(state.equals(EntityState.MODIFIED)){
				pageManager.updatePageComponent(page.getId(), component);
			}
			if(component.getLayoutConstraintProperties()!=null){
				saveComponentLayoutConstraints(component.getLayoutConstraintProperties());
			}
		}
	}
	
	private void saveComponentLayoutConstraints(Collection<LayoutConstraintProperty> layoutConstraints){
		for(LayoutConstraintProperty prop:layoutConstraints){
			EntityState state=EntityUtils.getState(prop);
			if(state.equals(EntityState.NEW)){
				pageManager.getComponentManager().insertComponentLayoutConstraintProperty(prop);
			}
			if(state.equals(EntityState.MODIFIED)){
				pageManager.getComponentManager().updateComponentLayoutConstraintProperty(prop);
			}
			if(state.equals(EntityState.DELETED)){
				pageManager.getComponentManager().deleteComponentLayoutConstraintProperty(prop.getId());
			}
		}
	}	
	
	private void savePageLayoutProperties(PageInfo page){
		for(LayoutProperty layout:page.getLayoutProperties()){
			EntityState state=EntityUtils.getState(layout);
			if(state.equals(EntityState.NEW)){
				layout.setComponentId(page.getId());
				layoutPropertyManager.insertLayoutProperty(layout);
			}
			if(state.equals(EntityState.MODIFIED)){
				layoutPropertyManager.updateLayoutProperty(layout);
			}
			if(state.equals(EntityState.DELETED)){
				layoutPropertyManager.deleteLayoutProperty(layout.getId());
			}
		}
	}
	@Expose
	public String preview(Map<String,Object> parameter)throws Exception{
		String pageId=(String)parameter.get("pageId");
		String pageName=(String)parameter.get("pageName");
		return pageOutputer.createPage(pageId,pageName);
	}
	
	public LayoutPropertyManager getLayoutPropertyManager() {
		return layoutPropertyManager;
	}
	public void setLayoutPropertyManager(LayoutPropertyManager layoutPropertyManager) {
		this.layoutPropertyManager = layoutPropertyManager;
	}
	public PackageManager getPackageManager() {
		return packageManager;
	}

	public void setPackageManager(PackageManager packageManager) {
		this.packageManager = packageManager;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}
	
	public PageOutputer getPageOutputer() {
		return pageOutputer;
	}

	public void setPageOutputer(PageOutputer pageOutputer) {
		this.pageOutputer = pageOutputer;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		componentSupports=applicationContext.getBeansOfType(ISupport.class).values();
	}
}
