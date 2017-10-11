package com.bstek.bdf2.core.aop;

import java.lang.reflect.Method;
import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author Jacky.gao
 * @since 2013年7月11日
 */
public class GlobalMethodInterceptor implements MethodInterceptor,ApplicationContextAware {
	private Collection<IMethodInterceptor> methodInterceptors;
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method=invocation.getMethod();
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
		this.doInterceptors(true, targetClass, method, invocation.getArguments(), null);
		Object retVal = invocation.proceed();
		this.doInterceptors(false, targetClass, method, invocation.getArguments(), retVal);
		return retVal;
	}
	
	private void doInterceptors(boolean isBefore,Class<?> objectClass,Method method,Object[] arguments,Object returnValue) throws Exception{
		for(IMethodInterceptor interceptor:methodInterceptors){
			if(!interceptor.support(objectClass, method)){
				continue;
			}
			if(isBefore){
				interceptor.doBefore(objectClass, method, arguments);
			}else{
				interceptor.doAfter(objectClass, method, arguments, returnValue);				
			}
		}
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		methodInterceptors=applicationContext.getBeansOfType(IMethodInterceptor.class).values();
	}
}
