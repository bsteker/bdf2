package com.bstek.bdf2.core.context;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.security.web.WebAttributes;

import com.bstek.dorado.core.el.ContextVarsInitializer;

public class ContextVariablesInitializer implements ContextVarsInitializer,
		BeanFactoryAware {
	private Logger logger = Logger.getLogger(ContextVariablesInitializer.class);

	public void initializeContext(Map<String, Object> contextVarsMap)
			throws Exception {
		contextVarsMap.put("loginUser", ContextHolder.getLoginUser());
		contextVarsMap.put("loginUsername",ContextHolder.getLoginUser()==null?null:ContextHolder.getLoginUser().getUsername());
		contextVarsMap.put("authenticationExceptionMessage",this.getAuthenticationExceptionMessage());
		
	}
	private String getAuthenticationExceptionMessage(){
		Exception exp=(Exception)ContextHolder.getHttpSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if(exp==null){
			exp=(Exception)ContextHolder.getRequest().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
		if(exp!=null){
			if (logger.isDebugEnabled()){
				logger.trace(exp.getMessage(), exp.getCause());
			}
			return exp.getMessage();
		}
		return null;
		
	}
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		
	}
}