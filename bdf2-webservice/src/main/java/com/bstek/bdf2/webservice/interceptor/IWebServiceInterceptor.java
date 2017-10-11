package com.bstek.bdf2.webservice.interceptor;

import org.springframework.ws.context.MessageContext;

/**
 * @author Jacky.gao
 * @since 2013-4-27
 */
public interface IWebServiceInterceptor {
	void handleRequest(MessageContext messageContext, Object endpoint) throws Exception;
	void handleResponse(MessageContext messageContext, Object endpoint) throws Exception;
	void handleFault(MessageContext messageContext, Object endpoint) throws Exception;
	void afterCompletion(MessageContext messageContext, Object endpoint,Exception ex) throws Exception;
}
