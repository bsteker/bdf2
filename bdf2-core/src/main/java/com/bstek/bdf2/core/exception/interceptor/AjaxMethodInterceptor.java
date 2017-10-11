package com.bstek.bdf2.core.exception.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.bdf2.core.exception.IAjaxExceptionHandler;
import com.bstek.dorado.common.proxy.PatternMethodInterceptor;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.view.resolver.ClientRunnableException;

/**
 * @author Jacky.gao
 * @since 2013年11月25日
 */
public class AjaxMethodInterceptor extends PatternMethodInterceptor implements ApplicationContextAware{
	protected final Log logger = LogFactory.getLog(getClass());
	
	private Collection<IAjaxExceptionHandler> handlers;
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
            return invocation.proceed();
        }catch (Exception exception) {
        	Throwable ex=getException(exception);
            for(IAjaxExceptionHandler handler:handlers){
            	if(handler.support(ex)){
            		handler.handle(ex, invocation);
            		break;
            	}
            }
            if(ex instanceof ClientRunnableException){
            	throw ex;
            }
            String errorInfo=Configure.getString("bdf2.ajaxInvodeExceptionMessage");
            String error=ex.getMessage();
            if(error==null){
            	error="";
            }
			if (error.indexOf("$") > -1){
		        error = java.util.regex.Matcher.quoteReplacement(error);  
	        }
            errorInfo = errorInfo.replaceAll("#errorMessage", error);            	
            logger.info(errorInfo, ex);
            throw new Exception(errorInfo);
        }
	}
		
	private Throwable getException(Throwable ex){
		ex.printStackTrace();
		Throwable throwable = ex;
		if(ex.getCause()==null){
			throwable = ex;
		}else{
			throwable = ex.getCause();
		}
		if (throwable instanceof InvocationTargetException){
			throwable = ((InvocationTargetException)throwable).getTargetException();
		}
		return throwable;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		handlers=applicationContext.getBeansOfType(IAjaxExceptionHandler.class).values();
	}
}
