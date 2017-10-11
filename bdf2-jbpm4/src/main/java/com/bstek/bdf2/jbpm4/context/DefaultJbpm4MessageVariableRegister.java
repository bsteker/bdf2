package com.bstek.bdf2.jbpm4.context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.TaskService;
import org.jbpm.api.task.Task;

import com.bstek.bdf2.core.model.MessageVariable;
import com.bstek.bdf2.jbpm4.service.IBpmService;

/**
 * @author Jacky.gao
 * @since 2013-3-25
 */
public class DefaultJbpm4MessageVariableRegister extends AbstractJbpm4MessageVariableRegister{
	private IBpmService bpmService;
	private String taskName="taskName";
	private String principal="principal";	
	private String taskCreateDate="taskCreateDate";	
	
	public Collection<MessageVariable> getMessageVariables() {
		List<MessageVariable> result=new ArrayList<MessageVariable>();
		MessageVariable var=new MessageVariable();
		var.setName(IBpmService.BUSINESS_ID);
		var.setDesc("业务数据ID");
		result.add(var);
		
		var=new MessageVariable();
		var.setName(taskCreateDate);
		var.setDesc("任务创建时间");
		result.add(var);
		var=new MessageVariable();
		var.setName(principal);
		var.setDesc("任务处理人");
		result.add(var);
		
		var=new MessageVariable();
		var.setName(taskName);
		var.setDesc("任务名称");
		result.add(var);
		
		var=new MessageVariable();
		var.setName(IBpmService.PROCESS_INSTANCE_PROMOTER);
		var.setDesc("流程实例发起人");
		result.add(var);
		return result;
	}

	public Map<String, String> fetchMessages(Task task,String assignee) {
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TaskService taskService=bpmService.getTaskService();
		Map<String, String> map=new HashMap<String,String>();
		map.put(principal, assignee);
		map.put(taskName, task.getName());
		map.put(taskCreateDate, sd.format(task.getCreateTime()));
		map.put(IBpmService.BUSINESS_ID,(String)taskService.getVariable(task.getId(), IBpmService.BUSINESS_ID));
		map.put(IBpmService.PROCESS_INSTANCE_PROMOTER,(String)taskService.getVariable(task.getId(), IBpmService.PROCESS_INSTANCE_PROMOTER));
		return map;
	}

	public IBpmService getBpmService() {
		return bpmService;
	}

	public void setBpmService(IBpmService bpmService) {
		this.bpmService = bpmService;
	}
}
