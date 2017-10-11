package com.bstek.bdf2.jbpm5.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.jbpm5.BpmJpaDao;
import com.bstek.bdf2.jbpm5.model.Task;
import com.bstek.bdf2.jbpm5.service.IKnowledgeService;
import com.bstek.bdf2.jbpm5.service.IProcessService;
import com.bstek.bdf2.jbpm5.service.ITaskService;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
@Component("bdf2.jbpm5.dashboard")
public class Dashboard extends BpmJpaDao {
	@Autowired
	@Qualifier(IKnowledgeService.BEAN_ID)
	private IKnowledgeService knowledgeService;
	
	@Autowired
	@Qualifier(IProcessService.BEAN_ID)
	private IProcessService processService;
	
	@Autowired
	@Qualifier(ITaskService.BEAN_ID)
	private ITaskService taskService;
	
	@DataProvider
	public Collection<org.drools.definition.process.Process> loadProcesses(){
		return knowledgeService.getKnowledgeBase().getProcesses();
	}
	
	@Expose
	public void operateTask(long taskId,String operate){
		if(operate.equals("claim")){
			taskService.claim(taskId, ContextHolder.getLoginUserName());
		}else if(operate.equals("release")){
			taskService.release(taskId, ContextHolder.getLoginUserName());
		}else if(operate.equals("start")){
			taskService.start(taskId, ContextHolder.getLoginUserName());
		}else if(operate.equals("complete")){
			taskService.complete(taskId);
		}
	}
	
	@Expose
	public void changeTaskOwner(long taskId,String newOwner){
		taskService.changeTaskOwner(taskId, newOwner);
	}
	
	@Expose
	public void abortProcessInstance(long processInstanceId){
		processService.abortProcessInstance(processInstanceId);
	}
	
	@DataProvider
	public void loadProcessInstances(Page<ProcessInstanceLog> page,Criteria criteria,String processId){
		String ql="from "+ProcessInstanceLog.class.getName()+" p where p.processId=:processId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("processId", processId);
		ParseResult result=this.parseCriteria(criteria, true, "p");
		if(result!=null){
			ql+=" and "+result.getAssemblySql();
			map.putAll(result.getValueMap());
		}
		String countQL="select count(*) "+ql;
		ql+=" order by p.start desc,p.end desc";
		this.pagingQuery(ql, countQL, page, map);
	}
	
	@DataProvider
	public Collection<NodeInstanceLog> loadNodes(long processInstanceId){
		String ql="from "+NodeInstanceLog.class.getName()+" n where n.processInstanceId=:processInstanceId order by n.nodeName asc,n.date desc";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("processInstanceId", processInstanceId);
		return this.find(ql, map);
	}
	
	@DataProvider
	public Collection<Task> loadTasks(long processInstanceId){
		String ql="from "+Task.class.getName()+" t where t.processInstanceId=:processInstanceId order by t.end asc,t.create desc";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("processInstanceId", processInstanceId);
		return this.find(ql, map);
	}
}
