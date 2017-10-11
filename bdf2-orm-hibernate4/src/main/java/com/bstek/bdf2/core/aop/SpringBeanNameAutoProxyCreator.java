package com.bstek.bdf2.core.aop;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;

/**
 * @author Jacky.gao
 * @since 2013年7月11日
 */
public class SpringBeanNameAutoProxyCreator extends BeanNameAutoProxyCreator {
	private static final long serialVersionUID = 8490241371290405005L;

	@Override
	public void setBeanNames(String[] beanNames) {
		for(String name:beanNames){
			if(name.trim().equals("*")){
				throw new IllegalArgumentException("Method interceptor bean name pattern is not allowed to use the ["+name+"]");
			}
		}
		super.setBeanNames(beanNames);			
	}
}
