package com.bstek.bdf2.core.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.springframework.web.filter.GenericFilterBean;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.controller.IController;

/**
 * @author Jacky.gao
 * @since 2013-2-21
 */
public class ControllerFilter extends GenericFilterBean implements ApplicationContextAware{
	private String controllerSuffix=".action";
	private Collection<IController> controllers;
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		String url=this.getUrl(req);
		if(url.indexOf("com.bstek.bdf2.")!=-1){
			writeInfo(res,"Url ["+url+"] can not used");
			return;
		}
		if(url.endsWith(controllerSuffix)){
			IController target=null;
			url=url.substring(0,url.length()-controllerSuffix.length());
			for(IController controller:controllers){
				if(controller.getUrl().equals(url)){
					target=controller;
					break;
				}
			}
			if(target==null){
				writeInfo(res, "action ["+url+"] was not found");
				return;
			}
			if(target.isDisabled()){
				writeInfo(res, "action ["+url+"] was disabled");
				return;
			}
			if(!target.anonymousAccess()){
				if(ContextHolder.getLoginUser()!=null){
					target.execute(req, (HttpServletResponse)response);					
				}else{
					writeInfo(res, "action ["+url+"] need login first");					
				}
			}else{
				target.execute(req, (HttpServletResponse)response);
			}
		}else{
			chain.doFilter(request, response);
		}
	}
	private void writeInfo(HttpServletResponse res, String info)
			throws IOException {
		PrintWriter pw=res.getWriter();
		try{
			pw.write("<font color='red'>"+info+"</font>");
		}finally{
			pw.flush();
			pw.close();
		}
	}
	private String getUrl(HttpServletRequest request){
		String contextPath=request.getContextPath();
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			// strip everything from the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}
		int queryParamIndex = uri.indexOf('?');
		if (queryParamIndex > 0) {
			// strip everything from the first question mark
			uri = uri.substring(0, queryParamIndex);
		}
		if(contextPath.length()>1){
			uri=uri.substring(contextPath.length(),uri.length());
		}
		return uri;
	}
	public String getControllerSuffix() {
		return controllerSuffix;
	}
	public void setControllerSuffix(String controllerSuffix) {
		this.controllerSuffix = controllerSuffix;
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		controllers=applicationContext.getBeansOfType(IController.class).values();
	}
}
