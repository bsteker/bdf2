package com.bstek.bdf2.jbpm4.view.toolbar.impl.completetask;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.jbpm4.view.toolbar.IToolbarContentProvider;
import com.bstek.dorado.annotation.Expose;

/**
 * @author Jacky.gao
 * @since 2013-6-3
 */
@Component("bdf2.jbpm4.completeTaskToolbarContentProvider")
public class CompleteTaskToolbarContentProvider implements
		IToolbarContentProvider {
	@Value("${bdf2.jbpm4.disabledCompleteTaskToolbarContentProvider}")
	private boolean disabled;
	@Autowired
	@Qualifier(IBpmService.BEAN_ID)
	private IBpmService bpmService;
	public String getView(){
		return "bdf2.jbpm4.view.toolbar.impl.completetask.CompleteTaskToolbarContentProvider";
	}
	
	public String key() {
		return "SimpleCompleteTask";
	}

	public String desc() {
		return "直接完成任务";
	}

	public boolean isDisabled() {
		return disabled;
	}

	@Expose
	public void completeTask(String taskId){
		bpmService.completeTaskById(taskId);
	}
}
