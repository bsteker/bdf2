package com.bstek.bdf2.core.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Jacky.gao
 * @since 2013-5-8
 */
public class ListController implements IController,ApplicationContextAware{
	private ApplicationContext applicationContext;
	public String getUrl() {
		return "/list";
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		StringBuffer sb=new StringBuffer();
		sb.append("<table style=\"width:100%;border:solid 1px\">");
		sb.append("<tr style=\"background:#DDDDDD\">");
		sb.append("<td>");
		sb.append("<strong>URL</strong>");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("<strong>AnonymousAccess</strong>");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("<strong>Disabled</strong>");
		sb.append("</td>");
		sb.append("</tr>");
		int i=1;
		for(IController c:applicationContext.getBeansOfType(IController.class).values()){
			int m=i%2;
			String bg="#FDFDFD";
			if(m==0){
				bg="#DDDDDD";
			}
			sb.append("<tr style=\"background:"+bg+"\">");
			sb.append("<td>");
			sb.append(c.getUrl());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(c.anonymousAccess());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(c.isDisabled());
			sb.append("</td>");
			sb.append("<tr>");
			i++;
		}
		sb.append("</table>");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf8");
		PrintWriter pw=response.getWriter();
		pw.write(sb.toString());
		pw.flush();
		pw.close();
	}

	public boolean anonymousAccess() {
		return false;
	}

	public boolean isDisabled() {
		return false;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	
}
