package com.bstek.bdf2.jasperreports.controller.impl;

import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

/**
 * @author Jacky.gao
 * @since 2013-5-12
 */
public class HtmlExporter extends AbstractExporter {
	private boolean disabled;
	public boolean isDisabled() {
		return disabled;
	}	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean support(String targetFile) {
		return targetFile.equals("html");
	}

	public void export(HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setContentType("text/html;charset=UTF-8");
		JRHtmlExporter exporter=new JRHtmlExporter(DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint=this.getJasperPrint(req);
		req.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
		OutputStream ouputStream = res.getOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		String path=req.getContextPath();
		if(path.endsWith("/")){
			path+="dorado/bdf2/jasperreports/html.image";
		}else{
			path+="/dorado/bdf2/jasperreports/html.image";			
		}
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,path+"?image=");
		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new ServletException(e);
		} finally {
			if (ouputStream != null) {
				ouputStream.flush();
				ouputStream.close();
			}
		}
	}

}
