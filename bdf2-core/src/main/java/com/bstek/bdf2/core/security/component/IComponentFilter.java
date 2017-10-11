package com.bstek.bdf2.core.security.component;

import com.bstek.bdf2.core.security.UserAuthentication;
import com.bstek.dorado.view.widget.Component;

/**
 * 实现对dorado当中所有组件进行权限过滤的接口类
 * @since 2013-1-30
 * @author Jacky.gao
 */
public interface IComponentFilter {
	void filter(String url,Component component,UserAuthentication authentication) throws Exception;
	boolean support(Component component);
}
