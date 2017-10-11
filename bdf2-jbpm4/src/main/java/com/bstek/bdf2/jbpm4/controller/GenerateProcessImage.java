/**
 * 
 */
package com.bstek.bdf2.jbpm4.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.api.ProcessDefinition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.web.resolver.AbstractResolver;
/**
 * @author Jacky.gao
 * @since 2013-3-22
 */
public class GenerateProcessImage extends AbstractResolver implements InitializingBean {
	public static final String URL="dorado/bdf2/jbpm4/generate.processimage";
	private IBpmService bpmService;

	public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException,
			ServletException {
		String processDefinitionId = req.getParameter("processDefinitionId");
		if (!StringUtils.hasText(processDefinitionId)) {
			throw new RuntimeException("processDefinitionId can not be null!");
		}
		ProcessDefinition pd = bpmService.findProcessDefinitionById(processDefinitionId);
		if(pd==null){
			throw new IllegalArgumentException("Process definition ["+processDefinitionId+"] is not exist!");
		}
		InputStream in = bpmService.getRepositoryService().getResourceAsStream(
				pd.getDeploymentId(), pd.getImageResourceName());
		resp.setContentType("image/png");
		OutputStream out = resp.getOutputStream();
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		out.flush();
		out.close();
	}


	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		execute(request, response);
		return null;
	}


	public IBpmService getBpmService() {
		return bpmService;
	}
	public void afterPropertiesSet() throws Exception {
		Map<String,IBpmService> map=this.getApplicationContext().getBeansOfType(IBpmService.class);
		if(map.size()==0){
			bpmService=this.getApplicationContext().getParent().getBeansOfType(IBpmService.class).values().iterator().next();
		}else{
			bpmService=map.values().iterator().next();
		}
	}
}
