package com.bstek.bdf2.jbpm4.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.HistoryEvent;
import org.jbpm.pvm.internal.history.events.TaskActivityStart;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.IDao;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.ReminderJob;
import com.bstek.bdf2.jbpm4.model.ReminderJobState;
import com.bstek.bdf2.jbpm4.model.TaskConfig;
import com.bstek.bdf2.jbpm4.model.TodoTask;
import com.bstek.bdf2.jbpm4.model.TodoTaskType;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.provider.Page;

public class BpmServiceImpl extends Jbpm4HibernateDao implements IBpmService {
	private TaskService taskService;
	private RepositoryService repositoryService;
	private ExecutionService executionService;
	private HistoryService historyService;
	private ManagementService managementService;
	private String defaultProcessUsername;
	private ProcessEngine processEngine;

	public void createTaskReminder(Task task, String cronExpression, String messageTemplateId, String taskReminderId) {
		Session session = this.getSessionFactory().openSession();
		try {
			ReminderJob job = new ReminderJob();
			job.setId(UUID.randomUUID().toString());
			job.setCronExpression(cronExpression);
			job.setExecutionId(task.getExecutionId());
			job.setTaskId(task.getId());
			job.setState(ReminderJobState.running);
			job.setMessageTemplateId(messageTemplateId);
			job.setTaskName(task.getName());
			job.setTaskReminderId(taskReminderId);
			session.save(job);
		} finally {
			session.flush();
			session.close();
		}
	}
	
	public void cancelTaskReminder(String processInstanceId) {
		Session session = this.getSessionFactory().openSession();
		try {
			String hql = "from " + ReminderJob.class.getName() + " where executionId=:executionId";
			@SuppressWarnings("unchecked")
			List<ReminderJob> jobs = session.createQuery(hql).setString("executionId",processInstanceId).list();
			cancelTaskReminderJobs(session, jobs);
		} finally {
			session.flush();
			session.close();
		}
	}
	
	
	public void cancelTaskReminder(Task task) {
		Session session = this.getSessionFactory().openSession();
		try {
			String hql = "from " + ReminderJob.class.getName() + " where executionId=:executionId and taskId=:taskId";
			@SuppressWarnings("unchecked")
			List<ReminderJob> jobs = session.createQuery(hql).setString("executionId", task.getExecutionId()).setString("taskId", task.getId()).list();
			cancelTaskReminderJobs(session, jobs);
		} finally {
			session.flush();
			session.close();
		}
	}

	private void cancelTaskReminderJobs(Session session, List<ReminderJob> jobs) {
		for(ReminderJob job:jobs){
			job.setState(ReminderJobState.deleted);
			session.save(job);				
		}
	}
	
	
	public NewDeployment deployProcess(InputStream fin) throws IOException {
		ZipInputStream zinFin = new ZipInputStream(fin);
		try{
			NewDeployment deployment=this.getRepositoryService().createDeployment();
			deployment.addResourcesFromZipInputStream(zinFin);
			String deploymentId=deployment.deploy();
			String processDefinitionId=this.getRepositoryService().createProcessDefinitionQuery().deploymentId(deploymentId).uniqueResult().getId();
			addProcessDefinition(processDefinitionId);
			return deployment;
		}finally{
			zinFin.close();			
			fin.close();			
		}
	}
	
	private void addProcessDefinition(String processDefinitionId){
		Session session=this.getSessionFactory().openSession();
		IUser user=ContextHolder.getLoginUser();
		String companyId=null;
		if(user!=null){
			companyId=user.getCompanyId();
		}
		String fixedCompanyId=Configure.getString("bdf2.jbpm4.fixedCompanyId");
		if(StringUtils.isEmpty(fixedCompanyId)){
			fixedCompanyId=Configure.getString(IDao.FIXED_COMPANY_ID);
		}
		if(StringUtils.isNotEmpty(fixedCompanyId)){
			companyId=fixedCompanyId;
		}
		try{
			com.bstek.bdf2.jbpm4.model.ProcessDefinition pd=new com.bstek.bdf2.jbpm4.model.ProcessDefinition();
			pd.setCompanyId(companyId);
			pd.setId(processDefinitionId);
			pd.setCreateDate(new Date());
			session.save(pd);
		}finally{
			session.flush();
			session.close();
		}
	}

