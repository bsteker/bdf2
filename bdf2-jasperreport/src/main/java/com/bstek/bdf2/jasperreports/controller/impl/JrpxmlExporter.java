package com.bstek.bdf2.jasperreports.controller.impl;

import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXmlExporter;

/**
 * @author Jacky.gao
 * @since 2013-5-11
 */
public class JrpxmlExporter extends AbstractExporter {
	private boolean disabled;

	public boolean support(String targetFile) {
		return targetFile.equals("jrpxml");
	}

	public void export(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		res.setContentType("text/xml");
		res.setHeader("Content-Disposition", "inline; filename=\"file.jrpxml\"");
		JRXmlExporter exporter = new JRXmlExporter(DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint=this.getJasperPrint(req);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
		//exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, Integer.valueOf(1));
		OutputStream ouputStream = res.getOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
