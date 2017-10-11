package com.bstek.bdf2.jbpm4.designer.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.uploader.service.ILobStoreService;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-6-25
 */
public class LoadProcessController extends AbstractResolver {
	private ILobStoreService lobStoreService;
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lobId=request.getParameter("lobId");
		String xml=lobStoreService.getString(lobId);
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter writer=response.getWriter();
        xml=(xml==null)?"":xml;
        writer.write(xml);
        writer.flush();
        writer.close();
		return null;
	}
	public ILobStoreService getLobStoreService() {
		return lobStoreService;
	}
	public void setLobStoreService(ILobStoreService lobStoreService) {
		this.lobStoreService = lobStoreService;
	}
}
