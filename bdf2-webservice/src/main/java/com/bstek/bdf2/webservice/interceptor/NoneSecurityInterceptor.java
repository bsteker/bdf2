package com.bstek.bdf2.webservice.interceptor;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

import com.bstek.bdf2.webservice.jaxb.IWebservice;

public class NoneSecurityInterceptor implements EndpointInterceptor {
	private Wss4jSecurityInterceptor wss4jSecurityInterceptor;
	public boolean handleRequest(MessageContext messageContext, Object endpoint)
			throws Exception {
		if(useSecurity(endpoint)){
			return wss4jSecurityInterceptor.handleRequest(messageContext, endpoint);
		}
		return true;
	}

	public boolean handleResponse(MessageContext messageContext, Object endpoint)
			throws Exception {
		if(useSecurity(endpoint)){
			return wss4jSecurityInterceptor.handleResponse(messageContext, endpoint);
		}
		return true;
	}

	public boolean handleFault(MessageContext messageContext, Object endpoint)
			throws Exception {
		if(useSecurity(endpoint)){
			return wss4jSecurityInterceptor.handleFault(messageContext, endpoint);
		}
		return true;
	}

	public void afterCompletion(MessageContext messageContext, Object endpoint,
			Exception ex) throws Exception {
		if(useSecurity(endpoint)){
			wss4jSecurityInterceptor.afterCompletion(messageContext, endpoint,ex);
		}
	}

	private boolean useSecurity(Object endpoint){
		boolean result=false;
		if(endpoint!=null && endpoint instanceof MethodEndpoint){
			MethodEndpoint ep=(MethodEndpoint)endpoint;
			Object bean=ep.getBean();
			if(bean instanceof IWebservice){
				IWebservice b=(IWebservice)bean;
				result=b.useSecurity();
			}
		}
		return result;
	}

	public void setWss4jSecurityInterceptor(
			Wss4jSecurityInterceptor wss4jSecurityInterceptor) {
		this.wss4jSecurityInterceptor = wss4jSecurityInterceptor;
	}
}
