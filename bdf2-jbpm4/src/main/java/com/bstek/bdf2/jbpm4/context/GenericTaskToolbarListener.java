package com.bstek.bdf2.jbpm4.context;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jbpm.api.Execution;
import org.jbpm.api.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.view.ViewManagerHelper;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.ToolbarConfig;
import com.bstek.bdf2.jbpm4.model.ToolbarContent;
import com.bstek.bdf2.jbpm4.model.ToolbarPosition;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.jbpm4.view.toolbar.IToolbarContentProvider;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.VerticalAlign;
import com.bstek.dorado.view.widget.base.toolbar.ToolBar;
import com.bstek.dorado.view.widget.layout.DockLayout;
import com.bstek.dorado.view.widget.layout.DockLayoutConstraint;
import com.bstek.dorado.view.widget.layout.DockMode;
import com.bstek.dorado.view.widget.layout.HBoxLayout;
import com.bstek.dorado.view.widget.layout.HBoxLayoutConstraintSupport;
import com.bstek.dorado.view.widget.layout.Layout;
import com.bstek.dorado.view.widget.layout.LayoutConstraintSupport;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Jacky.gao
 * @since 2013-6-4
 */
public class GenericTaskToolbarListener extends GenericObjectListener<View> implements ApplicationContextAware{
	private Log log=LogFactory.getLog(getClass());
	private IBpmService bpmService;
	private ViewManagerHelper viewManagerHelper;
	private Collection<IToolbarContentProvider> providers;
	private Jbpm4HibernateDao hibernateDao;
	private static final String RENDER_TAG="render_generictasktoolbar_tag";
	private static final String RENDERED="rendered";
	@Override
	public boolean beforeInit(View view) throws Exception {
		return true;
	}

	@Override
	public void onInit(View view) throws Exception {
		HttpServletRequest request=DoradoContext.getCurrent().getRequest();
		String render=(String)request.getAttribute(RENDER_TAG);
		if(StringUtils.isNotEmpty(render) && render.equals(RENDERED)){
			return;
		}
		request.setAttribute(RENDER_TAG, RENDERED);
		ToolbarConfig config=loadToolbarContent();
		if(config==null || config.getContents().size()<1){
			return;
		}
		ToolBar toolbar=null;
		String toolBarId=Configure.getString("bdf2.jbpm4.genericTaskToolBarId");
		if(StringUtils.isNotEmpty(toolBarId)){
			toolbar=(ToolBar)view.getComponent(toolBarId);
		}else{
			toolbar=new ToolBar();
		}
		for(ToolbarContent content:config.getContents()){
			for(IToolbarContentProvider provider:providers){
				if(provider.key().equals(content.getToolbarContentProvider())){
					if(provider.isDisabled()){
						log.warn(content.getToolbarContentProvider() + " was disabled!");
					}else{
						String viewName=provider.getView();
						if(StringUtils.isNotEmpty(viewName)){
							ToolBar toolBar=this.copyViewAndRetriveToolBar(viewName, view);
							if(toolBar!=null){
								for(Control control:toolBar.getItems()){
									toolbar.addItem(control);
								}
							}
						}
					}
					break;
				}
				
			}
		}
		if(StringUtils.isNotEmpty(toolBarId)){
			return;
		}
		Layout layout=view.getLayout();
		LayoutConstraintSupport targetConstraint=null;
		if(layout instanceof DockLayout){
			DockLayoutConstraint constraint = new DockLayoutConstraint();
			if(config.getToolbarPosition().equals(ToolbarPosition.top)){
				constraint.setType(DockMode.top);
			}else{
				constraint.setType(DockMode.bottom);
			}
			targetConstraint=constraint;
		}
		if(layout instanceof HBoxLayout){
			HBoxLayoutConstraintSupport constraint = new HBoxLayoutConstraintSupport();
			if(config.getToolbarPosition().equals(ToolbarPosition.top)){
				constraint.setAlign(VerticalAlign.top);
			}else{
				constraint.setAlign(VerticalAlign.bottom);
			}
			targetConstraint=constraint;
		}	
		if(targetConstraint!=null){
			toolbar.setLayoutConstraint(targetConstraint);				
		}
		if(config.getToolbarPosition().equals(ToolbarPosition.top)){
			view.addChild(toolbar, 0);			
		}else{
			view.addChild(toolbar);						
		}				
	}

	private ToolBar copyViewAndRetriveToolBar(String targetViewName,View parentView){
		try{
			ToolBar toolBar=null;
			View targetView=viewManagerHelper.getViewConfigManager().getViewConfig(targetViewName).getView();
			for(Component component:targetView.getChildren()){
				component.setParent(null);
				if(!(component instanceof ToolBar)){
					parentView.addChild(component);					
				}else{
					toolBar=(ToolBar)component;
				}
			}
			return toolBar;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	private ToolbarConfig loadToolbarContent(){
		String taskId=DoradoContext.getCurrent().getRequest().getParameter("taskId");
		if(StringUtils.isEmpty(taskId)){
			return null;
		}
		Task task=bpmService.findTaskById(taskId);
		if(task==null){
			return null;
		}
		Execution execution=bpmService.findExecutionById(task.getExecutionId());
		if(execution==null){
			return null;
		}
		String processDefinitionId=execution.getProcessDefinitionId();
		String taskName=task.getName();
		String hql="from "+ToolbarConfig.class.getName()+" t where t.processDefinitionId=:processDefinitionId and t.taskName=:taskName";
		Session session=hibernateDao.getSessionFactory().openSession();
		try{
			List<ToolbarConfig> contents=session.createQuery(hql).setString("processDefinitionId", processDefinitionId).setString("taskName", taskName).list();
			ToolbarConfig config=null;
			if(contents.size()<1){
				return null;
			}
			config=contents.get(0);
			String configId=contents.get(0).getId();
			hql="from "+ToolbarContent.class.getName()+" t where t.toolbarConfigId=:configId order by order asc";
			config.setContents(session.createQuery(hql).setString("configId", configId).list());
			return config;
		}finally{
			session.close();
		}
	}
	
	public IBpmService getBpmService() {
		return bpmService;
	}

	public void setBpmService(IBpmService bpmService) {
		this.bpmService = bpmService;
	}
	public Jbpm4HibernateDao getHibernateDao() {
		return hibernateDao;
	}

	public void setHibernateDao(Jbpm4HibernateDao hibernateDao) {
		this.hibernateDao = hibernateDao;
	}

	public ViewManagerHelper getViewManagerHelper() {
		return viewManagerHelper;
	}

	public void setViewManagerHelper(ViewManagerHelper viewManagerHelper) {
		this.viewManagerHelper = viewManagerHelper;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		providers=applicationContext.getBeansOfType(IToolbarContentProvider.class).values();
	}
}
