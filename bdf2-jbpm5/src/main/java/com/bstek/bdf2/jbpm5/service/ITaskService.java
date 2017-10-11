package com.bstek.bdf2.jbpm5.service;

import java.util.List;
import java.util.Map;

import com.bstek.bdf2.jbpm5.model.Task;
import com.bstek.bdf2.jbpm5.model.TaskStatus;
import com.bstek.bdf2.jbpm5.task.TaskSummary;
import com.bstek.dorado.data.provider.Page;

/**
 * @author Jacky.gao
 * @since 2013-4-9
 */
public interface ITaskService {
	public static final String BEAN_ID="bdf2.jbpm5.taskService";
	/**
	 * 完成指定ID的任务，同时设置任务的完成人
	 * @param taskId 要完成的任务ID
	 * @param username 任务完成人用户名
	 */
	void complete(long taskId, String username);
	
	/**
	 * 完成指定ID的任务，同时设置任务的完成人及回写到流程实例中的变量集合
	 * @param taskId 任务ID
	 * @param username 用户名
	 * @param results 回写的变量集合
	 */
	void complete(long taskId, String username,Map<String,Object> results);
	/**
	 * 完成指定ID的任务
	 * @param taskId 要完成的任务ID
	 */
	void complete(long taskId);
	
	/**
	 * 完成指定ID的任务，同时设置回写到流程实例中的变量集合
	 * @param taskId 任务ID
	 * @param results 变量集合
	 */
	void complete(long taskId,Map<String,Object> results);
	/**
	 * 将指定ID的任务委派给另一个人去处理
	 * @param taskId 任务的ID
	 * @param username 当前任务处理人
	 * @param targetUsername 目标处理人
	 */
	void delegate(long taskId, String username, String targetUsername);
	
	/**
	 * 根据ID获取一个任务对象
	 * @param taskId 任务ID
	 * @return 返回任务对象
	 */
	Task getTask(long taskId);
	

	/**
	 * 根据任务ID，查找指定的流程变量的值
	 * @param taskId 任务ID
	 * @param variableName 要查找的流程变量名
	 * @return 流程变量值
	 */
	Object getProcessVariable(long taskId,String variableName);
	
	/**
	 * 认领一个任务
	 * @param taskId 要认领任务的ID
	 * @param username 认领任务的人的用户名
	 */
	void claim(long taskId, String username);
	/**
	 * 对认领后的任务进行释放，从而允许其它人认领
	 * @param taskId 要释放任务的ID
	 * @param username 任务的释放人
	 */
	void release(long taskId, String username);
	/**
	 * 开始处理一个任务
	 * @param taskId 任务的ID
	 * @param username 开始处理任务的人的用户名
	 */
	void start(long taskId, String username);
	/**
	 * 将一个任务挂起
	 * @param taskId 要挂起的任务的ID
	 * @param username 扶起任务的人的用户名
	 */
	void suspend(long taskId, String username);
	/**
	 * 让处于挂起状态的任务恢复正常
	 * @param taskId 要操作的任务的ID
	 * @param username
	 */
	void resume(long taskId, String username);
	/**
	 * 停止处理一个任务
	 * @param taskId 任务的ID
	 * @param username 停止任务的人的用户名
	 */
	void stop(long taskId, String username);
	/**
	 * 标明任务处理状态为失败
	 * @param taskId 要处理的任务ID
	 * @param username 处理人的用户名
	 */
	void fail(long taskId, String username,String faultMessage);
	/**
	 * 将任务状态改成退出
	 * @param taskId 任务的ID
	 * @param username 操作任务的人的用户名
	 */
	void exit(long taskId, String username);
	
	/**
	 * 更改任务处理人
	 * @param taskId 任务ID
	 * @param newOwner 新的处理人用户名
	 */
	void changeTaskOwner(long taskId,String newOwner);
	
	/**
	 * 获取直接分派给指定用户的待办任务列表
	 * @param username 用户名
	 * @return 任务列表
	 */
	List<TaskSummary> getTasksOwned(String username);
	/**
	 * 获取直接分派给指定用户指定状态的任务列表
	 * @param username 用户名
	 * @param status 任务状态集合
	 * @return 任务列表
	 */
	List<TaskSummary>  getTasksOwned(String username, List<TaskStatus> status);
	
	/**
	 * 分页获取指定用户的待办任务列表
	 * @param username 指定用户名
	 * @param page 分页对象
	 */
	void pagingTasksOwned(String username,Page<TaskSummary> page);
	/**
	 * 分页获取直接分派给指定用户指定状态的任务列表
	 * @param username 用户名
	 * @param page 分页对象
	 * @param status 状态集合
	 */
	void pagingTasksOwned(String username,Page<TaskSummary> page,List<TaskStatus> status);
	/**
	 * 获取指定用户可以处理的潜在任务列表
	 * @param username 用户名
	 * @return 任务集合
	 */
	List<TaskSummary> getTasksForPotentialOwner(String username);
	/**
	 * 分页获取指定用户可以处理的潜在任务列表
	 * @param username 用户名
	 * @param page 分页对象
	 */
	void getTasksForPotentialOwner(String username,Page<TaskSummary> page);
	/**
	 * 分页获取指定用户可以处理的指定状态的潜在任务列表
	 * @param username 用户名
	 * @param status 状态集合
	 * @return 任务列表
	 */
	List<TaskSummary> getTasksForPotentialOwner(String username,List<TaskStatus> status);
	/**
	 * 分页获取指定用户可以处理的指定状态的潜在任务列表
	 * @param username 用户名
	 * @param page 分页对象
	 * @param status 状态集合
	 */
	void getTasksForPotentialOwner(String username,Page<TaskSummary> page,List<TaskStatus> status);
	/**
	 * 获取指定流程实例下指定状态的任务列表
	 * @param processInstanceId 流程实例ID
	 * @param status 状态集合
	 * @return 任务列表
	 */
	List<TaskSummary> getTasksByStatusAndProcessId(long processInstanceId, List<TaskStatus> status);
	/**
	 * 获取指定流程实例下指定状态指定任务名的任务列表
	 * @param processInstanceId
	 * @param status
	 * @param taskName
	 * @return
	 */
	List<TaskSummary> getTasksByStatusByProcessIdAndTaskName(long processInstanceId, List<TaskStatus> status,String taskName);

	/**
	 * 根据任务ID查找其下所有子任务
	 * @param taskId 任务ID
	 * @return 子任务列表
	 */
	List<TaskSummary> getSubTasks(long taskId);
}