	/**
	 * 初始化任务完成时需要向流程上下文当中设置的变量
	 * 
	 * @param variablesMap
	 * @param task
	 */
	private void initCompleteTaskVariables(Map<String, Object> variablesMap, Task task) {
		ContextHolder.getLoginUser();
		IUser loginUser = ContextHolder.getLoginUser();
		String userName = this.defaultProcessUsername;
		if (loginUser != null) {
			userName = loginUser.getUsername();
		}
		variablesMap.put(LAST_PROCESS_USER, userName);
		variablesMap.put(LAST_PASS_ACTIVITY_NAME, task.getName());
	}

	public ProcessInstance newProcessInstanceByProcessDefinitionKey(String processDefinitionKey) {
		return this.newProcessInstanceByProcessDefinitionKey(processDefinitionKey, new HashMap<String, Object>());
	}

	public ProcessInstance newProcessInstanceByProcessDefinitionKey(String processDefinitionKey, Map<String, Object> map) {
		// 将流程发起人，及其所在部门、职位放到流程实例上下文当中
		this.initProcessInstanceStartVariables(map);
		ProcessInstance pi = this.getExecutionService().startProcessInstanceByKey(processDefinitionKey, map);
		this.getExecutionService().createVariables(pi.getId(), map, true);
		return pi;

	}

	public ProcessInstance newProcessInstanceByProcessDefinitionId(String processDefinitionId) {
		return this.newProcessInstanceByProcessDefinitionId(processDefinitionId, new HashMap<String, Object>());
	}

	public ProcessInstance newProcessInstanceByProcessDefinitionId(String processDefinitionId, Map<String, Object> map) {
		this.initProcessInstanceStartVariables(map);
		ProcessInstance pi = this.getExecutionService().startProcessInstanceById(processDefinitionId, map);
		this.getExecutionService().createVariables(pi.getId(), map, true);
		return pi;
	}

	private void initProcessInstanceStartVariables(Map<String, Object> map) {
		IUser loginUser = ContextHolder.getLoginUser();
		String userName = this.defaultProcessUsername;
		if (loginUser != null) {
			userName = loginUser.getUsername();
		}
		map.put(PROCESS_INSTANCE_START_DATE, new Date());// 流程开始日期
		map.put(PROCESS_INSTANCE_PROMOTER, userName);// 发起人

	}

