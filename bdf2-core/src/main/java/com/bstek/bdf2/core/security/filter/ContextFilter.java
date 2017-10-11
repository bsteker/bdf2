package com.bstek.bdf2.core.security.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.exception.IExceptionHandler;
import com.bstek.bdf2.core.security.ISecurityInterceptor;
import com.bstek.bdf2.core.security.InterceptorType;

/**
 * @since 2013-1-22
 * @author Jacky.gao
 */
public class ContextFilter extends GenericFilterBean implements ApplicationContextAware{
	private Collection<IExceptionHandler> exceptionHandlers;
	private Collection<ISecurityInterceptor> securityInterceptors;
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpRequestResponseHolder holder=new HttpRequestResponseHolder((HttpServletRequest)request,(HttpServletResponse)response);
		ContextHolder.setHttpRequestResponseHolder((HttpServletRequest)request,(HttpServletResponse)response);
		try{
			this.doInterceptor(InterceptorType.before, holder);
			chain.doFilter(request, response);
			this.doInterceptor(InterceptorType.success, holder);
		}catch(Exception exception){
			this.doInterceptor(InterceptorType.failure, holder);
			Throwable throwable=this.getThrowableCause(exception);
			boolean support=false;
			for(IExceptionHandler handler:exceptionHandlers){
				if(handler.support(throwable)){
					support=true;
					handler.handle(holder, throwable);		
					break;
				}
			}
			
			if(!support){
				if(throwable instanceof IOException){
					throw (IOException)throwable;
				}else{
					throw new ServletException(throwable);
				}
			}
		}finally{
			ContextHolder.clean();
		}
	}
	
	private void doInterceptor(InterceptorType type,HttpRequestResponseHolder holder){
		for(ISecurityInterceptor intercepor:securityInterceptors){
			if(type.equals(InterceptorType.before)){
				intercepor.beforeAuthorization(holder);
			}else if(type.equals(InterceptorType.success)){
				intercepor.authorizationSuccess(holder);
			}else if(type.equals(InterceptorType.failure)){
				intercepor.authorizationFailure(holder);
			}
		}
	}
	private Throwable getThrowableCause(Throwable ex){
		if(ex.getCause()==null){
			return ex;
		}else{
			return getThrowableCause(ex.getCause());
		}
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		securityInterceptors=applicationContext.getBeansOfType(ISecurityInterceptor.class).values();
		exceptionHandlers=applicationContext.getBeansOfType(IExceptionHandler.class).values();
	}
}
