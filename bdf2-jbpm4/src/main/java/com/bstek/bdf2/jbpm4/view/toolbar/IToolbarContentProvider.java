package com.bstek.bdf2.jbpm4.view.toolbar;


/**
 * @author Jacky.gao
 * @since 2013-6-3
 * 用于为流程中任务处理页面的通用工具栏提供具体内容
 */
public interface IToolbarContentProvider {
	/**
	 * 返回包含要放置到通用工具栏上的具体内容所在的具体dorado7的view名称，
	 * 比如bdf2.jbpm4.view.toolbar.impl.completetask.CompleteTaskToolbarContentProvider，这个系统默认提供的用于完成任务的内容提供者页面
	 * @return 返回一个具体view的名称，不包含.d
	 */
	String getView();
	/**
	 * @return 返回能代表当前这个提供者的key，一个有意义的字符串，比如SimpleCompleteTask
	 */
	String key();
	/**
	 * @return 返回一段描述信息，用于说明这个提供者作用
	 */
	String desc();
	/**
	 * @return 返回这个提供者是否被禁用，返回true，那么这个提供者将不能使用
	 */
	boolean isDisabled();
}
