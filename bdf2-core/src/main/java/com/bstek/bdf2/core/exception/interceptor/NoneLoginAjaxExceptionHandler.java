package com.bstek.bdf2.core.exception.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInvocation;

import com.bstek.bdf2.core.exception.IAjaxExceptionHandler;
import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Jacky.gao
 * @since 2013年11月29日
 */
public class NoneLoginAjaxExceptionHandler implements IAjaxExceptionHandler {

	public void handle(Throwable exception, MethodInvocation invocation) {
		HttpServletRequest request=DoradoContext.getCurrent().getRequest();
		String contextPath=request.getContextPath();
		StringBuffer sb=new StringBuffer();
		NoneLoginException ex=(NoneLoginException)exception;
		if(ex.isSessionKickAway()){
			sb.append("dorado.MessageBox.alert(\""+Configure.getString("bdf2.ajaxSessionKickAwayMessage")+"\",function(){\n");
			sb.append("window.open(\""+contextPath+Configure.getString("bdf2.formLoginUrl")+"\",\"_top\");\n");
			sb.append("})");			
		}else{
			sb.append("dorado.MessageBox.alert(\""+Configure.getString("bdf2.ajaxSessionExpireMessage")+"\",function(){\n");
			sb.append("window.open(\""+contextPath+Configure.getString("bdf2.formLoginUrl")+"\",\"_top\");\n");
			sb.append("})");			
		}
        throw new ClientRunnableException(sb.toString());
	}

	public boolean support(Throwable exception) {
		return (exception instanceof NoneLoginException);
	}

}
