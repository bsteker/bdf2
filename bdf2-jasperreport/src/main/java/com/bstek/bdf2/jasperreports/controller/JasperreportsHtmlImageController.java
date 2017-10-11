package com.bstek.bdf2.jasperreports.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.web.resolver.AbstractResolver;

public class JasperreportsHtmlImageController extends AbstractResolver {
	private ImageServlet servlet=new ImageServlet();
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		servlet.service(request, response);
		request.getSession().removeAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
		return null;
	}
}
