package com.bstek.bdf2.jbpm4.pro.security.interceptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.Execution;
import org.jbpm.api.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.jbpm4.pro.security.component.ComponentFilter;
import com.bstek.bdf2.jbpm4.pro.security.component.IComponentFilter;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.web.DoradoContext;

/**
 * 一个用于对全局所有Component进行权限处理
 * 
 * @since 2013-1-31
 * @author Jacky.gao
 */
@org.springframework.stereotype.Component
public class WorkflowComponentListener extends GenericObjectListener<Component> implements ApplicationContextAware {
	private IBpmService bpmService;
	private ComponentFilter componentFilter;

	@Override
	public boolean beforeInit(Component component) throws Exception {
		return true;
	}

	@Override
	public void onInit(Component component) throws Exception {
		String executionId = DoradoContext.getAttachedRequest().getParameter("executionId");
		String taskId = DoradoContext.getAttachedRequest().getParameter("taskId");
		if (executionId != null && taskId != null) {
			Task task = bpmService.findTaskById(taskId);
			if (task != null) {
				Execution execution = bpmService.findExecutionByTaskId(taskId);
				String taskName = task.getActivityName();
				String processDefinitionId = execution.getProcessDefinitionId();
				componentFilter.filter(processDefinitionId, taskName, component, null);
			}
		}
	}

	private Map<String, IComponentFilter> componentFilterMap;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<IComponentFilter> componentFilterList = applicationContext.getBeansOfType(IComponentFilter.class)
				.values();
		componentFilterMap = new HashMap<String, IComponentFilter>(componentFilterList.size());
		for (IComponentFilter componentFilter : componentFilterList) {
			componentFilterMap.put(componentFilter.getSupportType(), componentFilter);
		}
	}

	public void setBpmService(IBpmService bpmService) {
		this.bpmService = bpmService;
	}

	public void setComponentFilter(ComponentFilter componentFilter) {
		this.componentFilter = componentFilter;
	}
}
