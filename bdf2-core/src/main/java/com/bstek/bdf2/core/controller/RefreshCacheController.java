package com.bstek.bdf2.core.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import com.bstek.dorado.web.DoradoContext;

public class RefreshCacheController implements IController {

	private static final Logger log = LoggerFactory.getLogger(RefreshCacheController.class);
	public static final String URL = "/system.refreshCache";

	public String getUrl() {
		return URL;
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml;charset=utf8");
		PrintWriter pw = response.getWriter();
		String beanId = (String) request.getParameter("beanId");
		String methodName = (String) request.getParameter("methodName");
		String parameter = (String) request.getParameter("parameter");
		if (StringUtils.isNotEmpty(beanId) && StringUtils.isNotEmpty(methodName)) {
			try {
				Object object = DoradoContext.getAttachedWebApplicationContext().getBean(beanId);
				Object[] arguments = null;
				if (StringUtils.isNotEmpty(parameter)) {
					arguments = (Object[]) SerializationUtils.deserialize(new Base64(true).decode(parameter));
				}
				MethodUtils.invokeMethod(object, methodName, arguments);
				log.info("invoke refreshCache method [" + beanId + "#" + methodName + "] success");
				pw.print("ok");
			} catch (Exception e) {
				log.error("invoke refreshCache method [" + beanId + "#" + methodName + "] error," + e.getMessage());
				pw.print("error");
			}
		}
	}

	public boolean anonymousAccess() {
		return true;
	}

	public boolean isDisabled() {
		return false;
	}

}
