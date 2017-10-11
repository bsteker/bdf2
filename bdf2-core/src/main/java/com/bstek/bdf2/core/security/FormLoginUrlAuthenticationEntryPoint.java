package com.bstek.bdf2.core.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.bstek.bdf2.core.exception.NoneLoginException;
import com.bstek.bdf2.core.security.session.SessionStateConstants;

/**
 * @author Jacky.gao
 * @since 2013年12月19日
 */
public class FormLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	private String sessionKickAwayUrl;
	private String expiredUrl;
	public FormLoginUrlAuthenticationEntryPoint(String loginFormUrl,String sessionKickAwayUrl,String expiredUrl) {
		super(loginFormUrl);
		this.sessionKickAwayUrl=sessionKickAwayUrl;
		this.expiredUrl=expiredUrl;
	}
	@Override
    protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) {
    	if(authException!=null){
    		Throwable ex=(Throwable)getCause(authException);
    		if(ex instanceof NoneLoginException){
    			NoneLoginException exception=(NoneLoginException)ex;
    			if(exception.isSessionKickAway()){
    				return sessionKickAwayUrl;
    			}
    		}
    	}
    	HttpSession session=request.getSession();
    	String state=(String)session.getAttribute(SessionStateConstants.SESSION_STATE);
    	if(state!=null){
    		//session.removeAttribute(SessionStateConstants.SESSION_STATE);
    		if(state.equals(SessionStateConstants.KICKAWAY)){
    			return sessionKickAwayUrl;
    		}else if(state.equals(SessionStateConstants.EXPIRED)){
    			return expiredUrl;
    		}
    	}
    	return super.buildRedirectUrlToLoginPage(request, response, authException);
    }
    
    private Throwable getCause(Throwable ex){
    	if(ex.getCause()!=null){
    		return getCause(ex.getCause());
    	}else{
    		return ex;
    	}
    }
}
