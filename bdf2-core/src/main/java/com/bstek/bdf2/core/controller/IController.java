package com.bstek.bdf2.core.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现该接口，并将类注册到Spring就可以通过指定的URL访问执行其中的execute方法体
 * @author Jacky.gao
 * @since 2013-2-21
 */
public interface IController {
	String getUrl();
	void execute(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException;
	boolean anonymousAccess();
	boolean isDisabled();
}
