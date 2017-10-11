package com.bstek.bdf2.core.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.bdf2.core.business.IUser;

/**
 * @author Jacky.gao
 * @since 2013年7月5日
 * 获取通过其它方式已经登录的用户信息，比如通过SSO等
 */
public interface IRetrivePreAuthenticatedUser {
	/**
	 * 根据给出的request与response对象，取出当前已通过其它途径预认证的IUser对象，如果返回null表示预认证未通过，系统将不会处理
	 * @param request
	 * @param response
	 * @return 返回已被预认证通过的IUser对象
	 * @throws ServletException
	 */
	IUser retrive(HttpServletRequest request,HttpServletResponse response) throws ServletException;
}
