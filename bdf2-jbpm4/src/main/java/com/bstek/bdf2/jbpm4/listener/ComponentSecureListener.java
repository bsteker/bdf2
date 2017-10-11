package com.bstek.bdf2.jbpm4.listener;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm4.model.ComponentControl;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.jbpm4.service.IComponentControlService;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.Component;

/**
 * @author Jacky.gao
 * @since 2013-7-1
 */
public class ComponentSecureListener extends GenericObjectListener<View> implements ApplicationContextAware{
	private IBpmService bpmService;
	private IComponentControlService componentControlService;
	private Collection<IFilter> filters;
	@Override
	public boolean beforeInit(View view) throws Exception {
		return true;
	}

	@Override
	public void onInit(View view) throws Exception {
		String taskId=ContextHolder.getRequest().getParameter("taskId");
		if(StringUtils.isEmpty(taskId)){
			return;
		}
		Task task=bpmService.getTaskService().getTask(taskId);
		if(task==null)return;
		String taskName=task.getName();
		String processDefinitionId=bpmService.getExecutionService().findExecutionById(task.getExecutionId()).getProcessDefinitionId();
		Collection<ComponentControl> componentControls=componentControlService.getComponentControls(processDefinitionId,taskName);
		if(componentControls.size()==0)return;
		for(ViewElement ve:view.getInnerElements()){
			if(ve==null || !(ve instanceof Component)){
				continue;
			}
			Component component=(Component)ve;
			this.filterComponent(component, componentControls);
		}
	}
	
	private void filterComponent(Component component,Collection<ComponentControl> componentControls){
		for(IFilter filter:filters){
			if(filter.support(component)){
				filter.filter(component, componentControls);
				break;
			}
		}
		if(component.getInnerElements()==null){
			return;
		}
		for(ViewElement ve:component.getInnerElements()){
			if(ve==null || !(ve instanceof Component)){
				continue;
			}
			Component c=(Component)ve;
			this.filterComponent(c, componentControls);
		}
	}

	public IBpmService getBpmService() {
		return bpmService;
	}

	public void setBpmService(IBpmService bpmService) {
		this.bpmService = bpmService;
	}

	public IComponentControlService getComponentControlService() {
		return componentControlService;
	}

	public void setComponentControlService(
			IComponentControlService componentControlService) {
		this.componentControlService = componentControlService;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		filters=applicationContext.getBeansOfType(IFilter.class).values();
	}
}
