package com.bstek.bdf2.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.RedirectStrategy;

/**
 * @author Jacky.gao
 * @since 2013年12月19日
 */
public class ForwardRedirectStrategy implements RedirectStrategy {

	public void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
			throw new IOException();
		}
	}

}
