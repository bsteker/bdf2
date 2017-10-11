package com.bstek.bdf2.core.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.bdf2.core.context.ContextHolder;

/**
 * @author Jacky.gao
 * @since 2013-5-10
 */
public class SecurityInterceptorAdapter implements ISecurityInterceptor {
	public void beforeLogin(HttpRequestResponseHolder holder) {

	}

	public void loginSuccess(HttpRequestResponseHolder holder) {

	}

	public void loginFailure(HttpRequestResponseHolder holder) {

	}

	public void beforeAuthorization(HttpRequestResponseHolder holder) {

	}

	public void authorizationSuccess(HttpRequestResponseHolder holder) {

	}

	public void authorizationFailure(HttpRequestResponseHolder holder) {

	}
	
	/**
	 * 在用户通过其它途径取取到登录用户并认证成功之后，这里允许其提供一个IUser接口实现及HttpRequestResponseHolder对象后,<br>
	 * 这个方法可以将这个IUser对应的用户信息放到系统上下文当中，标明该用户登录成功
	 * @param user IUser接口实现类对象
	 * @param holder 一个包含Request及Response的HttpRequestResponseHolder对象
	 */
	protected void registerLoginInfo(IUser user,HttpRequestResponseHolder holder){
		holder.getRequest().getSession().setAttribute(ContextHolder.LOGIN_USER_SESSION_KEY, user);
		UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
