package com.bstek.bdf2.webservice.interceptor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

/**
 * @author Jacky.gao
 * @since 2013-3-6
 */
public class EndpointSecurityInterceptor implements FactoryBean<EndpointInterceptor> {
	private Wss4jSecurityInterceptor wss4jSecurityInterceptor;
	private NoneSecurityInterceptor noneSecurityInterceptor;
	private boolean useSecurity;
	public EndpointInterceptor getObject() throws Exception {
		if(useSecurity){
			return wss4jSecurityInterceptor;
		}else{
			return noneSecurityInterceptor;
		}
	}

	public Class<?> getObjectType() {
		return EndpointInterceptor.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setWss4jSecurityInterceptor(
			Wss4jSecurityInterceptor wss4jSecurityInterceptor) {
		this.wss4jSecurityInterceptor = wss4jSecurityInterceptor;
	}

	public void setNoneSecurityInterceptor(
			NoneSecurityInterceptor noneSecurityInterceptor) {
		this.noneSecurityInterceptor = noneSecurityInterceptor;
	}

	public void setUseSecurity(boolean useSecurity) {
		this.useSecurity = useSecurity;
	}
}
