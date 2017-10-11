package com.bstek.bdf2.jbpm4.pro.security.component;

import com.bstek.dorado.view.widget.Component;

/**
 * 实现对dorado当中所有组件进行权限过滤的接口类
 * 
 * @since 2013-1-30
 * @author Jacky.gao
 */
public interface IComponentFilter {
	void filter(String processDefinitionId, String taskName, Component component) throws Exception;

	String getSupportType();

}
