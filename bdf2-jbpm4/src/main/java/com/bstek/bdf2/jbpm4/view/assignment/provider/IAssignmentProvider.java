package com.bstek.bdf2.jbpm4.view.assignment.provider;

import java.util.Collection;

import org.jbpm.api.model.OpenExecution;

import com.bstek.bdf2.jbpm4.model.AssignmentInfo;
import com.bstek.bdf2.jbpm4.model.PrincipalDef;

/**
 * @author Jacky.gao
 * @since 2013-3-23
 */
public interface IAssignmentProvider {
	/**
	 * @return 返回配置页面所在的URL
	 */
	String getUrl();
	/**
	 * @return 返回配置类型的ID
	 */
	String getTypeId();
	/**
	 * @return 返回与配置类型ID对象的Name描述
	 */
	String getTypeName();
	
	/**
	 * @param assignmentDefId 任务分配定义的ID
	 * @return 根据任务分配定义的ID返回与之对应的任务分配信息
	 */
	Collection<AssignmentInfo> getAssignmentInfos(String assignmentDefId);
	
	/**
	 * 删除指定分配定义下的分配定义信息
	 * @param assignmentDefId 分配定义信息的ID
	 */
	void deleteAssignmentInfos(String assignmentDefId);
	
	/**
	 * @param assignmentDefId 任务分配定义的ID
	 * @param execution	当前流程实例执行上下文对象
	 * @return 根据任务分配定义的ID返回所有与之对应的任务处理人的PrincipalDef集合
	 */
	Collection<PrincipalDef> getTaskPrincipals(String assignmentDefId,OpenExecution execution);
	
	/**
	 * @return 是否禁用
	 */
	boolean isDisabled();
}
