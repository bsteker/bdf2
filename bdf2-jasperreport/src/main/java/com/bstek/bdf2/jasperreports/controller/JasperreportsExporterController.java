package com.bstek.bdf2.jasperreports.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-5-11
 */
public class JasperreportsExporterController extends AbstractResolver implements InitializingBean{
	private Collection<IExporter> exporters;
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest req,HttpServletResponse res) throws Exception {
		String exportFileType=req.getParameter(IExporter.EXPORT_FILE_TYPE_PARAMETER_KEY);
		if(StringUtils.isEmpty(exportFileType)){
			throw new RuntimeException("Export file type can not be null!");
		}
		IExporter target=null;
		for(IExporter exporter:exporters){
			if(exporter.support(exportFileType)){
				target=exporter;
				break;
			}
		}
		if(target==null){
			throw new RuntimeException("Unsupport export file type ["+exportFileType+"]!");
		}
		target.export(req, res);
		return null;
	}	
	public void afterPropertiesSet() throws Exception {
		exporters=new ArrayList<IExporter>();
		exporters.addAll(this.getApplicationContext().getBeansOfType(IExporter.class).values());
		ApplicationContext ac=this.getApplicationContext().getParent();
		if(ac!=null && ac.getBeansOfType(IExporter.class).values().size()>0){
			exporters.addAll(ac.getBeansOfType(IExporter.class).values());
		}
	}
}
