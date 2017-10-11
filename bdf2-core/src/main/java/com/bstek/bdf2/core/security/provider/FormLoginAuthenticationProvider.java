package com.bstek.bdf2.core.security.provider;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpRequestResponseHolder;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.security.ISecurityInterceptor;
import com.bstek.bdf2.core.security.InterceptorType;
import com.bstek.bdf2.core.service.IFrameworkService;

/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
public class FormLoginAuthenticationProvider extends DaoAuthenticationProvider implements ApplicationContextAware{
	private Collection<ISecurityInterceptor> securityInterceptors;
	private IFrameworkService frameworkService;
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		IUser user=(IUser)userDetails;
		HttpRequestResponseHolder holder=new HttpRequestResponseHolder(ContextHolder.getRequest(),ContextHolder.getResponse());
		this.doInterceptor(InterceptorType.before, holder);
		try{
			frameworkService.authenticate(user, authentication);
		}catch(Exception ex){
			this.doInterceptor(InterceptorType.failure, holder);		
			throw new AuthenticationServiceException(ex.getMessage());
		}
    	ContextHolder.getHttpSession().setAttribute(ContextHolder.LOGIN_USER_SESSION_KEY,user);
		this.doInterceptor(InterceptorType.success, holder);
	}
	public IFrameworkService getFrameworkService() {
		return frameworkService;
	}
	public void setFrameworkService(IFrameworkService frameworkService) {
		this.frameworkService = frameworkService;
	}
	private void doInterceptor(InterceptorType type,HttpRequestResponseHolder holder){
		for(ISecurityInterceptor intercepor:securityInterceptors){
			if(type.equals(InterceptorType.before)){
				intercepor.beforeLogin(holder);
			}else if(type.equals(InterceptorType.success)){
				intercepor.loginSuccess(holder);
			}else if(type.equals(InterceptorType.failure)){
				intercepor.loginFailure(holder);
			}
		}
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		securityInterceptors=applicationContext.getBeansOfType(ISecurityInterceptor.class).values();
	}
}
