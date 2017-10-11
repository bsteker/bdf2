package com.bstek.bdf2.webservice.interceptor;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

/**
 * @author Jacky.gao
 * @since 2013-4-27
 */
public class GenericWebServiceInterceptor implements EndpointInterceptor,ApplicationContextAware{
	private Collection<IWebServiceInterceptor> interceptors;
	public boolean handleRequest(MessageContext messageContext, Object endpoint)
			throws Exception {
		for(IWebServiceInterceptor interceptor:interceptors){
			interceptor.handleRequest(messageContext, endpoint);
		}
		return true;
	}

	public boolean handleResponse(MessageContext messageContext, Object endpoint)
			throws Exception {
		for(IWebServiceInterceptor interceptor:interceptors){
			interceptor.handleResponse(messageContext, endpoint);
		}
		return true;
	}

	public boolean handleFault(MessageContext messageContext, Object endpoint)
			throws Exception {
		for(IWebServiceInterceptor interceptor:interceptors){
			interceptor.handleFault(messageContext, endpoint);
		}
		return true;
	}

	public void afterCompletion(MessageContext messageContext, Object endpoint,
			Exception ex) throws Exception {
		for(IWebServiceInterceptor interceptor:interceptors){
			interceptor.afterCompletion(messageContext, endpoint,ex);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		interceptors=applicationContext.getBeansOfType(IWebServiceInterceptor.class).values();
		if(interceptors.size()==0 && applicationContext.getParent()!=null){
			interceptors=applicationContext.getParent().getBeansOfType(IWebServiceInterceptor.class).values();
		}
	}
}
