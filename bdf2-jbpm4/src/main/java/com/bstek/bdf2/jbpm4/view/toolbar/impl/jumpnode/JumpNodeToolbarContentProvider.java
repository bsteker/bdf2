package com.bstek.bdf2.jbpm4.view.toolbar.impl.jumpnode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.model.Activity;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.bdf2.jbpm4.view.toolbar.IToolbarContentProvider;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;

/**
 * @author Jacky.gao
 * @since 2013-6-3
 */
@Component("bdf2.jbpm4.jumpNodeToolbarContentProvider")
public class JumpNodeToolbarContentProvider implements IToolbarContentProvider {
	@Value("${bdf2.jbpm4.disabledJumpNodeToolbarContentProvider}")
	private boolean disabled;
	@Autowired
	@Qualifier(IBpmService.BEAN_ID)
	private IBpmService bpmService;
	public String getView(){
		return "bdf2.jbpm4.view.toolbar.impl.jumpnode.JumpNodeToolbarContentProvider";
	}

	public String key() {
		return "SimpleJumpToOtherTaskNode";
	}

	public String desc() {
		return "直接跳转到其它任务节点";
	}

	public boolean isDisabled() {
		return disabled;
	}

	@DataProvider
	public Collection<Map<String,Object>> loadJumpTaskNodes(String taskId){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Task task=bpmService.findTaskById(taskId);
		if(task==null){
			return list;
		}
		String processDefinitionId=bpmService.findExecutionById(task.getExecutionId()).getProcessDefinitionId();
		ProcessDefinitionImpl pd = (ProcessDefinitionImpl) bpmService.findProcessDefinitionById(processDefinitionId);
		List<? extends Activity> activityList = pd.getActivities();
		for (Activity ac : activityList) {
			String type=ac.getType();
			String name=ac.getName();
			if (!name.equals(task.getActivityName()) && (type.equals("task"))) {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("name",name);
				list.add(map);
			}
		}
		return list;
	}
	
	@Expose
	public void jumpToTargetNode(String targetNode,String taskId){
		bpmService.jumpToTargetActivity(taskId, targetNode, null);
	}
}
