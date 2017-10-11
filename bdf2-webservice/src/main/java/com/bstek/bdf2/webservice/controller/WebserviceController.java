package com.bstek.bdf2.webservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.ws.transport.http.WebServiceMessageReceiverHandlerAdapter;

/**
 * @author Jacky.gao
 * @since 2013-3-5
 */
public class WebserviceController extends AbstractController {
	private WebServiceMessageReceiverHandlerAdapter webServiceMessageReceiverHandlerAdapter;
	private Object defaultHandler;
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return webServiceMessageReceiverHandlerAdapter.handle(request, response, defaultHandler);
	}
	public void setWebServiceMessageReceiverHandlerAdapter(
			WebServiceMessageReceiverHandlerAdapter webServiceMessageReceiverHandlerAdapter) {
		this.webServiceMessageReceiverHandlerAdapter = webServiceMessageReceiverHandlerAdapter;
	}
	public void setDefaultHandler(Object defaultHandler) {
		this.defaultHandler = defaultHandler;
	}
}
