package com.bstek.bdf2.webservice.client;

/**
 * @author Jacky.gao
 * @since 2013-3-7
 */
public class WebServiceClient {
	private WebServiceClientTools tools;
	public WebServiceClient(String serviceUri){
		tools=new WebServiceClientTools();
		tools.setDefaultUri(serviceUri);
	}
	public WebServiceClient setMarshallerClasses(Class<?>[] classes){
		tools.setMarshallerUnmarshallerClass(classes);
		return this;
	}
	public WebServiceClient setUsernameToken(String username,String password,boolean encryptPassword){
		tools.setUsernameToken(username, password, encryptPassword);
		return this;
	}
	public WebServiceClient setHttpAuthenticationCredentials(String username,String password){
		tools.setHttpAuthenticationCredentials(username, password);
		return this;
	}
	public Object sendAndReceive(Object obj){
		return tools.marshalSendAndReceive(obj);
	}
}
