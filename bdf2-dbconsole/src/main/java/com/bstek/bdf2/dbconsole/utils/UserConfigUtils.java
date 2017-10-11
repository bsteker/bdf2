package com.bstek.bdf2.dbconsole.utils;

import com.bstek.bdf2.core.context.ContextHolder;

public class UserConfigUtils {
	private static final String LOGIN_USER = "dbconsole_user";

	/**
	 * 获取登录用户
	 * 
	 * @return 返回用户名
	 */
	public static String getUserName() {
		if (ContextHolder.getLoginUser() == null) {
			return LOGIN_USER;
		} else {
			return ContextHolder.getLoginUserName();
		}
	}
}