	public ProcessDefinition findProcessDefinitionById(String processDefinitionId) {
		List<ProcessDefinition> list = this.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<HistoryActivityInstance> findHistoryActivityInstanceByExecutionId(String executionId) {
		return this.getHistoryService().createHistoryActivityInstanceQuery().executionId(executionId).list();
	}

	public Task findTaskById(String taskId) {
		return this.getTaskService().getTask(taskId);
	}

	public void addSubTask(String taskId, String IUser, String taskName) {
		TaskImpl parentTask = (TaskImpl) this.findTaskById(taskId);
		DbSession dbSession = EnvironmentImpl.getFromCurrent(DbSession.class);
		TaskImpl task = (TaskImpl) dbSession.createTask();
		task.setActivityName(parentTask.getActivityName());
		task.setAssignee(IUser);
		task.setCreateTime(new Date());
		task.setDescription("名为\"" + parentTask.getActivityName() + "\"上产生的子任务");
		task.setExecution(parentTask.getExecution());
		task.setProcessInstance(parentTask.getProcessInstance());
		if (taskName != null) {
			task.setName(taskName);
		} else {
			task.setName(parentTask.getActivityName() + "的子任务");
		}
		task.setSignalling(false);// 这里的true表示在该Task结束时为触发本类的signal方法，否则不会触发该方法
		parentTask.addSubTask(task);
		dbSession.save(task);
		HistoryEvent.fire(new TaskActivityStart(task), parentTask.getExecution());
	}

	private void completeTask(Task task, String transitionName, Map<String, Object> variables) {
		this.initCompleteTaskVariables(variables, task);
		this.getExecutionService().createVariables(task.getExecutionId(), variables, true);
		if (StringUtils.isNotEmpty(transitionName)) {
			this.getTaskService().completeTask(task.getId(), transitionName);
		} else {
			this.getTaskService().completeTask(task.getId());
		}
	}

	public void completeTask(Task task) {
		this.completeTask(task, null, new HashMap<String, Object>());
	}

	public void completeTask(Task task, Map<String, Object> variablesMap) {
		this.completeTask(task, null, variablesMap);
	}

	public void completeTaskById(String taskId, String transitionName, Map<String, Object> variablesMap) {
		Task task = this.findTaskById(taskId);
		this.completeTask(task, transitionName, variablesMap);
	}

	public void completeTaskById(String taskId) {
		Task task = this.findTaskById(taskId);
		this.completeTask(task);
	}

	public void completeTaskById(String taskId, Map<String, Object> variablesMap) {
		Task task = this.findTaskById(taskId);
		this.completeTask(task, variablesMap);
	}

	public void completeTaskById(String taskId, String transitionName) {
		this.completeTaskById(taskId, transitionName, new HashMap<String, Object>());
	}

	/**
	 * 找到当前任务节点默认的Transition
	 * 
	 * @param task
	 * @return 返回当前任务节点默认的Transition名称
	 */
	/*
	 * private String findTaskDefaultTransitionName(Task task) { TaskImpl t =
	 * (TaskImpl) task; String acName = t.getActivityName(); String transName =
	 * null; String execId = t.getExecutionId(); ExecutionImpl exec =
	 * (ExecutionImpl) this.findExecutionById(execId); ProcessDefinitionImpl pd
	 * = (ProcessDefinitionImpl) this.findProcessDefinitionById(exec
	 * .getProcessDefinitionId()); List<?> list = pd.getActivities(); for (int i
	 * = 0; i < list.size(); i++) { ActivityImpl ac = (ActivityImpl)
	 * list.get(i); if (ac.getName().equals(acName)) { transName =
	 * ac.getDefaultOutgoingTransition().getName(); break; } } return transName;
	 * }
	 */

	public Execution findExecutionById(String executionId) {
		return this.getExecutionService().findExecutionById(executionId);
	}

	public Execution findExecutionByTaskId(String taskId) {
		return this.getExecutionService().findExecutionById(this.findTaskById(taskId).getExecutionId());
	}

	public void rejectionToPrevTaskActivity(String taskId, String destinationActivityName, Map<String, Object> variableMap) {
		List<? extends Transition> transList = this.findIncomingTransitionsByTaskId(taskId);
		String acName = null;
		for (Transition trans : transList) {
			Activity ac = trans.getSource();
			if (ac.getType().equals("task")) {
				if (StringUtils.isNotEmpty(destinationActivityName) && destinationActivityName.equals(ac.getName())) {
					acName = ac.getName();
					break;
				} else {
					acName = ac.getName();
					break;
				}
			}
		}
		if (acName != null) {
			this.jumpToTargetActivity(taskId, acName, variableMap);
		} else if (StringUtils.isNotEmpty(destinationActivityName)) {
			throw new RuntimeException("The previous node has no named" + destinationActivityName + " to be back!");
		} else {
			throw new RuntimeException("Has no previous task node to be back!");
		}
	}

	public void jumpToTargetActivity(String taskId, String destinationActivityName, Map<String, Object> variableMap) {
		TaskImpl task = (TaskImpl) this.findTaskById(taskId);
		Execution execution = this.findExecutionById(task.getExecutionId());

		String transName = findTransitionNameFromSourceToDest(destinationActivityName, task, execution);
		if (variableMap == null) {
			variableMap = new HashMap<String, Object>();
		}
		if (StringUtils.isNotEmpty(transName)) {
			this.completeTask(task, transName, variableMap);
		} else {
			this.completeTask(task, variableMap);
		}
	}

	public void retrieveTask(String executionId) {
		List<Task> list = getTaskService().createTaskQuery().processInstanceId(executionId).list();
		if (list.size() > 0) {
			HistoryTaskInstanceImpl hisTask = this.findLastHistoryActiviyInstance(executionId);
			if (hisTask == null) {
				this.invaildProcess(list.get(0).getId());
			} else {
				Map<String, Object> variables = new HashMap<String, Object>();
				HashSet<String> userSet = new HashSet<String>();
				userSet.add(hisTask.getHistoryTask().getAssignee());
				Execution execution = this.findExecutionById(list.get(0).getExecutionId());
				String transitionName = this.findTransitionNameFromSourceToDest(hisTask.getActivityName(), list.get(0), execution);
				this.completeTask(list.get(0), transitionName, variables);
			}
		}
	}

	public void withdrawTask(String taskId) {
		Task task = this.findTaskById(taskId);
		HistoryTaskInstanceImpl hisTask = this.findLastHistoryActiviyInstance(task.getExecutionId());
		if (hisTask != null) {
			Map<String, Object> variables = new HashMap<String, Object>();
			HashSet<String> userSet = new HashSet<String>();
			userSet.add(hisTask.getHistoryTask().getAssignee());
			Execution execution = this.findExecutionById(task.getExecutionId());
			String transitionName = this.findTransitionNameFromSourceToDest(hisTask.getActivityName(), task, execution);
			this.completeTask(task, transitionName, variables);
		} else {
			this.invaildProcess(taskId);
		}
	}

	public void invaildProcess(String taskId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		Task task = this.findTaskById(taskId);
		Execution execution = this.findExecutionById(task.getExecutionId());

		ProcessDefinitionImpl pd = (ProcessDefinitionImpl) this.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(execution.getProcessDefinitionId()).uniqueResult();
		List<?> activities = pd.getActivities();
		ActivityImpl ac = null;
		for (int i = 0; i < activities.size(); i++) {
			ac = (ActivityImpl) activities.get(i);
			if (ac.getType().equals("end")) {
				break;
			}
		}
		String transitionName = this.findTransitionNameFromSourceToDest(ac.getName(), task, execution);
		this.completeTask(task, transitionName, variables);
	}

	/**
	 * 节点自由跳转,获取源节点与目标节点之间TransitionName,同时完成与当前节点并行的所有任务
	 * 
	 * @param destinationActivityName
	 * @param task
	 * @param execution
	 * @return 返回获取源节点与目标节点之间TransitionName名称
	 */
	private String findTransitionNameFromSourceToDest(String destinationActivityName, Task task, Execution execution) {
		// 将当前节点产生的任务除当前任务外其余全部complete
		String processInsanceId=execution.getProcessInstance()!=null?execution.getProcessInstance().getId():execution.getId();
		List<Task> taskList = this.getTaskService().createTaskQuery().processInstanceId(processInsanceId).activityName(task.getActivityName()).list();
		for (Task ta : taskList) {
			if (!ta.getId().equals(task.getId())) {
				this.completeTask(ta);
			}
		}
		ProcessDefinitionImpl pd = (ProcessDefinitionImpl) this.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(execution.getProcessDefinitionId()).uniqueResult();
		ActivityImpl sourceActivity = pd.getActivity(task.getActivityName());
		List<? extends Transition> transList = sourceActivity.getOutgoingTransitions();
		boolean existTrans = false;
		String transName = null;
		// 首先判断在源节点与目标节点之间是否存在Transition，如果存在就采用，否则就动态创建一条Transition
		for (Transition trans : transList) {
			if (trans.getDestination().getName().equals(destinationActivityName)) {
				transName = trans.getName();
				existTrans = true;
				break;
			}
		}
		if (!existTrans) {
			List<?> list = pd.getActivities();
			ActivityImpl destinationActivity = null;
			for (int i = 0; i < list.size(); i++) {
				ActivityImpl ac = (ActivityImpl) list.get(i);
				if (ac.getName().equals(destinationActivityName)) {
					destinationActivity = ac;
					break;
				}
			}
			if (destinationActivity != null) {
				transName = "dynamicTransition";
				TransitionImpl newTrans = sourceActivity.createOutgoingTransition();
				newTrans.setDestination(destinationActivity);
				// 防止重名
				transName += "_" + System.identityHashCode(newTrans);
				newTrans.setName(transName);
			} else {
				throw new RuntimeException("Current process has no named " + destinationActivityName + " node!");
			}
		}
		return transName;
	}

	/**
	 * 查询当前任务的前一节点
	 * 
	 * @param executionId
	 *            任务实例名称
	 * @return 返回HistoryTaskInstanceImpl对象
	 */
	private HistoryTaskInstanceImpl findLastHistoryActiviyInstance(String executionId) {
		List<HistoryActivityInstance> listHai = getHistoryService().createHistoryActivityInstanceQuery().processInstanceId(executionId).orderDesc("startTime").list();
		int count = 1;
		for (HistoryActivityInstance hisActivity : listHai) {
			if (hisActivity instanceof HistoryTaskInstanceImpl) {
				if (count != 1) {
					return (HistoryTaskInstanceImpl) hisActivity;
				}
				count++;
			}
		}
		return null;
	}

	public List<? extends Transition> findIncomingTransitionsByTaskId(String taskId) {
		Task task = this.findTaskById(taskId);
		Execution execution = this.findExecutionById(task.getExecutionId());
		ProcessDefinitionImpl pd = (ProcessDefinitionImpl) this.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(execution.getProcessDefinitionId()).uniqueResult();
		ActivityImpl activity = pd.getActivity(task.getActivityName());
		return activity.getIncomingTransitions();
	}

	public Map<String, Object> findTodoTaskCount(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		long assigneeCount = taskService.createTaskQuery().assignee(username).count();
		long candidateCount = taskService.createTaskQuery().candidate(username).count();
		map.put("personal", new Long(assigneeCount).intValue());
		map.put("group", new Long(candidateCount).intValue());
		return map;
	}

	public void findPersonalTodoTasks(Page<TodoTask> page, String username) {
		List<TodoTask> result = new ArrayList<TodoTask>();
		long count = taskService.createTaskQuery().assignee(username).count();
		page.setEntityCount(new Long(count).intValue());
		if (count > 0) {
			int firstResult = (page.getPageNo() - 1) * page.getPageSize();
			int maxResults = page.getPageNo() * page.getPageSize();
			List<Task> taskList = taskService.createTaskQuery().assignee(username).orderDesc(TaskQuery.PROPERTY_PRIORITY).page(firstResult, maxResults).list();
			for (Task task : taskList) {
				result.add(this.buildTodoTask(task, username, TodoTaskType.personal));
			}
			page.setEntities(result);
		}
	}

	public void findGroupTodoTasks(Page<TodoTask> page, String username) {
		List<TodoTask> result = new ArrayList<TodoTask>();
		long count = taskService.createTaskQuery().candidate(username).count();
		page.setEntityCount(new Long(count).intValue());
		if (count > 0) {
			int firstResult = (page.getPageNo() - 1) * page.getPageSize();
			int maxResults = page.getPageNo() * page.getPageSize();
			List<Task> taskList = taskService.createTaskQuery().candidate(username).orderDesc(TaskQuery.PROPERTY_PRIORITY).page(firstResult, maxResults).list();
			for (Task task : taskList) {
				result.add(this.buildTodoTask(task, username, TodoTaskType.group));
			}
			page.setEntities(result);
		}
	}

	private TodoTask buildTodoTask(Task t, String owner, TodoTaskType type) {
		TodoTask todo = new TodoTask();
		todo.setId(t.getId());
		todo.setName(t.getName());
		todo.setPrincipal(owner);
		todo.setType(type);
		todo.setCreateDate(t.getCreateTime());
		todo.setExecutionId(t.getExecutionId());
		todo.setDesc(t.getDescription());
		todo.setTask(t);
		todo.setBusinessId((String)executionService.getVariable(t.getExecutionId(), BUSINESS_ID));
		String processDefinitionId = executionService.findExecutionById(t.getExecutionId()).getProcessDefinitionId();
		String taskName = t.getName();
		String hql = "from " + TaskConfig.class.getName() + " where processDefinitionId=:processDefinitionId and taskName=:taskName";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processDefinitionId", processDefinitionId);
		map.put("taskName", taskName);
		List<TaskConfig> configs = this.query(hql, map);
		if (configs.size() > 0) {
			todo.setUrl(configs.get(0).getUrl());
		}
		return todo;
	}

	public void deleteDeployment(String deploymentId) {
		Session session=this.getSessionFactory().openSession();
		try{
			List<ProcessDefinition> list=this.getRepositoryService().createProcessDefinitionQuery().deploymentId(deploymentId).list();
			for(ProcessDefinition process:list){
				String processDefinitionId=process.getId();
				com.bstek.bdf2.jbpm4.model.ProcessDefinition pd=new com.bstek.bdf2.jbpm4.model.ProcessDefinition();
				pd.setId(processDefinitionId);
				session.delete(pd);
			}
			this.getRepositoryService().deleteDeploymentCascade(deploymentId);
		}finally{
			session.flush();
			session.close();
		}
	}
	
	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public ExecutionService getExecutionService() {
		return executionService;
	}

	public void setExecutionService(ExecutionService executionService) {
		this.executionService = executionService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public ManagementService getManagementService() {
		return managementService;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	public void setDefaultProcessUsername(String defaultProcessUsername) {
		this.defaultProcessUsername = defaultProcessUsername;
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
}
