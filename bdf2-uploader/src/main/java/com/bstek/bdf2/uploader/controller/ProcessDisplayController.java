package com.bstek.bdf2.uploader.controller;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.uploader.model.UploadDefinition;
import com.bstek.bdf2.uploader.service.IFileService;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author Jacky.gao
 * @since 2013-5-6
 */
public class ProcessDisplayController extends AbstractResolver {
	private IFileService fileService;
	private static final String ID_KEY = "id";

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		String id = req.getParameter(ID_KEY);
		if (!StringUtils.hasText(id)) {
			id = (String) req.getAttribute(ID_KEY);
		}
		if (StringUtils.isEmpty(id)) {
			throw new RuntimeException("Upload definition id can not be null!");
		}
		UploadDefinition definition = fileService.getUploadDefinition(id);
		if (StringUtils.isEmpty(definition.getUploadProcessorKey())) {
			throw new RuntimeException("Upload definition [" + id
					+ "] has not set processor!");
		}
		InputStream in = fileService.getFile(definition);
		BufferedInputStream bin = new BufferedInputStream(in);
		ServletOutputStream out = res.getOutputStream();
		try {
			IOUtils.copy(bin, out);
		} finally {
			IOUtils.closeQuietly(in);
		}
		out.flush();
		out.close();
		return null;
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}
}
