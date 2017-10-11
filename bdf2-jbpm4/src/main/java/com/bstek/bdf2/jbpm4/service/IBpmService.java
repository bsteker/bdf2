package com.bstek.bdf2.jbpm4.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;
import org.springframework.context.ApplicationContext;

import com.bstek.bdf2.jbpm4.model.TodoTask;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-3-21
 */
public interface IBpmService {
	public static final String BEAN_ID = "bdf2.jbpm4.bpmService";
	public static final String LAST_PASS_ACTIVITY_NAME = "last_pass_activity_name";// 流程实例最后一次经过的流程节点名称
	public static final String LAST_PROCESS_USER = "last_process_User";// 流程实例最后一次的审批人
	public static final String LAST_process_USER_DEPT_IDS = "last_process_User_dept";// 流程实例最后一次的审批人所在部门
	public static final String BUSINESS_ID = "businessId";// 在流程实例当中存在的业务数据的ID
	public static final String PROCESS_INSTANCE_START_DATE = "process_instance_start_date";
	public static final String PROCESS_INSTANCE_PROMOTER = "process_instance_promoter";

	void findPersonalTodoTasks(Page<TodoTask> page,String username);
	
	void findGroupTodoTasks(Page<TodoTask> page,String username);
	
	void createTaskReminder(Task task,String cronExpression,String messageTemplateId,String taskReminderId);
	
	void cancelTaskReminder(Task task);
	
	void cancelTaskReminder(String processInstanceId);
	
	SessionFactory getSessionFactory();
	
	ApplicationContext getApplicationContext();
	
	Map<String,Object> findTodoTaskCount(String username);
	
	NewDeployment deployProcess(InputStream fin) throws IOException;
	
	void deleteDeployment(String deploymentId);
	
	ProcessInstance newProcessInstanceByProcessDefinitionKey(
			String processDefinitionKey);

	ProcessInstance newProcessInstanceByProcessDefinitionId(
			String processDefinitionId);

	ProcessInstance newProcessInstanceByProcessDefinitionKey(
			String processDefinitionKey, Map<String, Object> variablesMap);

	ProcessInstance newProcessInstanceByProcessDefinitionId(
			String processDefinitionId, Map<String, Object> variablesMap);

	Execution findExecutionById(String executionId);

	Execution findExecutionByTaskId(String taskId);

	List<HistoryActivityInstance> findHistoryActivityInstanceByExecutionId(
			String executionId);

	ProcessDefinition findProcessDefinitionById(String processDefinitionId);

	Task findTaskById(String taskId);

	void addSubTask(String taskId, String IUser, String taskName);

	void completeTaskById(String taskId);

	void completeTaskById(String taskId, String transitionName,
			Map<String, Object> variablesMap);

	void completeTaskById(String taskId, String transitionName);

	void completeTaskById(String taskId, Map<String, Object> variablesMap);

	void completeTask(Task task);

	void completeTask(Task task, Map<String, Object> variablesMap);

	void rejectionToPrevTaskActivity(String taskId,
			String targetTaskActivityName, Map<String, Object> variableMap);

	void jumpToTargetActivity(String taskId, String targetTaskActivityName,
			Map<String, Object> variableMap);

	void retrieveTask(String executionId);

	void withdrawTask(String taskId);

	void invaildProcess(String taskId);

	List<? extends Transition> findIncomingTransitionsByTaskId(String taskId);

	ManagementService getManagementService();

	TaskService getTaskService();

	ExecutionService getExecutionService();

	RepositoryService getRepositoryService();

	HistoryService getHistoryService();
	
	ProcessEngine getProcessEngine();
}
