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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.security.IRetrivePreAuthenticatedUser;

/**
 * @author Jacky.gao
 * @since 2013年7月5日
 */
public class PreAuthenticatedProcessingFilter extends GenericFilterBean implements ApplicationContextAware{
	private Collection<IRetrivePreAuthenticatedUser> preAuthentications;
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		if(ContextHolder.getLoginUser()==null && preAuthentications.size()>0){
			HttpServletRequest req=(HttpServletRequest)request;
			HttpServletResponse res=(HttpServletResponse)response;
			IUser user=preAuthentications.iterator().next().retrive(req,res);
			if(user!=null){
				req.getSession().setAttribute(ContextHolder.LOGIN_USER_SESSION_KEY, user);
				UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);				
			}
		}
		chain.doFilter(request, response);
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		preAuthentications=applicationContext.getBeansOfType(IRetrivePreAuthenticatedUser.class).values();
	}
}
