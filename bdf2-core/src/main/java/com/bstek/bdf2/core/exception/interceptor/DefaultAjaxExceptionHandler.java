package com.bstek.bdf2.core.exception.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.bstek.bdf2.core.exception.IAjaxExceptionHandler;

public class DefaultAjaxExceptionHandler implements IAjaxExceptionHandler {
	private Logger logger = Logger.getLogger(AjaxMethodInterceptor.class);

	public void handle(Throwable exception, MethodInvocation invocation) {
		logger.error(exception.getMessage(), exception);
	}

	public boolean support(Throwable exception) {
		return true;
	}

}
