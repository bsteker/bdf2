package com.bstek.bdf2.jasperreports.controller.impl;

import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

/**
 * @author Jacky.gao
 * @since 2013-5-12
 */
public class DocxExporter extends AbstractExporter {
	private boolean disabled;
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean support(String targetFile) {
		return targetFile.equals("docx");
	}

	public void export(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String fileName=this.getExportFileName(req);
		fileName+=".docx";
		res.setContentType("application/octet-stream");
		res.setHeader("Connection", "close");
		res.setHeader("Content-Disposition", "attachment;filename=\"" + new String(fileName.getBytes("utf-8"),"ISO-8859-1") + "\"");
		JRDocxExporter exporter = new JRDocxExporter(DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint=this.getJasperPrint(req);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
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
}
