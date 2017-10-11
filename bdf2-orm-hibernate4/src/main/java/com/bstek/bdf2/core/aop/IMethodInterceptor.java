package com.bstek.bdf2.core.aop;

import java.lang.reflect.Method;

/**
 * 实现该接口用于拦截用户感兴趣的业务方法调用，比如作业务操作审计等
 * @author Jacky.gao
 * @since 2013年7月11日
 */
public interface IMethodInterceptor {
	/**
	 * 是否支持当前方法调用
	 * @param objectClass 调用类class
	 * @param method 调用的方法对象
	 * @return true表示支持，false表示不支持
	 */
	boolean support(Class<?> objectClass,Method method);
	/**
	 * 在方法调用之前拦截,如不需要保持实现类中该方法为空即可
	 * @param objectClass 调用类class
	 * @param method 调用的方法对象
	 * @param arguments 方法调用时采用的参数集合
	 * @throws Exception
	 */
	void doBefore(Class<?> objectClass,Method method,Object[] arguments) throws Exception;
	/**
	 * 在方法调用之后拦截,如不需要保持实现类中该方法为空即可
	 * @param objectClass 调用类class
	 * @param method 调用的方法对象
	 * @param arguments 方法调用时采用的参数集合
	 * @param returnValue 方法调用完成后的返回值
	 * @throws Exception
	 */
	void doAfter(Class<?> objectClass,Method method,Object[] arguments,Object returnValue) throws Exception;
}
