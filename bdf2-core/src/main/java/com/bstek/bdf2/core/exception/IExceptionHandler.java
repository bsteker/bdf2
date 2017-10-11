package com.bstek.bdf2.core.exception;

import org.springframework.security.web.context.HttpRequestResponseHolder;

/**
 * @since 2013-1-27
 * @author Jacky.gao
 */
public interface IExceptionHandler {
	void handle(HttpRequestResponseHolder requestResponseHolder,Throwable exception);
	boolean support(Throwable exception);
}
