package com.bstek.bdf2.importexcel.upload;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.importexcel.context.ImportContext;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.parse.IExcelParser;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * 
 * @author matt.yao@bstek.com
 * @since 2.0
 * 
 */
public class UploadResolver extends AbstractResolver {
	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			doUpload(request, response);
		} catch (Exception e) {
			response.getWriter().print(e.getMessage());
		}
		return null;
	}

	private void doUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MultipartHttpServletRequest multiPartReq = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		if (!(request instanceof MultipartHttpServletRequest) && multipartResolver.isMultipart(request)) {
			multiPartReq = multipartResolver.resolveMultipart(request);
		} else if (request instanceof MultipartHttpServletRequest) {
			multiPartReq = (MultipartHttpServletRequest) request;
		}
		if (multiPartReq != null) {
			MultipartFile file = multiPartReq.getFile("file");
			String name = file.getOriginalFilename();
			if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
				InputStream input = file.getInputStream();
				try {
					String bigData = multiPartReq.getParameter("bigData");
					String startRow = multiPartReq.getParameter("startRow");
					String endRow = multiPartReq.getParameter("endRow");
					String excelModelId = multiPartReq.getParameter("excelModelId");
					String fileExtension = null;
					if (name.endsWith(".xlsx")) {
						fileExtension = "xlsx";
					} else if (name.endsWith(".xls")) {
						fileExtension = "xls";
					}
					boolean supportBigData = false;
					if (StringUtils.isNotEmpty(bigData) && bigData.equals("true")) {
						supportBigData = true;
					}
					
					Map<String, Object> parameters = new HashMap<String, Object>();
					Enumeration paraNames = multiPartReq.getParameterNames();
					for (Enumeration e = paraNames; e.hasMoreElements();) {
						String thisName = e.nextElement().toString();
						String thisValue = multiPartReq.getParameter(thisName);
						parameters.put(thisName, thisValue);
					}
					ImportContext.setParameters(parameters);
					IExcelParser excelParser = (IExcelParser) getExcelParser(fileExtension, supportBigData);
					ExcelDataWrapper excelDataWrapper = excelParser.parse(excelModelId, Integer.valueOf(startRow), Integer.valueOf(endRow), input);
					excelParser.put2Cache(excelDataWrapper);
				} finally {
					IOUtils.closeQuietly(input);
				}
			}
		}
	}

	private IExcelParser getExcelParser(String fileExtension, boolean supportBigData) {
		Map<String, IExcelParser> map = ContextHolder.getApplicationContext().getBeansOfType(IExcelParser.class, true, true);
		for (IExcelParser parser : map.values()) {
			if (parser.supportFileExtension(fileExtension)) {
				if ((supportBigData && parser.supportBigData()) || !supportBigData && !parser.supportBigData()) {
					return parser;
				}
			}
		}
		return null;
	}
}
