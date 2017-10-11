package com.bstek.bdf2.webservice.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.util.WebUtils;
import org.springframework.ws.transport.http.WsdlDefinitionHandlerAdapter;
import org.springframework.ws.wsdl.WsdlDefinition;

/**
 * @author Jacky.gao
 * @since 2013-3-5
 */
public class WsdlController extends AbstractController implements InitializingBean{
	private WsdlDefinitionHandlerAdapter wsdlDefinitionHandlerAdapter;
	private Map<String, WsdlDefinition> wsdlDefinitions;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileName = WebUtils.extractFilenameFromUrlPath(request.getRequestURI());
		WsdlDefinition target=null;
		if(wsdlDefinitions.containsKey(fileName)){
			target = wsdlDefinitions.get(fileName);
		}
		if(target==null){
			PrintWriter out=response.getWriter();
			try{
				out.write("WSDL file ["+fileName+"] is not exist!");
				return null;
			}finally{
				out.flush();
				out.close();
			}			
		}else{
			return wsdlDefinitionHandlerAdapter.handle(request, response,target);			
		}
	}

	public void setWsdlDefinitionHandlerAdapter(
			WsdlDefinitionHandlerAdapter wsdlDefinitionHandlerAdapter) {
		this.wsdlDefinitionHandlerAdapter = wsdlDefinitionHandlerAdapter;
	}
	public void afterPropertiesSet() throws Exception {
		wsdlDefinitions=this.getApplicationContext().getBeansOfType(WsdlDefinition.class);
		if(this.getApplicationContext().getParent()!=null){
			wsdlDefinitions.putAll(this.getApplicationContext().getParent().getBeansOfType(WsdlDefinition.class));			
		}
	}
}
