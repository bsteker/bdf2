package com.bstek.bdf2.core.exception;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Dorado7的Ajax调用操作产生异常时的拦截类
 * @author Jacky.gao
 * @since 2013年11月25日
 */
public interface IAjaxExceptionHandler {
	/**
	 * 对异常的处理方法，可在这里记录异常信息，或者将异常信息包装后抛至前台显示给用户，通过做法如下：<br>
	 * String script="dorado.MessageBox.alert('业务操作异常，请与管理员联系!")";<br>
     * throw new ClientRunnableException(script);<br>
	 * @param exception
	 * @param invocation
	 */
	void handle(Throwable exception,MethodInvocation invocation);
	/**
	 * 是否支持处理当前异常对象
	 * @param exception 当前产生的异常对象
	 * @return 返回true表示支持处理，否则不支持处理
	 */
	boolean support(Throwable exception);
}
