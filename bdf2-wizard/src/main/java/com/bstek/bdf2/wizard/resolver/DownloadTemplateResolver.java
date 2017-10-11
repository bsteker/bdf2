package com.bstek.bdf2.wizard.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.bdf2.wizard.utils.WizardFileUtils;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.util.FileUtils;
import com.bstek.dorado.web.resolver.AbstractResolver;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 */
public class DownloadTemplateResolver extends AbstractResolver implements InitializingBean {

	private boolean deleteCache = true;

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		doDownloadFile(request, response);
		return null;
	}

	private void doDownloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String name = request.getParameter("name") + ".zip";
		if (!StringUtils.hasText(id) || !id.matches("[a-z0-9-]+")) {
			return;
		}
		if (!StringUtils.hasText(name) || !name.matches("[^/\\\\?<>*:\"|]+(\\.(zip))$")) {
			return;
		}
		response.setHeader("Server", "http://www.bstek.com");
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Connection", "close");
		response.setHeader("Accept-Ranges", "bytes");
		name = new String(name.getBytes("gb2312"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
		FileInputStream input = null;
		OutputStream output = null;
		try {
			File file = WizardFileUtils.getTempFile(id, name);
			input = new FileInputStream(file);
			output = response.getOutputStream();
			IOUtils.copy(input, output);
			output.flush();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
			if (deleteCache) {
				FileUtils.removeDirectory(WizardFileUtils.getTempFile(id));
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		deleteCache = Configure.getBoolean("bdf2.wizard.deleteCache");
	}
}
