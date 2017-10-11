package com.bstek.bdf2.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;


/**
 * @since 2013-1-23
 * @author Jacky.gao
 */
public class MultiAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private String authenticationType="form";
	private LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint;
	private CasAuthenticationEntryPoint casAuthenticationEntryPoint;
	private BasicAuthenticationEntryPoint basicAuthenticationEntryPoint;
	public void commence(HttpServletRequest request,HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if(authenticationType.equals("form")){
			loginUrlAuthenticationEntryPoint.commence(request, response, authException);
		}else if(authenticationType.equals("cas")){
			casAuthenticationEntryPoint.commence(request, response, authException);
		}else if(authenticationType.equals("basic")){
			basicAuthenticationEntryPoint.commence(request, response, authException);
		}else{
			throw new IllegalArgumentException("Does not support current authentication type ["+authenticationType+"]");
		}
		
	}

	public void setLoginUrlAuthenticationEntryPoint(
			LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint) {
		this.loginUrlAuthenticationEntryPoint = loginUrlAuthenticationEntryPoint;
	}

	public void setCasAuthenticationEntryPoint(
			CasAuthenticationEntryPoint casAuthenticationEntryPoint) {
		this.casAuthenticationEntryPoint = casAuthenticationEntryPoint;
	}

	public void setBasicAuthenticationEntryPoint(
			BasicAuthenticationEntryPoint basicAuthenticationEntryPoint) {
		this.basicAuthenticationEntryPoint = basicAuthenticationEntryPoint;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
}