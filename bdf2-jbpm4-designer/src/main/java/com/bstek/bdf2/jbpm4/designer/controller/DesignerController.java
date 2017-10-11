package com.bstek.bdf2.jbpm4.designer.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-6-21
 */
public class DesignerController extends AbstractResolver {

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Properties properties=new Properties();
        properties.setProperty("resource.loader", "file");
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        String contextPath=request.getContextPath();
        if(!contextPath.endsWith("/")){
        	contextPath+="/";
        }
        VelocityEngine velocityEngine=new VelocityEngine(properties);
        VelocityContext context=new VelocityContext();
        StringBuffer sb=new StringBuffer();
        sb.append("\r");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+contextPath+"dorado/res/dorado/resources/jquery.contextMenu.css\" />");
        sb.append("\r");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+contextPath+"dorado/res/dorado/resources/jquery-ui-1.8.19.custom.css\" />");
        sb.append("\r");
        sb.append("<script type=\"text/javascript\" src=\""+contextPath+"dorado/res/dorado/scripts/jbpm4-designer-all-in-one.js\"></script>");
        sb.append("\r");
        
        String serverUrl=this.buildServerUrl(request.getScheme(),request.getServerName(),request.getServerPort());
        if(contextPath.endsWith("/")){
        	serverUrl+=contextPath.substring(0,contextPath.length()-1);
        }
        context.put("cssandscript", sb.toString());
        context.put("baseIconsDir", contextPath+"dorado/res/dorado/resources");
        context.put("serverUrl", serverUrl);
        StringWriter writer=new StringWriter();
        velocityEngine.mergeTemplate("dorado/resources/jbpm4-designer.html","utf-8", context, writer);
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out=response.getWriter();
        out.write(writer.toString());
        out.flush();
        out.close();
		return null;
	}
	
	private String buildServerUrl(String scheme, String serverName, int serverPort) {
        scheme = scheme.toLowerCase();
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        // Only add port if not default
        if ("http".equals(scheme)) {
            if (serverPort != 80) {
                url.append(":").append(serverPort);
            }
        } else if ("https".equals(scheme)) {
            if (serverPort != 443) {
                url.append(":").append(serverPort);
            }
        }
        return url.toString();
    }
}
