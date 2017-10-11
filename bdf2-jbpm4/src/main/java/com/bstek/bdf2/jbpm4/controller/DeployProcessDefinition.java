package com.bstek.bdf2.jbpm4.controller;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.jbpm4.service.IBpmService;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-3-21
 */
public class DeployProcessDefinition extends AbstractResolver implements InitializingBean{
	private IBpmService bpmService;
	private boolean disableAnonymousDeployProcess;
	public IBpmService getBpmService() {
		return bpmService;
	}

	
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try{
			if(this.disableAnonymousDeployProcess && ContextHolder.getLoginUser()==null){
				throw new AccessDeniedException("Please login first!");
			}
			if(request instanceof DefaultMultipartHttpServletRequest){
				DefaultMultipartHttpServletRequest req=(DefaultMultipartHttpServletRequest)request;
				Collection<MultipartFile> multipartFiles=req.getFileMap().values();
				if(multipartFiles.size()>0){
					CommonsMultipartFile fileItem=(CommonsMultipartFile)multipartFiles.iterator().next();
					bpmService.deployProcess(fileItem.getInputStream());					
				}
			}else{
				ServletFileUpload servletUpload = new ServletFileUpload(new DiskFileItemFactory());
				List<?> list=servletUpload.parseRequest(request);
				if (list.size()>0) {
					FileItem fileItem = (FileItem)list.get(0);
					bpmService.deployProcess(fileItem.getInputStream());		
				}
			}
			PrintWriter pw=response.getWriter();
			pw.write("ok");
			pw.flush();
			pw.close();
		}catch(Exception ex){
			ex.printStackTrace();
			PrintWriter pw=response.getWriter();
			pw.write("error");
			pw.flush();
			pw.close();
		}
		return null;
	}

	public void afterPropertiesSet() throws Exception {
		Map<String,IBpmService> map=this.getApplicationContext().getBeansOfType(IBpmService.class);
		if(map.size()==0){
			bpmService=this.getApplicationContext().getParent().getBeansOfType(IBpmService.class).values().iterator().next();
		}else{
			bpmService=map.values().iterator().next();
		}
	}

	public boolean isDisableAnonymousDeployProcess() {
		return disableAnonymousDeployProcess;
	}

	public void setDisableAnonymousDeployProcess(
			boolean disableAnonymousDeployProcess) {
		this.disableAnonymousDeployProcess = disableAnonymousDeployProcess;
	}
}
