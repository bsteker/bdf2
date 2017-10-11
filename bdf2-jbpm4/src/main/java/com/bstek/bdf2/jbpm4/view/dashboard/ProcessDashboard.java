package com.bstek.bdf2.jbpm4.view.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jbpm.api.Execution;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.ProcessInstanceQuery;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm4.Jbpm4HibernateDao;
import com.bstek.bdf2.jbpm4.model.ProcessDefinition;
import com.bstek.bdf2.jbpm4.model.Variable;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-3-22
 */
@Component("bdf2.jbpm4.processDashboard")
public class ProcessDashboard extends Jbpm4HibernateDao {
	@Autowired
	@Qualifier(IBpmService.BEAN_ID)
	private IBpmService bpmService;
	
	@DataProvider
	public void findProcessDefinition(Page<ProcessDefinitionImpl> page) throws Exception {
		IUser user=ContextHolder.getLoginUser();
		if(user==null){
			throw new RuntimeException("Please login first");
		}
		String hql="from "+ProcessDefinition.class.getName()+" where companyId=:companyId or companyId is null";
		String countHql="select count(*) "+hql;
		Page<ProcessDefinition> p=new Page<ProcessDefinition>(page.getPageSize(),page.getPageNo());
		Map<String,Object> map=new HashMap<String,Object>();
		
		String companyId=user.getCompanyId();
		if(StringUtils.isNotEmpty(getFixedCompanyId())){
			companyId=getFixedCompanyId();						
		}
		String fixedCompanyId=Configure.getString("bdf2.jbpm4.fixedCompanyId");
		if(StringUtils.isNotEmpty(fixedCompanyId)){
			companyId=fixedCompanyId;
		}
		map.put("companyId", companyId);			
		
		this.pagingQuery(p, hql+" order by createDate desc", countHql,map);
		page.setEntityCount(p.getEntityCount());
		List<ProcessDefinitionImpl> result=new ArrayList<ProcessDefinitionImpl>();
		for(ProcessDefinition pd:p.getEntities()){
			result.add((ProcessDefinitionImpl)bpmService.findProcessDefinitionById(pd.getId()));
		}
		page.setEntities(result);
	}
	
	@DataProvider
	public void findProcessInstance(Page<ProcessInstance> page,String processDefinitionId)throws Exception {
		ProcessInstanceQuery query = bpmService.getExecutionService().createProcessInstanceQuery();
		query.processDefinitionId(processDefinitionId);
		page.setEntityCount(new Long(query.count()).intValue());
		int start=(page.getPageNo()-1)*page.getPageSize();
		int end=page.getPageNo()*page.getPageSize();
		query.page(start, end);
		query.orderDesc("id");
		page.setEntities(query.list());
	}
	
	@DataProvider
	public Collection<Task> findTaskByExecutionId(String executionId) throws Exception {
		List<Task> list =bpmService.getTaskService().createTaskQuery()
				.processInstanceId(executionId)
				.list();
		return list;
	}
	
	@DataProvider
	public Collection<? extends Activity> findAllTaskActivitys(String taskId,
			String processDefinitionId) {
		Task task = bpmService.findTaskById(taskId);
		ProcessDefinitionImpl pd = (ProcessDefinitionImpl) bpmService.findProcessDefinitionById(processDefinitionId);
		List<? extends Activity> activityList = pd.getActivities();
		List<Activity> result = new ArrayList<Activity>();
		for (Activity ac : activityList) {
			if (!ac.getName().equals(task.getActivityName())
					&& (ac.getType().equals("task") || ac.getType().equals("end"))) {
				result.add(ac);
			}
		}
		return result;
	}

