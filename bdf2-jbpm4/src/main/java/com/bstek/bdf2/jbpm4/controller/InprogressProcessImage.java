package com.bstek.bdf2.jbpm4.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.Execution;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-3-25
 */
public class InprogressProcessImage extends AbstractResolver implements InitializingBean{
	private IBpmService bpmService;
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest req,HttpServletResponse res) throws Exception {
		String taskId=req.getParameter("taskId");
		String executionId=req.getParameter("executionId");
		Map<String, Map<String, Object>> distinctActivityMap = new HashMap<String, Map<String, Object>>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Execution execution = null;
		if (StringUtils.isNotEmpty(executionId)) {
			execution = bpmService.findExecutionById(executionId);
		}
		Task task = null;
		if (StringUtils.isNotEmpty(taskId)) {
			task = bpmService.findTaskById(taskId);
			execution = bpmService.findExecutionById(task.getExecutionId());
		}
		String processInstanceId=execution.getProcessInstance().getId();
		String processDefinitionId=execution.getProcessDefinitionId();
		HistoryProcessInstance historyProcessInstance = bpmService.getHistoryService().createHistoryProcessInstanceQuery().processInstanceId(processInstanceId).uniqueResult();
		// 当前Execution所在节点的集合
		Set<String> activeActivityNameSet = execution.findActiveActivityNames();
		String topExecutionId = findTopParentExectionId(execution);
		List<HistoryActivityInstance> list = bpmService.getHistoryService()
				.createHistoryActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderDesc("startTime").list();
		// 输出所有的运行过的历史任务节点
		List<Map<String,String>> tipinfos=new ArrayList<Map<String,String>>();
		for (HistoryActivityInstance hisActivity : list) {
			String hisExecutionId = hisActivity.getExecutionId();
			boolean equalsFlag = true;
			if (hisActivity instanceof HistoryTaskInstanceImpl) {
				HistoryTaskInstanceImpl hisTask = (HistoryTaskInstanceImpl) hisActivity;
				String assignee = hisTask.getHistoryTask().getAssignee();
				if ((hisExecutionId.startsWith(topExecutionId) && equalsFlag) || historyProcessInstance != null) {
					boolean outputFlag = true;
					// 如果有Execution进来的话，
					for (String acName : activeActivityNameSet) {
						if (acName.equals(hisTask.getActivityName())) {
							//表明当前历史节点就是流程实例当前所处节点
							outputFlag = false;
							break;
						}
					}
					if (StringUtils.isNotEmpty(taskId)) {
						if (taskId.equals(hisTask.getHistoryTask().getId())) {
							outputFlag = false;
						}
					}
					if (hisTask.getEndTime() == null) {
						outputFlag = false;
					}
					String startTime = (hisTask.getStartTime() != null) ? sd.format(hisTask.getStartTime()) :null;
					String endTime = (hisTask.getEndTime() != null) ? "任务已结束，结束时间为 "+sd.format(hisTask.getEndTime()) : "任务未结束";
					StringBuffer sb = new StringBuffer();
					if(StringUtils.isNotEmpty(assignee)){
						sb.append("该任务的处理人为：" + assignee+"<br>");						
					}
					if(StringUtils.isNotEmpty(startTime)){
						sb.append("该任务的开始时间为：" + startTime+"<br>");						
					}
					if(StringUtils.isNotEmpty(endTime)){
						sb.append("该任务状态：" + endTime+"<br>");						
					}
					String divId="div"+UUID.randomUUID().toString().replaceAll("-", "");
					Map<String,String> map=new HashMap<String,String>();
					map.put("divId",divId);
					map.put("info",sb.toString());
					tipinfos.add(map);
					outputHistoryNodeDiv(distinctActivityMap, processDefinitionId,hisTask, assignee,!outputFlag, sb.toString(),divId);
				}
			} else {
				boolean outputFlag = true;
				// 如果有Execution进来的话，
				for (String acName : activeActivityNameSet) {
					if (acName.equals(hisActivity.getActivityName())) {
						outputFlag = false;
						break;
					}
				}
				outputHistoryNodeDiv(distinctActivityMap, processDefinitionId,hisActivity, null, !outputFlag, null,null);
			}
		}
		res.setContentType("text/html;charset=utf-8");
		String imageURL=req.getContextPath()+"/dorado/bdf2/jbpm4/generate.processimage?processDefinitionId="+processDefinitionId;
		StringBuffer script = new StringBuffer();
		script.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+req.getContextPath()+"/dorado/res/dorado/resources/simpletip.css\"  />");
		script.append("<script language=\"javascript\" type=\"text/javascript\" src=\""+req.getContextPath()+"/dorado/client/jquery.dpkg\"></script>");
		script.append("<script language=\"javascript\" type=\"text/javascript\" src=\""+req.getContextPath()+"/dorado/res/dorado/scripts/jquery.simpletip-1.3.1.min.js\"></script>");
		script.append("<script language=\"javascript\" type=\"text/javascript\">");
		script.append("$(document).ready(function(){");
		script.append("$(\"<img/>\").attr(\"src\",\""
				+ imageURL
				+ "\").load(function(){"
				+ "$(\"#_inprogressProcessImageDiv\").css({width:(this.width+10)+\"px\",height:(this.height+10)+\"px\"});"
				+ "});");
		for(Map<String,String> map:tipinfos){
			script.append("$(\"#"+map.get("divId")+"\").simpletip({fixed:false,content:\""+map.get("info")+"\"});");			
		}
		script.append("});");
		script.append("</script>");
		script.append("<div id=\"_inprogressProcessImageDiv\" style=\"z-index:300;position:relative;background:url("+ imageURL + ") no-repeat\">" + buildDiv(distinctActivityMap) + "</div>");
		distinctActivityMap.clear();
		PrintWriter out = res.getWriter();
		try{
			out.write(script.toString());
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private String buildDiv(Map<String, Map<String, Object>> distinctActivityMap) {
		StringBuffer content = new StringBuffer();
		if (distinctActivityMap.size() > 0) {
			int top = 0;
			for (Map<String, Object> activityMap : distinctActivityMap.values()) {
				String message = (String) activityMap.get("message");
				Boolean isCurrent = (Boolean) activityMap.get("isCurrent");
				Map<String, Object> styleMap = null;
				if (message != null) {
					styleMap = (Map<String, Object>) activityMap.get("messageDivStyle");
					if (isCurrent) {
						styleMap.put("color", "red");
					}
					top = buildDivStyle(content, styleMap, top, message);
				}
				styleMap = (Map<String, Object>) activityMap.get("bgDivStyle");
				if (styleMap != null) {
					top = buildDivStyle(content, styleMap, top, "&nbsp;");
				}
				styleMap = (Map<String, Object>) activityMap.get("countDivStyle");
				if (styleMap != null) {
					int count = (Integer) activityMap.get("count");
					if (isCurrent) {
						count--;
					}
					if(count>0){
						top = buildDivStyle(content, styleMap, top, "[" + count + "]");						
					}
				}
			}
		}
		return content.toString();
	}

	private int buildDivStyle(StringBuffer content, Map<String, Object> styleMap, int top,String message) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div style=\"");
		int height = (Integer) styleMap.get("height");
		for (Entry<String, Object> entry : styleMap.entrySet()) {
			if (entry.getKey().equals("left")) {
				sb.append(entry.getKey() + ":" + entry.getValue().toString() + "px;");
			} else if (entry.getKey().equals("top")) {
				int currentTop = (Integer) entry.getValue();
				currentTop = currentTop - top;
				top += height;
				sb.append("top:" + currentTop + "px;");
			} else if (entry.getKey().equals("width")) {
				sb.append("width:" + entry.getValue().toString() + "px;");
			} else if (entry.getKey().equals("height")) {
				sb.append("height:" + entry.getValue().toString() + "px;");
			} else {
				sb.append(entry.getKey() + ":" + entry.getValue().toString() + ";");
			}
		}
		sb.append("\"");
		if (styleMap.get("tip") != null) {
			sb.append(" id=\""+styleMap.get("divId")+"\"\"");
		}
		sb.append(">" + message + "</div>");
		content.append(sb.toString());
		return top;
	}

	
	private void outputHistoryNodeDiv(Map<String, Map<String, Object>> distinctActivityMap,String processDefinitionId,
			HistoryActivityInstance hisActivity, String msg, boolean isCurrent,String tip,String divId) {

		String acName = hisActivity.getActivityName();
		if (distinctActivityMap.get(acName) == null) {
			Map<String, Object> activityMap = new HashMap<String, Object>();
			ActivityCoordinates activityAC = bpmService.getRepositoryService().getActivityCoordinates(processDefinitionId, acName);

			int left = activityAC.getX();
			int top = activityAC.getY();
			int width = activityAC.getWidth();
			int height = activityAC.getHeight();
			if (msg != null) {
				Map<String, Object> style = new HashMap<String, Object>();
				style.put("text-align", "center");
				style.put("position", "relative");
				style.put("left", left);
				style.put("top", top-18);
				style.put("height", 20);
				style.put("width", width);
				style.put("color", "green");
				style.put("cursor", "pointer");
				style.put("tip", tip);
				style.put("divId",divId);
				style.put("z-index", "1000000");
				
				activityMap.put("messageDivStyle", style);
				activityMap.put("message", msg);
			}
			Map<String, Object> style = new HashMap<String, Object>();
			style.put("position", "relative");
			style.put("left", left-5);
			style.put("top", top-15);
			style.put("font-size","10px");
			style.put("height", 16);
			style.put("width", width);
			style.put("color", "green");
			style.put("z-index", "224");
			activityMap.put("countDivStyle", style);

			style = new HashMap<String, Object>();
			style.put("position", "relative");
			style.put("left", left);
			style.put("top", top);
			style.put("height",height);
			style.put("width", width);
			style.put("color", "green");
			style.put("z-index", "225");
			style.put("opacity", "0.4");
			style.put("filter", "alpha(opacity=50)");
			if (isCurrent) {
				style.put("background-color","yellow");
			} else {
				style.put("background-color","green");
			}
			activityMap.put("bgDivStyle", style);
			activityMap.put("count", 1);
			activityMap.put("isCurrent", isCurrent);
			distinctActivityMap.put(acName, activityMap);
		} else {
			Map<String, Object> activityMap = distinctActivityMap.get(acName);
			Integer count = (Integer) activityMap.get("count");
			activityMap.put("count", ++count);
			activityMap.put("isCurrent", isCurrent);
		}
	}

	
	/**
	 * 找到指定的Execution的最顶层的Execution ID
	 * @param execution  给定的Execution对象
	 * @return String 最顶层的Execution ID
	 */
	public String findTopParentExectionId(Execution execution) {
		Execution parentExecution = execution.getParent();
		if (parentExecution != null) {
			return findTopParentExectionId(parentExecution);
		} else {
			return execution.getId();
		}
	}


	public void afterPropertiesSet() throws Exception {
		Map<String,IBpmService> map=this.getApplicationContext().getBeansOfType(IBpmService.class);
		if(map.size()==0){
			map=this.getApplicationContext().getParent().getBeansOfType(IBpmService.class);
		}
		bpmService=map.values().iterator().next();
	}
}
