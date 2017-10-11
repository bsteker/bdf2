package com.bstek.bdf2.core.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;

import com.bstek.bdf2.core.business.AbstractUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.service.IUserService;

/**
 * @since 2013-1-23
 * @author Jacky.gao
 */
public class RememberMeAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {
	private IUserService userService;
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)throws IOException, ServletException {
		Object user=authentication.getPrincipal();
		Assert.notNull(user,"通过Remember Me方式登录成功后未获取到用户信息");
		if(user instanceof AbstractUser){
			ContextHolder.getHttpSession().setAttribute(ContextHolder.LOGIN_USER_SESSION_KEY, user);
		}else if(user instanceof AbstractUser){
			ContextHolder.getHttpSession().setAttribute(ContextHolder.LOGIN_USER_SESSION_KEY, userService.loadUserByUsername((String)user));			
		}else{
			throw new ServletException("Unsupport current principal["+user+"]");
		}
	}
	
	public IUserService getUserService() {
		return userService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