	@DataResolver
	public void startNewProcess(Collection<Variable> varaibles, String processDefinitionId)
			throws Exception {
		if (varaibles != null) {
			Map<String,Object> map=new HashMap<String,Object>();
			for(Variable v:varaibles){
				map.put(v.getName(),v.getValue());
			}
			bpmService.newProcessInstanceByProcessDefinitionId(processDefinitionId,map);
		}else{
			bpmService.newProcessInstanceByProcessDefinitionId(processDefinitionId);			
		}
	}
	
	
	@Expose
	public void deleteProcessDefinitionById(String deploymentId,String processDefinitionId) throws Exception {
		Session session=this.getSessionFactory().openSession();
		try{
			ProcessDefinition pd=new ProcessDefinition();
			pd.setId(processDefinitionId);
			session.delete(pd);
			bpmService.getRepositoryService().deleteDeploymentCascade(deploymentId);
		}finally{
			session.flush();
			session.close();
		}
	}
	
	private Map<String, Object> generateVariablesMap(Collection<Variable> variables) {
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		if (variables != null) {
			for (Variable v : variables) {
				variablesMap.put(v.getName(),v.getValue());
			}
		}
		return variablesMap;
	}

	/**
	 * 删除指定的流程实例根据流程实例的ID
	 * @param processInstanceId 流程实例ID
	 */
	@Expose
	public void deleteProcessInstance(String processInstanceId){
		bpmService.cancelTaskReminder(processInstanceId);
		bpmService.getExecutionService().deleteProcessInstanceCascade(processInstanceId);
	}

	/**
	 * 结束指定的流程实例
	 * @param processInstanceId 流程实例ID
	 */
	@Expose
	public void cancelProcessInstance(String processInstanceId){
		bpmService.cancelTaskReminder(processInstanceId);
		bpmService.getExecutionService().endProcessInstance(processInstanceId, ProcessInstance.STATE_ENDED);
	}

	/**
	 * 结束指定的Task，并传入相关参数
	 * @param variables 变量集合
	 * @param parameter 参数
	 */
	
	@DataResolver
	public void completeTask(Collection<Variable> variables, Map<String, Object> parameter){
		Map<String, Object> variablesMap = this.generateVariablesMap(variables);
		String taskId = (String) parameter.get("taskId");
		String path = (String) parameter.get("path");
		bpmService.cancelTaskReminder(bpmService.findTaskById(taskId));
		if (StringUtils.isNotEmpty(path)) {
			bpmService.completeTaskById(taskId, path, variablesMap);;
		} else {
			bpmService.completeTaskById(taskId,variablesMap);
		}
	}
	
	@DataProvider
	public Collection<Map<String, String>> findOutgoings(String taskId)
			throws Exception {
		Task task =bpmService.findTaskById(taskId);
		String executionId = task.getExecutionId();
		Execution execution = bpmService.findExecutionById(executionId);
		if (execution.findActiveActivityNames().size() > 0) {
			String activityName = execution.findActiveActivityNames().iterator().next();
			return this.findActivityInstance(activityName, execution.getProcessDefinitionId());
		}
		return null;
	}

	private List<Map<String, String>> findActivityInstance(String activityName, String pdid) {
		ProcessDefinitionImpl pd = (ProcessDefinitionImpl) bpmService
				.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(pdid)
				.list().get(0);
		Activity activity = pd.findActivity(activityName);
		List<? extends Transition> transList = activity.getOutgoingTransitions();
		List<Map<String, String>> transNameList = new ArrayList<Map<String, String>>();
		Map<String, String> temp = null;
		for (Transition trans : transList) {
			if (trans.getName() != null) {
				temp = new HashMap<String, String>();
				temp.put("name", trans.getName());
				transNameList.add(temp);
			}
		}
		return transNameList;
	}
	
	/**
	 * 任务节点跳转
	 * @param activityName 节点名称
	 * @param taskId 任务ID
	 */
	@Expose
	public void executeJump(String activityName, String taskId){
		bpmService.jumpToTargetActivity(taskId, activityName, null);
	}

	/**
	 * 更改任务处理人
	 * @param taskId 任务ID
	 * @param userId 用户ID
	 */
	@Expose
	public void changeTaskAssignee(String taskId, String userId){
		userId=userId.trim();
		bpmService.getTaskService().assignTask(taskId, userId);
	}
}
